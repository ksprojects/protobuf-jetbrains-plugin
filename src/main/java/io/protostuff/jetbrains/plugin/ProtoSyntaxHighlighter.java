package io.protostuff.jetbrains.plugin;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import io.protostuff.compiler.parser.ProtoLexer;
import io.protostuff.compiler.parser.ProtoParser;
import org.antlr.jetbrains.adapter.lexer.ANTLRLexerAdaptor;
import org.antlr.jetbrains.adapter.lexer.PSIElementTypeFactory;
import org.antlr.jetbrains.adapter.lexer.TokenIElementType;
import org.jetbrains.annotations.NotNull;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

/**
 * A highlighter is really just a mapping from token type to
 * some text attributes using {@link #getTokenHighlights(IElementType)}.
 * The reason that it returns an array, TextAttributesKey[], is
 * that you might want to mix the attributes of a few known highlighters.
 * A {@link TextAttributesKey} is just a name for that a theme
 * or IDE skin can set. For example, {@link DefaultLanguageHighlighterColors#KEYWORD}
 * is the key that maps to what identifiers look like in the editor.
 * To change it, see dialog: Editor > Colors & Fonts > Language Defaults.
 * <p>
 * From <a href="http://www.jetbrains.org/intellij/sdk/docs/reference_guide/custom_language_support/syntax_highlighting_and_error_highlighting.html">doc</a>:
 * "The mapping of the TextAttributesKey to specific attributes used
 * in an editor is defined by the EditorColorsScheme class, and can
 * be configured by the user if the plugin provides an appropriate
 * configuration interface.
 * ...
 * The syntax highlighter returns the {@link TextAttributesKey}
 * instances for each token type which needs special highlighting.
 * For highlighting lexer errors, the standard TextAttributesKey
 * for bad characters HighlighterColors.BAD_CHARACTER can be used."
 */
public class ProtoSyntaxHighlighter extends SyntaxHighlighterBase {
    public static final TextAttributesKey ID =
            createTextAttributesKey("PROTO_ID", DefaultLanguageHighlighterColors.IDENTIFIER);
    public static final TextAttributesKey KEYWORD =
            createTextAttributesKey("PROTO_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD);
    public static final TextAttributesKey STRING =
            createTextAttributesKey("PROTO_STRING", DefaultLanguageHighlighterColors.STRING);
    public static final TextAttributesKey NUMBER =
            createTextAttributesKey("PROTO_NUMBER", DefaultLanguageHighlighterColors.NUMBER);
    public static final TextAttributesKey LINE_COMMENT =
            createTextAttributesKey("PROTO_LINE_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT);
    public static final TextAttributesKey BLOCK_COMMENT =
            createTextAttributesKey("PROTO_BLOCK_COMMENT", DefaultLanguageHighlighterColors.BLOCK_COMMENT);
    private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];

    static {
        PSIElementTypeFactory.defineLanguageIElementTypes(ProtoLanguage.INSTANCE,
                ProtoParser.tokenNames,
                ProtoParser.ruleNames);
    }

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        ProtoLexer lexer = new ProtoLexer(null);
        return new ANTLRLexerAdaptor(ProtoLanguage.INSTANCE, lexer);
    }

    @NotNull
    @Override
    public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
        if (!(tokenType instanceof TokenIElementType)) return EMPTY_KEYS;
        TokenIElementType myType = (TokenIElementType) tokenType;
        int antlrTokenType = myType.getANTLRTokenType();
        TextAttributesKey attrKey;
        switch (antlrTokenType) {
            case ProtoLexer.INTEGER_VALUE:
            case ProtoLexer.FLOAT_VALUE:
                attrKey = NUMBER;
                break;
            case ProtoLexer.STRING_VALUE:
                attrKey = STRING;
                break;
            case ProtoLexer.COMMENT:
                attrKey = LINE_COMMENT;
                break;
            case ProtoLexer.LINE_COMMENT:
                attrKey = BLOCK_COMMENT;
                break;
            default:
                return EMPTY_KEYS;
        }
        return new TextAttributesKey[]{attrKey};
    }
}
