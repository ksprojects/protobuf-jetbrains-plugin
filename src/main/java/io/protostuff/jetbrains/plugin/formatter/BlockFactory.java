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

    static final Map<IElementType, Factory> registry = new HashMap<IElementType, Factory>();

    static {
        Factory FAIL_ROOT_NODE = new Factory() {
            @Override
            public Block create(ASTNode node, Alignment alignment, Indent indent, CodeStyleSettings settings) {
                throw new IllegalStateException("Root node cannot be handled here");
            }
        };
        register(rule(RULE_proto), FAIL_ROOT_NODE);
        Factory leafBlockFactory = new Factory() {
            @Override
            public Block create(ASTNode node, Alignment alignment, Indent indent, CodeStyleSettings settings) {
                return new LeafBlock(node, alignment, indent, settings);
            }
        };
        Factory parentBlockFactory = new Factory() {
            @Override
            public Block create(ASTNode node, Alignment alignment, Indent indent, CodeStyleSettings settings) {
                return new ParentBlock(node, alignment, indent, settings);
            }
        };
        Factory statementBlockFactory = new Factory() {
            @Override
            public Block create(ASTNode node, Alignment alignment, Indent indent, CodeStyleSettings settings) {
                return new StatementBlock(node, alignment, indent, settings);
            }
        };
        register(rule(RULE_packageName), leafBlockFactory);
        register(rule(RULE_rpcType), leafBlockFactory);
        register(rule(RULE_ident), leafBlockFactory);
        register(rule(RULE_mapKey), leafBlockFactory);
        register(rule(RULE_mapValue), leafBlockFactory);
        register(rule(RULE_tag), leafBlockFactory);
        register(rule(RULE_fieldName), leafBlockFactory);
        register(rule(RULE_textFormatOptionName), leafBlockFactory);
        register(rule(RULE_textFormatOptionValue), leafBlockFactory);
        register(rule(RULE_optionName), leafBlockFactory);
        register(rule(RULE_optionValue), leafBlockFactory);
        register(rule(RULE_fieldModifier), leafBlockFactory);
        register(rule(RULE_typeReference), leafBlockFactory);
        register(rule(RULE_range), statementBlockFactory);
        register(rule(RULE_reservedFieldNames), statementBlockFactory);
        register(rule(RULE_reservedFieldRanges), statementBlockFactory);
        register(rule(RULE_fieldOptions), statementBlockFactory);
        register(rule(RULE_map), statementBlockFactory);
        register(rule(RULE_syntax), statementBlockFactory);
        register(rule(RULE_packageStatement), statementBlockFactory);
        register(rule(RULE_importStatement), statementBlockFactory);
        register(rule(RULE_optionEntry), statementBlockFactory);
        register(rule(RULE_option), statementBlockFactory);
        register(rule(RULE_messageBlock), parentBlockFactory);
        register(rule(RULE_textFormat), parentBlockFactory);
        register(rule(RULE_textFormatEntry), parentBlockFactory);
        register(rule(RULE_field), statementBlockFactory);
        register(rule(RULE_enumBlock), parentBlockFactory);
        register(rule(RULE_enumField), statementBlockFactory);
        register(rule(RULE_serviceBlock), parentBlockFactory);
        register(rule(RULE_rpcMethod), parentBlockFactory);
        register(rule(RULE_extendBlock), parentBlockFactory);
        register(rule(RULE_extendBlockEntry), statementBlockFactory);
        register(rule(RULE_oneof), parentBlockFactory);
        register(rule(RULE_oneofField), statementBlockFactory);
        register(rule(RULE_oneofGroup), parentBlockFactory);
        register(rule(RULE_groupBlock), parentBlockFactory);
        register(rule(RULE_extensions), statementBlockFactory);

        register(rule(RULE_enumName), leafBlockFactory);
        register(rule(RULE_enumFieldName), leafBlockFactory);
        register(rule(RULE_enumFieldValue), leafBlockFactory);
        register(rule(RULE_serviceName), leafBlockFactory);
        register(rule(RULE_rpcName), leafBlockFactory);
        register(rule(RULE_messageName), leafBlockFactory);
        register(rule(RULE_oneofName), leafBlockFactory);
        register(rule(RULE_groupName), leafBlockFactory);
        register(rule(RULE_fullIdent), leafBlockFactory);
        register(rule(RULE_fileReference), leafBlockFactory);
        register(rule(RULE_rangeFrom), leafBlockFactory);
        register(rule(RULE_rangeTo), leafBlockFactory);
        register(rule(RULE_reservedFieldName), leafBlockFactory);
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
