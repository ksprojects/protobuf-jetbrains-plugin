package io.protostuff.jetbrains.plugin.formatter;

import com.intellij.formatting.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static io.protostuff.compiler.parser.ProtoParser.*;
import static io.protostuff.jetbrains.plugin.ProtoParserDefinition.rule;
import static io.protostuff.jetbrains.plugin.ProtoParserDefinition.token;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public class BlockFactory {

    private static final Map<IElementType, Factory> registry = new HashMap<>();
    private static final Wrap NO_WRAP = Wrap.createWrap(WrapType.NONE, false);
    private static final Alignment ALIGNMENT = Alignment.createAlignment();

    static {
        register(rule(RULE_syntax), SyntaxBlock::new);
        register(rule(RULE_packageStatement), PackageBlock::new);
        register(rule(RULE_importStatement), ImportBlock::new);
        register(rule(RULE_optionEntry), OptionBlock::new);
        register(rule(RULE_messageBlock), MessageBlock::new);
        register(rule(RULE_enumBlock), EnumBlock::new);
        register(rule(RULE_serviceBlock), ServiceBlock::new);
        register(token(LINE_COMMENT), LineCommentBlock::new);
        register(token(COMMENT), CommentBlock::new);
    }

    private static void register(IElementType elementType, Factory factory) {
        registry.put(elementType, factory);
    }

    static Block createBlock(ASTNode node, Alignment alignment) {
        Factory factory = registry.get(node.getElementType());
        if (factory == null) {
            return createGenericBlock(node, alignment);
        }
        return factory.create(node, alignment, Indent.getNoneIndent());
    }

    @NotNull
    private static GenericBlock createGenericBlock(ASTNode node, Alignment alignment) {
        return new GenericBlock(node, alignment, Indent.getNoneIndent());
    }

    private interface Factory {
        Block create(ASTNode node, Alignment alignment, Indent indent);
    }

}
