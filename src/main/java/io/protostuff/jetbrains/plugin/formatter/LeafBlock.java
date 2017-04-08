package io.protostuff.jetbrains.plugin.formatter;

import com.intellij.formatting.ASTBlock;
import com.intellij.formatting.Alignment;
import com.intellij.formatting.Block;
import com.intellij.formatting.ChildAttributes;
import com.intellij.formatting.Indent;
import com.intellij.formatting.Spacing;
import com.intellij.formatting.Wrap;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import java.util.Collections;
import java.util.List;
import org.jetbrains.annotations.NotNull;

class LeafBlock implements ASTBlock {
    private final ASTNode node;
    private final Alignment alignment;
    private final Indent myIndent;

    LeafBlock(ASTNode node, Alignment alignment, Indent indent, CodeStyleSettings settings) {
        this.node = node;
        this.alignment = alignment;
        myIndent = indent;
    }

    @Override
    public ASTNode getNode() {
        return node;
    }

    @Override
    @NotNull
    public TextRange getTextRange() {
        return node.getTextRange();
    }

    @Override
    @NotNull
    public List<Block> getSubBlocks() {
        return Collections.emptyList();
    }

    @Override
    public Wrap getWrap() {
        return null;
    }

    @Override
    public Indent getIndent() {
        return myIndent;
    }

    @Override
    public Alignment getAlignment() {
        return alignment;
    }

    @Override
    public Spacing getSpacing(Block child1, @NotNull Block child2) {
        return null;
    }

    @Override
    @NotNull
    public ChildAttributes getChildAttributes(final int newChildIndex) {
        return new ChildAttributes(getIndent(), null);
    }

    @Override
    public boolean isIncomplete() {
        return false;
    }

    @Override
    public boolean isLeaf() {
        return true;
    }

}
