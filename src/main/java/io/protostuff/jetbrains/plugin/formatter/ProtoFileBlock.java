package io.protostuff.jetbrains.plugin.formatter;

import com.intellij.formatting.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.formatter.common.AbstractBlock;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static io.protostuff.compiler.parser.ProtoParser.RULE_proto;
import static io.protostuff.jetbrains.plugin.ProtoParserDefinition.rule;
import static io.protostuff.jetbrains.plugin.formatter.BlockFactory.createBlock;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public class ProtoFileBlock extends AbstractBlock {

    private static final Spacing BLANK_LINE = Spacing.createSpacing(0, 0, 2, true, 2);
    private static final Spacing LINE_BREAK = Spacing.createSpacing(0, 0, 1, true, 2);
    private static final Spacing NONE = Spacing.createSpacing(0, 0, 0, true, 2);

    protected ProtoFileBlock(@NotNull ASTNode node, @Nullable Wrap wrap, @Nullable Alignment alignment) {
        super(node, wrap, alignment);

    }

    private static boolean canBeCorrectBlock(com.intellij.lang.ASTNode node) {
        return node.getText().trim().length() > 0;
    }

    @Override
    protected List<Block> buildChildren() {
        List<Block> blocks = new ArrayList<>();
        ASTNode protoRootNode = myNode.findChildByType(rule(RULE_proto));
        ASTNode child = protoRootNode.getFirstChildNode();
        while (child != null) {
            if (canBeCorrectBlock(child)) {
                Block block = createBlock(child);
                blocks.add(block);
            }
            child = child.getTreeNext();
        }
        return blocks;
    }

    @Nullable
    @Override
    public Spacing getSpacing(@Nullable Block child1, @NotNull Block child2) {
        if (child1 == null) {
            return NONE;
        }
        if (child1 instanceof ImportBlock
                && child2 instanceof ImportBlock) {
            return LINE_BREAK;
        }
        if (child1 instanceof CommentBlock
                || child1 instanceof LineCommentBlock) {
            return NONE;
        }
        return BLANK_LINE;
    }

    @Override
    public boolean isLeaf() {
        return myNode.getFirstChildNode() == null;
    }

    @Override
    public Indent getIndent() {
        return Indent.getAbsoluteNoneIndent();
    }
}
