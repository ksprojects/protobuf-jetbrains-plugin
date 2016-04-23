package io.protostuff.jetbrains.plugin.formatter;

import com.intellij.formatting.Alignment;
import com.intellij.formatting.Block;
import com.intellij.formatting.Indent;
import com.intellij.formatting.Spacing;
import com.intellij.lang.ASTNode;
import com.intellij.psi.formatter.FormatterUtil;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Abstract block for all constructs that have children in curly braces.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public class ParentBlock extends StatementBlock {

    private final Set<Block> headerBlocks = new HashSet<>();

    protected ParentBlock(@NotNull ASTNode node, @Nullable Alignment alignment, Indent indent) {
        super(node, alignment, indent);
    }

    enum State {
        BEFORE_LEFT_CURLY_BRACE,
        AFTER_LEFT_CURLY_BRACE
    }

    @Override
    protected List<Block> buildChildren() {
        ASTNode child = getNode().getFirstChildNode();
        State state = State.BEFORE_LEFT_CURLY_BRACE;
        List<Block> result = new ArrayList<>();
        Alignment childAlignment = Alignment.createAlignment();
        while (child != null) {
            if (!FormatterUtil.containsWhiteSpacesOnly(child)) {
                IElementType elementType = child.getElementType();
                if (LCURLY.equals(elementType)) {
                    state = State.AFTER_LEFT_CURLY_BRACE;
                    result.add(BlockFactory.createBlock(child, myAlignment, Indent.getNoneIndent()));
                } else if (RCURLY.equals(elementType)) {
                    result.add(BlockFactory.createBlock(child, myAlignment, Indent.getNoneIndent()));
                } else {
                    switch (state) {
                        case BEFORE_LEFT_CURLY_BRACE:
                            Block block = BlockFactory.createBlock(child, myAlignment, Indent.getNoneIndent());
                            headerBlocks.add(block);
                            result.add(block);
                            break;
                        case AFTER_LEFT_CURLY_BRACE:
                            result.add(BlockFactory.createBlock(child, childAlignment, Indent.getNormalIndent(true)));
                            break;
                        default:
                            throw new IllegalStateException(state.toString());
                    }
                }
            }
            child = child.getTreeNext();
        }
        return result;
    }

    @Nullable
    @Override
    public Spacing getSpacing(@Nullable Block child1, @NotNull Block child2) {
        if (headerBlocks.contains(child1)) {
            return super.getSpacing(child1, child2);
        }
        return NEW_LINE;
    }

    @Override
    public boolean isLeaf() {
        return false;
    }
}
