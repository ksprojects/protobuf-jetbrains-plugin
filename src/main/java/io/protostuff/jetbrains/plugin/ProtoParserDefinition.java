package io.protostuff.jetbrains.plugin;

import com.google.common.base.Throwables;
import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
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

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.protostuff.compiler.parser.ProtoLexer.*;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public class ProtoParserDefinition implements ParserDefinition {

    static {
        PSIElementTypeFactory.defineLanguageIElementTypes(ProtoLanguage.INSTANCE,
                ProtoParser.tokenNames, ProtoParser.ruleNames);
    }

    private static final List<TokenIElementType> TOKEN_TYPES = PSIElementTypeFactory.getTokenIElementTypes(ProtoLanguage.INSTANCE);
    private static final List<RuleIElementType> RULE_TYPES = PSIElementTypeFactory.getRuleIElementTypes(ProtoLanguage.INSTANCE);

    public static final TokenIElementType ID = TOKEN_TYPES.get(ProtoLexer.IDENT);

    public static final TokenSet KEYWORDS = PSIElementTypeFactory.createTokenSet(ProtoLanguage.INSTANCE,
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

    // keywords also can be identifiers
    public static final TokenSet IDENTIFIER_TOKEN_SET = PSIElementTypeFactory.createTokenSet(ProtoLanguage.INSTANCE,
            ProtoLexer.IDENT,
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

    public static final TokenSet COMMENT_TOKEN_SET = PSIElementTypeFactory.createTokenSet(ProtoLanguage.INSTANCE,
            ProtoLexer.COMMENT,
            ProtoLexer.LINE_COMMENT
    );

    public static final TokenSet LITERAL_TOKEN_SET = PSIElementTypeFactory.createTokenSet(ProtoLanguage.INSTANCE,
            ProtoLexer.STRING_VALUE,
            ProtoLexer.FLOAT_VALUE,
            ProtoLexer.INTEGER_VALUE,
            ProtoLexer.IDENT,
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

    // tokens

    public static final TokenIElementType LCURLY = TOKEN_TYPES.get(ProtoLexer.LCURLY);
    public static final TokenIElementType RCURLY = TOKEN_TYPES.get(ProtoLexer.RCURLY);
    public static final TokenIElementType LPAREN = TOKEN_TYPES.get(ProtoLexer.LPAREN);
    public static final TokenIElementType RPAREN = TOKEN_TYPES.get(ProtoLexer.RPAREN);
    public static final TokenIElementType LSQUARE = TOKEN_TYPES.get(ProtoLexer.LSQUARE);
    public static final TokenIElementType RSQUARE = TOKEN_TYPES.get(ProtoLexer.RSQUARE);
    public static final TokenIElementType LT = TOKEN_TYPES.get(ProtoLexer.LT);
    public static final TokenIElementType GT = TOKEN_TYPES.get(ProtoLexer.GT);
    public static final TokenIElementType ASSIGN = TOKEN_TYPES.get(ProtoLexer.ASSIGN);

    // Rules
    public static final IElementType R_TYPE_REFERENCE = RULE_TYPES.get(ProtoParser.RULE_typeReference);
    public static final IElementType R_NAME = RULE_TYPES.get(ProtoParser.RULE_ident);
    public static final IElementType R_FIELD_MODIFIER = RULE_TYPES.get(ProtoParser.RULE_fieldModifier);
    public static final IElementType R_FIELD_NAME = RULE_TYPES.get(ProtoParser.RULE_fieldName);
    public static final IElementType R_TAG = RULE_TYPES.get(ProtoParser.RULE_tag);
    private static final IFileElementType FILE = new IFileElementType(ProtoLanguage.INSTANCE);
    private static final TokenSet COMMENTS = PSIElementTypeFactory.createTokenSet(ProtoLanguage.INSTANCE, COMMENT, LINE_COMMENT);
    public static final TokenSet WHITESPACE = PSIElementTypeFactory.createTokenSet(ProtoLanguage.INSTANCE, WS, NL);

    private static final TokenSet STRING = PSIElementTypeFactory.createTokenSet(ProtoLanguage.INSTANCE, STRING_VALUE);

    public static TokenIElementType token(int token) {
        return TOKEN_TYPES.get(token);
    }

    public static RuleIElementType rule(int rule) {
        return RULE_TYPES.get(rule);
    }

    private final Map<Integer, Class<? extends ANTLRPsiNode>> classByRuleIndex = new HashMap<Integer, Class<? extends ANTLRPsiNode>>();

    public ProtoParserDefinition() {
        register(ProtoParser.RULE_syntax, SyntaxNode.class);
        register(ProtoParser.RULE_packageStatement, PackageStatement.class);
        register(ProtoParser.RULE_importStatement, ImportNode.class);
        register(ProtoParser.RULE_fileReference, FileReferenceNode.class);
        register(ProtoParser.RULE_messageBlock, MessageNode.class);
        register(ProtoParser.RULE_messageName, MessageNameNode.class);
        register(ProtoParser.RULE_field, FieldNode.class);
        register(ProtoParser.RULE_typeReference, TypeReferenceNode.class);
        register(ProtoParser.RULE_groupBlock, GroupNode.class);
        register(ProtoParser.RULE_enumBlock, EnumNode.class);
        register(ProtoParser.RULE_enumField, EnumConstantNode.class);
        register(ProtoParser.RULE_serviceBlock, ServiceNode.class);
        register(ProtoParser.RULE_rpcMethod, RpcMethodNode.class);
        register(ProtoParser.RULE_optionEntry, OptionEntryNode.class);
        register(ProtoParser.RULE_option, OptionNode.class);
        register(ProtoParser.RULE_oneof, OneOfNode.class);
        register(ProtoParser.RULE_oneofField, OneofFieldNode.class);
        register(ProtoParser.RULE_extendBlock, ExtendNode.class);
        register(ProtoParser.RULE_extensions, ExtensionsNode.class);
        register(ProtoParser.RULE_map, MapNode.class);
        register(ProtoParser.RULE_mapKey, MapKeyNode.class);
        register(ProtoParser.RULE_optionValue, OptionValueNode.class);
        register(ProtoParser.RULE_range, RangeNode.class);
        register(ProtoParser.RULE_reservedFieldRanges, ReservedFieldRangesNode.class);
        register(ProtoParser.RULE_reservedFieldNames, ReservedFieldNamesNode.class);
        register(ProtoParser.RULE_rpcType, RpcMethodTypeNode.class);
        register(ProtoParser.RULE_proto, ProtoRootNode.class);
    }

    private void register(int rule, Class<? extends ANTLRPsiNode> resultNodeClass) {
        if (classByRuleIndex.containsKey(rule)) {
            throw new IllegalStateException("Duplicate rule");
        }
        classByRuleIndex.put(rule, resultNodeClass);
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
                throw new UnsupportedOperationException();
//                return ((ProtoParser) parser).name();
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
    public ANTLRPsiNode createElement(ASTNode node) {
        IElementType elType = node.getElementType();
        if (elType instanceof TokenIElementType) {
            return new ANTLRPsiNode(node);
        }
        if (!(elType instanceof RuleIElementType)) {
            return new ANTLRPsiNode(node);
        }
        RuleIElementType ruleElType = (RuleIElementType) elType;
        int ruleIndex = ruleElType.getRuleIndex();
        if (classByRuleIndex.containsKey(ruleIndex)) {
            Class<? extends ANTLRPsiNode> nodeClass = classByRuleIndex.get(ruleIndex);
            try {
                Constructor<? extends ANTLRPsiNode> constructor = nodeClass.getConstructor(ASTNode.class);
                return constructor.newInstance(node);
            } catch (Exception e) {
                throw Throwables.propagate(e);
            }
        }
        return new ANTLRPsiNode(node);
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
