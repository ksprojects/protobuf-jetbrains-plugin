package io.protostuff.jetbrains.plugin;

import static io.protostuff.compiler.parser.ProtoLexer.COMMENT;
import static io.protostuff.compiler.parser.ProtoLexer.LINE_COMMENT;
import static io.protostuff.compiler.parser.ProtoParser.RULE_enumBlock;
import static io.protostuff.compiler.parser.ProtoParser.RULE_extendBlock;
import static io.protostuff.compiler.parser.ProtoParser.RULE_groupBlock;
import static io.protostuff.compiler.parser.ProtoParser.RULE_messageBlock;
import static io.protostuff.compiler.parser.ProtoParser.RULE_oneof;
import static io.protostuff.compiler.parser.ProtoParser.RULE_serviceBlock;
import static io.protostuff.jetbrains.plugin.ProtoParserDefinition.COMMENT_TOKEN_SET;
import static io.protostuff.jetbrains.plugin.ProtoParserDefinition.rule;
import static io.protostuff.jetbrains.plugin.ProtoParserDefinition.token;

import com.google.common.collect.ImmutableMap;
import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilder;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.util.Couple;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.TokenType;
import com.intellij.psi.impl.source.tree.FileElement;
import com.intellij.psi.tree.IElementType;
import io.protostuff.jetbrains.plugin.psi.EnumNode;
import io.protostuff.jetbrains.plugin.psi.ExtendNode;
import io.protostuff.jetbrains.plugin.psi.GroupNode;
import io.protostuff.jetbrains.plugin.psi.MessageNode;
import io.protostuff.jetbrains.plugin.psi.OneOfNode;
import io.protostuff.jetbrains.plugin.psi.ServiceNode;
import io.protostuff.jetbrains.plugin.psi.TypeReferenceNode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Folding ranges builder.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public class ProtoFoldingBuilder implements FoldingBuilder, DumbAware {

    private static final Map<IElementType, Function<ASTNode, String>> PROVIDERS = ImmutableMap.<IElementType, Function<ASTNode, String>>builder()
            .put(rule(RULE_messageBlock), node -> {
                MessageNode messageNode = node.getPsi(MessageNode.class);
                String name = messageNode.getName();
                return "message " + name + " {...}";
            })
            .put(rule(RULE_enumBlock), node -> {
                EnumNode enumNode = node.getPsi(EnumNode.class);
                String name = enumNode.getName();
                return "enum " + name + " {...}";
            })
            .put(rule(RULE_serviceBlock), node -> {
                ServiceNode serviceNode = node.getPsi(ServiceNode.class);
                String name = serviceNode.getName();
                return "service " + name + " {...}";
            })
            .put(rule(RULE_groupBlock), node -> {
                GroupNode groupNode = node.getPsi(GroupNode.class);
                String name = groupNode.getName();
                return "group " + name + " {...}";
            })
            .put(rule(RULE_oneof), node -> {
                OneOfNode oneOfNode = node.getPsi(OneOfNode.class);
                String name = oneOfNode.getName();
                return "oneof " + name + " {...}";
            })
            .put(rule(RULE_extendBlock), node -> {
                ExtendNode extendNode = node.getPsi(ExtendNode.class);
                TypeReferenceNode target = extendNode.getTarget();
                String name = target != null ? target.getText() : "...";
                return "extend " + name + " {...}";
            })
            .put(token(LINE_COMMENT), node -> "//...")
            .put(token(COMMENT), node -> "/*...*/")
            .build();

    @NotNull
    @Override
    public FoldingDescriptor[] buildFoldRegions(@NotNull ASTNode node, @NotNull Document document) {
        final List<FoldingDescriptor> descriptors = new ArrayList<>();
        collectDescriptorsRecursively(node, document, descriptors);
        return descriptors.toArray(new FoldingDescriptor[descriptors.size()]);
    }

    @Nullable
    @Override
    public String getPlaceholderText(@NotNull ASTNode node) {
        final IElementType type = node.getElementType();
        Function<ASTNode, String> provider = PROVIDERS.getOrDefault(type, ast -> "...");
        return provider.apply(node);
    }

    @Override
    public boolean isCollapsedByDefault(@NotNull ASTNode node) {
        // This code should collapse header comments.
        // However, in most of the cases it will not work,
        // as when file is open caret is at the beginning of the document,
        // thus preventing collapsing it.
        // TODO: Collapse header comment when file is opened
        return node.getTreeParent() instanceof FileElement
                && findPreviousNonWhitespaceOrCommentNode(node) == null;
    }

    @Nullable
    private ASTNode findPreviousNonWhitespaceOrCommentNode(@NotNull ASTNode node) {
        ASTNode tmp = node;
        while (tmp != null) {
            IElementType type = tmp.getElementType();
            if (!(tmp instanceof PsiWhiteSpace
                    || type == token(COMMENT)
                    || type == token(LINE_COMMENT))) {
                break;
            }
            tmp = tmp.getTreePrev();
        }
        return tmp;
    }

    private static void collectDescriptorsRecursively(@NotNull ASTNode node,
                                                      @NotNull Document document,
                                                      @NotNull List<FoldingDescriptor> descriptors) {
        final IElementType type = node.getElementType();
        if (type == token(COMMENT)
                && spanMultipleLines(node, document)) {
            descriptors.add(new FoldingDescriptor(node, node.getTextRange()));
        }
        if (type == token(LINE_COMMENT)) {
            final Couple<PsiElement> commentRange = expandLineCommentsRange(node.getPsi());
            final int startOffset = commentRange.getFirst().getTextRange().getStartOffset();
            final int endOffset = commentRange.getSecond().getTextRange().getEndOffset();
            if (document.getLineNumber(startOffset) != document.getLineNumber(endOffset)) {
                descriptors.add(new FoldingDescriptor(node, new TextRange(startOffset, endOffset)));
            }
        }
        if (PROVIDERS.keySet().contains(type)
                && spanMultipleLines(node, document)) {
            descriptors.add(new FoldingDescriptor(node, node.getTextRange()));
        }
        for (ASTNode child : node.getChildren(null)) {
            collectDescriptorsRecursively(child, document, descriptors);
        }
    }

    @NotNull
    private static Couple<PsiElement> expandLineCommentsRange(@NotNull PsiElement anchor) {
        return Couple.of(findFurthestSiblingOfSameType(anchor, false), findFurthestSiblingOfSameType(anchor, true));
    }

    private static PsiElement findFurthestSiblingOfSameType(@NotNull PsiElement anchor, boolean after) {
        ASTNode node = anchor.getNode();
        final IElementType expectedType = node.getElementType();
        ASTNode lastSeen = node;
        while (node != null) {
            final IElementType elementType = node.getElementType();
            if (elementType == expectedType) {
                lastSeen = node;
            } else if (elementType == TokenType.WHITE_SPACE) {
                if (expectedType == token(LINE_COMMENT)
                        && node.getText().indexOf('\n', 1) != -1) {
                    break;
                }
            } else if (!COMMENT_TOKEN_SET.contains(elementType) || COMMENT_TOKEN_SET.contains(expectedType)) {
                break;
            }
            node = after ? node.getTreeNext() : node.getTreePrev();
        }
        return lastSeen.getPsi();
    }

    private static boolean spanMultipleLines(@NotNull ASTNode node, @NotNull Document document) {
        final TextRange range = node.getTextRange();
        return document.getLineNumber(range.getStartOffset()) < document.getLineNumber(range.getEndOffset());
    }
}
