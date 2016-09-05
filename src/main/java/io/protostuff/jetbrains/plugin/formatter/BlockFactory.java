package io.protostuff.jetbrains.plugin.formatter;

import com.intellij.formatting.Alignment;
import com.intellij.formatting.Block;
import com.intellij.formatting.Indent;
import com.intellij.lang.ASTNode;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static io.protostuff.compiler.parser.ProtoParser.*;
import static io.protostuff.jetbrains.plugin.ProtoParserDefinition.rule;

/**
 * @author Kostiantyn Shchepanovskyi
 */
class BlockFactory {

    static final Map<IElementType, Factory> registry = new HashMap<>();

    static {
        Factory FAIL_ROOT_NODE = (node, alignment, indent, settings) -> {
            throw new IllegalStateException("Root node cannot be handled here");
        };
        register(rule(RULE_proto), FAIL_ROOT_NODE);
        register(rule(RULE_packageName), LeafBlock::new);
        register(rule(RULE_rpcType), LeafBlock::new);
        register(rule(RULE_ident), LeafBlock::new);
        register(rule(RULE_mapKey), LeafBlock::new);
        register(rule(RULE_mapValue), LeafBlock::new);
        register(rule(RULE_tag), LeafBlock::new);
        register(rule(RULE_fieldName), LeafBlock::new);
        register(rule(RULE_textFormatOptionName), LeafBlock::new);
        register(rule(RULE_textFormatOptionValue), LeafBlock::new);
        register(rule(RULE_optionName), LeafBlock::new);
        register(rule(RULE_optionValue), LeafBlock::new);
        register(rule(RULE_fieldModifier), LeafBlock::new);
        register(rule(RULE_typeReference), LeafBlock::new);
        register(rule(RULE_range), StatementBlock::new);
        register(rule(RULE_reservedFieldNames), StatementBlock::new);
        register(rule(RULE_reservedFieldRanges), StatementBlock::new);
        register(rule(RULE_fieldOptions), StatementBlock::new);
        register(rule(RULE_map), StatementBlock::new);
        register(rule(RULE_syntax), StatementBlock::new);
        register(rule(RULE_packageStatement), StatementBlock::new);
        register(rule(RULE_importStatement), StatementBlock::new);
        register(rule(RULE_optionEntry), StatementBlock::new);
        register(rule(RULE_option), StatementBlock::new);
        register(rule(RULE_messageBlock), ParentBlock::new);
        register(rule(RULE_textFormat), ParentBlock::new);
        register(rule(RULE_textFormatEntry), ParentBlock::new);
        register(rule(RULE_field), StatementBlock::new);
        register(rule(RULE_enumBlock), ParentBlock::new);
        register(rule(RULE_enumField), StatementBlock::new);
        register(rule(RULE_serviceBlock), ParentBlock::new);
        register(rule(RULE_rpcMethod), ParentBlock::new);
        register(rule(RULE_extendBlock), ParentBlock::new);
        register(rule(RULE_extendBlockEntry), StatementBlock::new);
        register(rule(RULE_oneof), ParentBlock::new);
        register(rule(RULE_oneofField), StatementBlock::new);
        register(rule(RULE_oneofGroup), ParentBlock::new);
        register(rule(RULE_groupBlock), ParentBlock::new);
        register(rule(RULE_extensions), StatementBlock::new);

        register(rule(RULE_enumName), LeafBlock::new);
        register(rule(RULE_enumFieldName), LeafBlock::new);
        register(rule(RULE_enumFieldValue), LeafBlock::new);
        register(rule(RULE_serviceName), LeafBlock::new);
        register(rule(RULE_rpcName), LeafBlock::new);
        register(rule(RULE_messageName), LeafBlock::new);
        register(rule(RULE_oneofName), LeafBlock::new);
        register(rule(RULE_groupName), LeafBlock::new);
        register(rule(RULE_fullIdent), LeafBlock::new);
        register(rule(RULE_fileReference), LeafBlock::new);
        register(rule(RULE_rangeFrom), LeafBlock::new);
        register(rule(RULE_rangeTo), LeafBlock::new);
        register(rule(RULE_reservedFieldName), LeafBlock::new);
    }

    private static void register(IElementType elementType, Factory factory) {
        if (registry.containsKey(elementType)) {
            throw new IllegalStateException("Already registered: " + elementType);
        }
        registry.put(elementType, factory);
    }

    static Block createBlock(ASTNode node, Alignment alignment, Indent indent, CodeStyleSettings settings) {
        Factory factory = registry.get(node.getElementType());
        if (factory == null) {
            // If element type is unknown it is best to keep existing formatting
            return createLeaf(node, alignment, indent, settings);
        }
        return factory.create(node, alignment, indent, settings);
    }

    @NotNull
    private static LeafBlock createLeaf(ASTNode node, Alignment alignment, Indent indent, CodeStyleSettings settings) {
        return new LeafBlock(node, alignment, indent, settings);
    }

    interface Factory {
        Block create(ASTNode node, Alignment alignment, Indent indent, CodeStyleSettings settings);
    }

}
