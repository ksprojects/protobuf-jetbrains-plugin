package io.protostuff.jetbrains.plugin.formatter;

import com.intellij.formatting.Alignment;
import com.intellij.formatting.Block;
import com.intellij.formatting.Wrap;
import com.intellij.formatting.WrapType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static io.protostuff.compiler.parser.ProtoParser.*;
import static io.protostuff.jetbrains.plugin.ProtoParserDefinition.rule;
import static io.protostuff.jetbrains.plugin.ProtoParserDefinition.token;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public class BlockFactory {

    private static final Map<IElementType, Function<ASTNode, Block>> blockFactory = new HashMap<>();

    private static final Wrap NO_WRAP = Wrap.createWrap(WrapType.NONE, false);

    private static final Alignment ALIGNMENT = Alignment.createAlignment();

    static {
        register(rule(RULE_syntax), node -> new SyntaxBlock(node, NO_WRAP, ALIGNMENT));
        register(rule(RULE_packageStatement), node -> new PackageBlock(node, NO_WRAP, ALIGNMENT));
        register(rule(RULE_importStatement), node -> new ImportBlock(node, NO_WRAP, ALIGNMENT));
        register(rule(RULE_optionEntry), node -> new OptionBlock(node, NO_WRAP, ALIGNMENT));
        register(rule(RULE_messageBlock), node -> new MessageBlock(node, NO_WRAP, ALIGNMENT));
        register(rule(RULE_enumBlock), node -> new EnumBlock(node, NO_WRAP, ALIGNMENT));
        register(rule(RULE_serviceBlock), node -> new ServiceBlock(node, NO_WRAP, ALIGNMENT));
        register(token(LINE_COMMENT), node -> new LineCommentBlock(node, NO_WRAP, ALIGNMENT));
        register(token(COMMENT), node -> new CommentBlock(node, NO_WRAP, ALIGNMENT));
    }

    private static void register(IElementType elementType, Function<ASTNode, Block> factory) {
        blockFactory.put(elementType, factory);
    }


    public static Block createBlock(ASTNode node) {
        Function<ASTNode, Block> factory = blockFactory.get(node.getElementType());
        if (factory == null) {
            return createGenericBlock(node);
        }
        return factory.apply(node);
    }

    @NotNull
    private static GenericBlock createGenericBlock(ASTNode node) {
        return new GenericBlock(node, Wrap.createWrap(WrapType.NONE, false), Alignment.createAlignment());
    }

}
