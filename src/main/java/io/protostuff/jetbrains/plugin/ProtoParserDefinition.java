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
import io.protostuff.jetbrains.plugin.psi.*;
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

    public static final TokenIElementType ID;
    public static final TokenSet KEYWORDS;
    // Rules
    public static final IElementType R_TYPE_REFERENCE;
    public static final IElementType R_NAME;
    public static final IElementType R_FIELD_MODIFIER;
    private static final IFileElementType FILE;
    private static final TokenSet COMMENTS;
    private static final TokenSet WHITESPACE;
    private static final TokenSet STRING;

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


        KEYWORDS = PSIElementTypeFactory.createTokenSet(ProtoLanguage.INSTANCE,
                ProtoLexer.PACKAGE,
                ProtoLexer.SYNTAX,
                ProtoLexer.IMPORT,
                ProtoLexer.PUBLIC,
                ProtoLexer.OPTION,
                ProtoLexer.MESSAGE,
                ProtoLexer.GROUP,
                ProtoLexer.OPTIONAL,
                ProtoLexer.REQUIRED,
                ProtoLexer.REPEATED,
                ProtoLexer.ONEOF,
                ProtoLexer.EXTEND,
                ProtoLexer.EXTENSIONS,
                ProtoLexer.RESERVED,
                ProtoLexer.TO,
                ProtoLexer.MAX,
                ProtoLexer.ENUM,
                ProtoLexer.SERVICE,
                ProtoLexer.RPC,
                ProtoLexer.STREAM,
                ProtoLexer.RETURNS,
                ProtoLexer.MAP,
                ProtoLexer.BOOLEAN_VALUE,
                ProtoLexer.DOUBLE,
                ProtoLexer.FLOAT,
                ProtoLexer.INT32,
                ProtoLexer.INT64,
                ProtoLexer.UINT32,
                ProtoLexer.UINT64,
                ProtoLexer.SINT32,
                ProtoLexer.SINT64,
                ProtoLexer.FIXED32,
                ProtoLexer.FIXED64,
                ProtoLexer.SFIXED32,
                ProtoLexer.SFIXED64,
                ProtoLexer.BOOL,
                ProtoLexer.STRING,
                ProtoLexer.BYTES
        );

        List<RuleIElementType> ruleTypes = PSIElementTypeFactory.getRuleIElementTypes(ProtoLanguage.INSTANCE);

        R_TYPE_REFERENCE = ruleTypes.get(ProtoParser.RULE_typeReference);
        R_NAME = ruleTypes.get(ProtoParser.RULE_name);
        R_FIELD_MODIFIER = ruleTypes.get(ProtoParser.RULE_fieldModifier);
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
            case ProtoParser.RULE_syntax:
                return new SyntaxNode(node);
            case ProtoParser.RULE_packageStatement:
                return new PackageNode(node);
            case ProtoParser.RULE_importStatement:
                return new ImportNode(node);
            case ProtoParser.RULE_messageBlock:
                return new MessageNode(node);
            case ProtoParser.RULE_field:
                return new FieldNode(node);
            case ProtoParser.RULE_typeReference:
                return new TypeReferenceNode(node);
            case ProtoParser.RULE_groupBlock:
                return new GroupNode(node);
            case ProtoParser.RULE_enumBlock:
                return new EnumNode(node);
            case ProtoParser.RULE_enumConstant:
                return new EnumConstantNode(node);
            case ProtoParser.RULE_serviceBlock:
                return new ServiceNode(node);
            case ProtoParser.RULE_rpcMethod:
                return new RpcMethodNode(node);
            case ProtoParser.RULE_optionEntry:
                return new OptionNode(node);
            case ProtoParser.RULE_oneof:
                return new OneOfNode(node);
            case ProtoParser.RULE_extendBlock:
                return new ExtendNode(node);
            case ProtoParser.RULE_extensions:
                return new ExtensionsNode(node);
            case ProtoParser.RULE_map:
                return new MapNode(node);
            case ProtoParser.RULE_mapKey:
                return new MapKeyNode(node);
            case ProtoParser.RULE_optionValue:
                return new OptionValueNode(node);
            case ProtoParser.RULE_range:
                return new RangeNode(node);
            case ProtoParser.RULE_reserved:
                return new ReservedFieldsNode(node);
            case ProtoParser.RULE_rpcType:
                return new RpcMethodTypeNode(node);
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
