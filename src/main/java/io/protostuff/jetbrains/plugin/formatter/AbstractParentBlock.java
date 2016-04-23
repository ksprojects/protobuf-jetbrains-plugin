package io.protostuff.jetbrains.plugin.formatter;

import com.intellij.formatting.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.formatter.FormatterUtil;
import com.intellij.psi.formatter.common.AbstractBlock;
import io.protostuff.compiler.parser.ProtoParser;
import io.protostuff.jetbrains.plugin.ProtoParserDefinition;
import org.antlr.jetbrains.adapter.lexer.TokenIElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract block for all constructs that have children in curly braces.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public abstract class AbstractParentBlock extends AbstractBlock {

    public static final TokenIElementType LEFT_CURLY_BRACE = ProtoParserDefinition.token(ProtoParser.LCURLY);
    public static final TokenIElementType RIGHT_CURLY_BRACE = ProtoParserDefinition.token(ProtoParser.RCURLY);
    public static final Spacing NEW_LINE = Spacing.createSpacing(0, 0, 1, true, 2);
    public static final Spacing SPACE = Spacing.createSpacing(1, 1, 0, false, 0);

    private final Indent indent;

    protected AbstractParentBlock(@NotNull ASTNode node, @Nullable Wrap wrap, @Nullable Alignment alignment, Indent indent) {
        super(node, wrap, alignment);
        this.indent = indent;
    }

    protected AbstractParentBlock(@NotNull ASTNode node, @Nullable Alignment alignment) {
        super(node, null, alignment);
        this.indent = Indent.getNoneIndent();
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
        Block openBrace = null;
        Block closeBrace = null;
        Alignment childAlignment = Alignment.createAlignment();
        while (child != null) {
            if (!FormatterUtil.containsWhiteSpacesOnly(child)) {

                if (child.getElementType() == LEFT_CURLY_BRACE) {
                    state = State.AFTER_LEFT_CURLY_BRACE;
                    openBrace = new LeafBlock(child, null, myAlignment, Indent.getNoneIndent());
                    result.add(openBrace);
                } else if (child.getElementType() == RIGHT_CURLY_BRACE) {
                    closeBrace = new LeafBlock(child, null, myAlignment, Indent.getNoneIndent());
                    result.add(closeBrace);
                } else {
                    switch (state) {
                        case BEFORE_LEFT_CURLY_BRACE:
                            result.add(new LeafBlock(child, null, myAlignment, Indent.getNoneIndent()));
                            break;
                        case AFTER_LEFT_CURLY_BRACE:
                            result.add(createChildBlock(child, getWrap(), childAlignment, Indent.getNormalIndent(true)));
                            break;
                    }
                }
            }
            child = child.getTreeNext();
        }
        return result;
    }

    abstract Block createChildBlock(ASTNode child, Wrap wrap, Alignment childAlignment, Indent noneIndent);

    @Nullable
    @Override
    public Spacing getSpacing(@Nullable Block child1, @NotNull Block child2) {
        if (child1 instanceof LeafBlock) {
            LeafBlock a = (LeafBlock) child1;
            if (a.getNode().getElementType() == LEFT_CURLY_BRACE) {
                return NEW_LINE;
            } else {
                return SPACE;
            }
        } else {
            return NEW_LINE;
        }
    }

    @Override
    public boolean isLeaf() {
        return true;
    }

    @Override
    public Indent getIndent() {
        return indent;
    }
}
