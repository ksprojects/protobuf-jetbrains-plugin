package io.protostuff.jetbrains.plugin.formatter;

import com.intellij.formatting.*;
import com.intellij.openapi.util.TextRange;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Single-line statement block, contains leaf blocks separated by spaces.
 */
public class SyntheticStatementBlock implements Block {

    public static final Spacing SPACE = Spacing.createSpacing(1, 1, 0, false, 0);
    private final Wrap myWrap;
    private final Alignment myAlignment;

    private final Indent myIndent;
    private final List<Block> children;

    public SyntheticStatementBlock(final Wrap wrap,
                                   final Alignment alignment,
                                   Indent indent, List<Block> children) {
        myWrap = wrap;
        myAlignment = alignment;
        myIndent = indent;
        this.children = children;
    }

    @Override
    @NotNull
    public TextRange getTextRange() {
        Block firstChild = children.get(0);
        Block lastChild = children.get(children.size() - 1);
        int startOffset = firstChild.getTextRange().getStartOffset();
        int endOffset = lastChild.getTextRange().getEndOffset();
        return TextRange.create(startOffset, endOffset);
    }

    @Override
    @NotNull
    public List<Block> getSubBlocks() {
        return children;
    }

    @Override
    public Wrap getWrap() {
        return myWrap;
    }

    @Override
    public Indent getIndent() {
        return myIndent;
    }

    @Override
    public Alignment getAlignment() {
        return myAlignment;
    }

    @Override
    public Spacing getSpacing(Block child1, @NotNull Block child2) {
        return SPACE;
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
