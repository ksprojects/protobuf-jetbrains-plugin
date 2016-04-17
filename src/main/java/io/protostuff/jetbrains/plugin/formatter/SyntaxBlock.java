package io.protostuff.jetbrains.plugin.formatter;

import com.intellij.formatting.Alignment;
import com.intellij.formatting.Block;
import com.intellij.formatting.Indent;
import com.intellij.formatting.Spacing;
import com.intellij.lang.ASTNode;
import com.intellij.psi.formatter.common.AbstractBlock;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public class SyntaxBlock extends AbstractBlock {

    protected SyntaxBlock(@NotNull ASTNode node, @Nullable Alignment alignment) {
        super(node, null, alignment);
    }


    @Override
    protected List<Block> buildChildren() {
        return Collections.emptyList();
    }

    @Nullable
    @Override
    public Spacing getSpacing(@Nullable Block child1, @NotNull Block child2) {
        return Spacing.getReadOnlySpacing();
    }

    @Override
    public boolean isLeaf() {
        return true;
    }

    @Override
    public Indent getIndent() {
        return Indent.getAbsoluteNoneIndent();
    }
}
