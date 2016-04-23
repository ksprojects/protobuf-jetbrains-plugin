package io.protostuff.jetbrains.plugin.formatter;

import com.intellij.formatting.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.formatter.FormatterUtil;
import com.intellij.psi.formatter.common.AbstractBlock;
import com.intellij.psi.tree.IElementType;
import io.protostuff.compiler.parser.ProtoLexer;
import io.protostuff.jetbrains.plugin.ProtoLanguage;
import io.protostuff.jetbrains.plugin.ProtoParserDefinition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract block for all constructs that have children in curly braces.
 *
 * @author Kostiantyn Shchepanovskyi
 */
@SuppressWarnings("WeakerAccess")
class StatementBlock extends AbstractBlock {

    public static final Spacing NEW_LINE = Spacing.createSpacing(0, 0, 1, true, 2);
    public static final Spacing SPACE = Spacing.createSpacing(1, 1, 0, false, 0);
    public static final Spacing SPACE_OR_NEW_LINE = Spacing.createSpacing(1, 1, 0, true, 1);
    public static final Spacing NONE = Spacing.createSpacing(0, 0, 0, false, 0);

    public static final IElementType SEMICOLON = ProtoParserDefinition.token(ProtoLexer.SEMICOLON);
    public static final IElementType LCURLY = ProtoParserDefinition.token(ProtoLexer.LCURLY);
    public static final IElementType RCURLY = ProtoParserDefinition.token(ProtoLexer.RCURLY);
    public static final IElementType LPAREN = ProtoParserDefinition.token(ProtoLexer.LPAREN);
    public static final IElementType RPAREN = ProtoParserDefinition.token(ProtoLexer.RPAREN);
    public static final IElementType LSQUARE = ProtoParserDefinition.token(ProtoLexer.LSQUARE);
    public static final IElementType RSQUARE = ProtoParserDefinition.token(ProtoLexer.RSQUARE);
    public static final IElementType LT = ProtoParserDefinition.token(ProtoLexer.LT);
    public static final IElementType GT = ProtoParserDefinition.token(ProtoLexer.GT);
    public static final IElementType LINE_COMMENT = ProtoParserDefinition.token(ProtoLexer.LINE_COMMENT);
    public static final IElementType COMMENT = ProtoParserDefinition.token(ProtoLexer.COMMENT);
    public static final IElementType COMMA = ProtoParserDefinition.token(ProtoLexer.COMMA);
    private static final SpacingBuilder SB = new SpacingBuilder(new CodeStyleSettings(), ProtoLanguage.INSTANCE)
            .after(LINE_COMMENT).spacing(0, 0, 1, true, 2)
            .after(LCURLY).spacing(0, 0, 1, true, 2)
            .before(RCURLY).spacing(0, 0, 1, true, 2)
            .after(LPAREN).spacing(0, 0, 0, false, 0)
            .before(RPAREN).spacing(0, 0, 0, false, 0)
            .after(LSQUARE).spacing(0, 0, 0, false, 0)
            .before(RSQUARE).spacing(0, 0, 0, false, 0)
            .before(LT).spacing(0, 0, 0, false, 0)
            .after(LT).spacing(0, 0, 0, false, 0)
            .before(GT).spacing(0, 0, 0, false, 0)
            .before(COMMA).spacing(0, 0, 0, false, 0)
            .before(SEMICOLON).spacing(0, 0, 0, false, 0)
            .after(COMMA).spacing(1, 1, 0, false, 0);

    private final Indent indent;

    protected StatementBlock(@NotNull ASTNode node, @Nullable Alignment alignment, Indent indent) {
        super(node, null, alignment);
        this.indent = indent;
    }

    @Override
    protected List<Block> buildChildren() {
        ASTNode child = getNode().getFirstChildNode();
        List<Block> result = new ArrayList<>();
        while (child != null) {
            if (!FormatterUtil.containsWhiteSpacesOnly(child)) {
                Block block = BlockFactory.createBlock(child, Alignment.createAlignment(), Indent.getNoneIndent());
                result.add(block);
            }
            child = child.getTreeNext();
        }
        return result;
    }

    @Nullable
    @Override
    public Spacing getSpacing(@Nullable Block child1, @NotNull Block child2) {
        Spacing spacing = SB.getSpacing(this, child1, child2);
        if (spacing == null) {
            return SPACE;
        }
        return spacing;
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    @Override
    public Indent getIndent() {
        return indent;
    }
}
