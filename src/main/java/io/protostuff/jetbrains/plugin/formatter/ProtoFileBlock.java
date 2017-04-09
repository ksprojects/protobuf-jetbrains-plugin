package io.protostuff.jetbrains.plugin.formatter;

import static io.protostuff.jetbrains.plugin.formatter.BlockFactory.createBlock;
import static io.protostuff.jetbrains.plugin.formatter.StatementBlock.COMMENT;
import static io.protostuff.jetbrains.plugin.formatter.StatementBlock.LINE_COMMENT;
import static io.protostuff.jetbrains.plugin.formatter.StatementBlock.SPACE_OR_NEW_LINE;

import com.intellij.formatting.ASTBlock;
import com.intellij.formatting.Alignment;
import com.intellij.formatting.Block;
import com.intellij.formatting.Indent;
import com.intellij.formatting.Spacing;
import com.intellij.formatting.Wrap;
import com.intellij.lang.ASTNode;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.formatter.FormatterUtil;
import com.intellij.psi.formatter.common.AbstractBlock;
import com.intellij.psi.tree.IElementType;
import io.protostuff.compiler.parser.ProtoParser;
import io.protostuff.jetbrains.plugin.ProtoParserDefinition;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Proto file block for formatter.
 *
 * @author Kostiantyn Shchepanovskyi
 */
class ProtoFileBlock extends AbstractBlock {

    private final CodeStyleSettings settings;

    ProtoFileBlock(@NotNull ASTNode node, @Nullable Wrap wrap, @Nullable Alignment alignment, CodeStyleSettings settings) {
        super(node, wrap, alignment);
        this.settings = settings;

    }

    @Override
    protected List<Block> buildChildren() {
        List<Block> blocks = new ArrayList<>();
        ASTNode child = myNode.getFirstChildNode();
        while (child != null) {
            if (!FormatterUtil.containsWhiteSpacesOnly(child)) {
                IElementType elementType = child.getElementType();
                if (ProtoParserDefinition.rule(ProtoParser.RULE_proto).equals(elementType)) {
                    appendProtoBlocks(child, blocks);
                } else {
                    // Comments are not part of root rule, we have to append them separately
                    blocks.add(new LeafBlock(child, Alignment.createAlignment(), Indent.getNoneIndent(), settings));
                }
            }
            child = child.getTreeNext();
        }
        return blocks;
    }

    private void appendProtoBlocks(ASTNode protoRootNode, List<Block> blocks) {
        ASTNode child = protoRootNode.getFirstChildNode();
        Alignment alignment = Alignment.createAlignment();
        while (child != null) {
            if (!FormatterUtil.containsWhiteSpacesOnly(child)) {
                Block block = createBlock(child, alignment, Indent.getNoneIndent(), settings);
                blocks.add(block);
            }
            child = child.getTreeNext();
        }
    }

    @Nullable
    @Override
    public Spacing getSpacing(@Nullable Block child1, @NotNull Block child2) {
        if (child1 == null) {
            return StatementBlock.NONE;
        }
        if (child2 instanceof ASTBlock) {
            ASTBlock block = (ASTBlock) child2;
            IElementType elementType = block.getNode().getElementType();

            // Do not move trailing comments to new line.
            if (LINE_COMMENT.equals(elementType)
                    || COMMENT.equals(elementType)) {
                return SPACE_OR_NEW_LINE;
            }
        }
        return StatementBlock.NEW_LINE;
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    @Override
    public Indent getIndent() {
        return Indent.getAbsoluteNoneIndent();
    }
}
