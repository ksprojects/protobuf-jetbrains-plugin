package io.protostuff.jetbrains.plugin;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import io.protostuff.compiler.parser.ProtoLexer;
import io.protostuff.compiler.parser.ProtoParser;
import io.protostuff.jetbrains.plugin.psi.MessageBlockSubtree;
import io.protostuff.jetbrains.plugin.psi.ProtoPsiFileRoot;
import org.antlr.jetbrains.adapter.lexer.ANTLRLexerAdaptor;
import org.antlr.jetbrains.adapter.lexer.PSIElementTypeFactory;
import org.antlr.jetbrains.adapter.lexer.RuleIElementType;
import org.antlr.jetbrains.adapter.lexer.TokenIElementType;
import org.antlr.jetbrains.adapter.parser.ANTLRParserAdaptor;
import org.antlr.jetbrains.adapter.psi.ANTLRPsiNode;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.ParseTree;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static io.protostuff.compiler.parser.ProtoLexer.*;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public class ProtoParserDefinition implements ParserDefinition {

    private static final IFileElementType FILE;
    private static final TokenSet COMMENTS;
    private static final TokenSet WHITESPACE;
    private static final TokenSet STRING;
    public static TokenIElementType ID;

    static {
        PSIElementTypeFactory.defineLanguageIElementTypes(ProtoLanguage.INSTANCE,
                ProtoParser.tokenNames, ProtoParser.ruleNames);
        List<TokenIElementType> tokenIElementTypes =
                PSIElementTypeFactory.getTokenIElementTypes(ProtoLanguage.INSTANCE);
        ID = tokenIElementTypes.get(ProtoLexer.NAME);
        FILE = new IFileElementType(ProtoLanguage.INSTANCE);
        COMMENTS = PSIElementTypeFactory.createTokenSet(ProtoLanguage.INSTANCE, COMMENT, LINE_COMMENT);
        WHITESPACE = PSIElementTypeFactory.createTokenSet(ProtoLanguage.INSTANCE, WS);
        STRING = PSIElementTypeFactory.createTokenSet(ProtoLanguage.INSTANCE, STRING_VALUE);
    }

    @NotNull
    @Override
    public Lexer createLexer(Project project) {
        ProtoLexer lexer = new ProtoLexer(null);
        return new ANTLRLexerAdaptor(ProtoLanguage.INSTANCE, lexer);
    }

    @Override
    public PsiParser createParser(Project project) {
        final ProtoParser parser = new ProtoParser(null);
        return new ANTLRParserAdaptor(ProtoLanguage.INSTANCE, parser) {
            @Override
            protected ParseTree parse(Parser parser, IElementType root) {
                // start rule depends on root passed in; sometimes we want to create an ID node etc...
                if (root instanceof IFileElementType) {
                    return ((ProtoParser) parser).proto();
                }
                // let's hope it's an ID as needed by "rename function"
                return ((ProtoParser) parser).name();
            }
        };
    }

    @Override
    public IFileElementType getFileNodeType() {
        return FILE;
    }

    @NotNull
    @Override
    public TokenSet getWhitespaceTokens() {
        return WHITESPACE;
    }

    @NotNull
    @Override
    public TokenSet getCommentTokens() {
        return COMMENTS;
    }

    @NotNull
    @Override
    public TokenSet getStringLiteralElements() {
        return STRING;
    }

    @NotNull
    @Override
    public PsiElement createElement(ASTNode node) {
        IElementType elType = node.getElementType();
        if (elType instanceof TokenIElementType) {
            return new ANTLRPsiNode(node);
        }
        if (!(elType instanceof RuleIElementType)) {
            return new ANTLRPsiNode(node);
        }
        RuleIElementType ruleElType = (RuleIElementType) elType;
        switch (ruleElType.getRuleIndex()) {
            case ProtoParser.RULE_messageBlock:
                return new MessageBlockSubtree(node);
            // TODO
            default:
                return new ANTLRPsiNode(node);
        }
    }

    @Override
    public PsiFile createFile(FileViewProvider viewProvider) {
        return new ProtoPsiFileRoot(viewProvider);
    }

    @Override
    public SpaceRequirements spaceExistanceTypeBetweenTokens(ASTNode left, ASTNode right) {
        return SpaceRequirements.MAY;
    }
}
