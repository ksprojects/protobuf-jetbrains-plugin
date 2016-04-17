package io.protostuff.jetbrains.plugin.formatter;

import com.google.common.base.Preconditions;
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

    protected ProtoFileBlock(@NotNull ASTNode node, @Nullable Wrap wrap, @Nullable Alignment alignment) {
        super(node, wrap, alignment);

    }

    @Override
    protected List<Block> buildChildren() {
        List<Block> blocks = new ArrayList<>();
        ASTNode protoRootNode = myNode.findChildByType(rule(RULE_proto));
        Preconditions.checkNotNull(protoRootNode, "Could not find root proto node");
        ASTNode child = protoRootNode.getFirstChildNode();
        Alignment alignment = Alignment.createAlignment();
        while (child != null) {
            if (Formatting.isNotEmpty(child)) {
                Block block = createBlock(child, alignment);
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
            return Formatting.NONE;
        }
        if (child1 instanceof ImportBlock
                && child2 instanceof ImportBlock) {
            return Formatting.LINE_BREAK;
        }
        if (child1 instanceof CommentBlock
                || child1 instanceof LineCommentBlock) {
            return Formatting.NONE;
        }
        return Formatting.BLANK_LINE;
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
