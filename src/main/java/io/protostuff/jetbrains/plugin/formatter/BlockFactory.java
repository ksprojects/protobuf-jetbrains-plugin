package io.protostuff.jetbrains.plugin.formatter;

import static io.protostuff.compiler.parser.ProtoParser.RULE_customFieldReference;
import static io.protostuff.compiler.parser.ProtoParser.RULE_enumBlock;
import static io.protostuff.compiler.parser.ProtoParser.RULE_enumField;
import static io.protostuff.compiler.parser.ProtoParser.RULE_enumFieldName;
import static io.protostuff.compiler.parser.ProtoParser.RULE_enumFieldValue;
import static io.protostuff.compiler.parser.ProtoParser.RULE_enumName;
import static io.protostuff.compiler.parser.ProtoParser.RULE_extendBlock;
import static io.protostuff.compiler.parser.ProtoParser.RULE_extendBlockEntry;
import static io.protostuff.compiler.parser.ProtoParser.RULE_extensions;
import static io.protostuff.compiler.parser.ProtoParser.RULE_field;
import static io.protostuff.compiler.parser.ProtoParser.RULE_fieldModifier;
import static io.protostuff.compiler.parser.ProtoParser.RULE_fieldName;
import static io.protostuff.compiler.parser.ProtoParser.RULE_fieldOptions;
import static io.protostuff.compiler.parser.ProtoParser.RULE_fieldRerefence;
import static io.protostuff.compiler.parser.ProtoParser.RULE_fileReference;
import static io.protostuff.compiler.parser.ProtoParser.RULE_fullIdent;
import static io.protostuff.compiler.parser.ProtoParser.RULE_groupBlock;
import static io.protostuff.compiler.parser.ProtoParser.RULE_groupName;
import static io.protostuff.compiler.parser.ProtoParser.RULE_ident;
import static io.protostuff.compiler.parser.ProtoParser.RULE_importStatement;
import static io.protostuff.compiler.parser.ProtoParser.RULE_map;
import static io.protostuff.compiler.parser.ProtoParser.RULE_mapKey;
import static io.protostuff.compiler.parser.ProtoParser.RULE_mapValue;
import static io.protostuff.compiler.parser.ProtoParser.RULE_messageBlock;
import static io.protostuff.compiler.parser.ProtoParser.RULE_messageName;
import static io.protostuff.compiler.parser.ProtoParser.RULE_oneof;
import static io.protostuff.compiler.parser.ProtoParser.RULE_oneofField;
import static io.protostuff.compiler.parser.ProtoParser.RULE_oneofGroup;
import static io.protostuff.compiler.parser.ProtoParser.RULE_oneofName;
import static io.protostuff.compiler.parser.ProtoParser.RULE_option;
import static io.protostuff.compiler.parser.ProtoParser.RULE_optionEntry;
import static io.protostuff.compiler.parser.ProtoParser.RULE_optionValue;
import static io.protostuff.compiler.parser.ProtoParser.RULE_packageName;
import static io.protostuff.compiler.parser.ProtoParser.RULE_packageStatement;
import static io.protostuff.compiler.parser.ProtoParser.RULE_proto;
import static io.protostuff.compiler.parser.ProtoParser.RULE_range;
import static io.protostuff.compiler.parser.ProtoParser.RULE_rangeFrom;
import static io.protostuff.compiler.parser.ProtoParser.RULE_rangeTo;
import static io.protostuff.compiler.parser.ProtoParser.RULE_reservedFieldName;
import static io.protostuff.compiler.parser.ProtoParser.RULE_reservedFieldNames;
import static io.protostuff.compiler.parser.ProtoParser.RULE_reservedFieldRanges;
import static io.protostuff.compiler.parser.ProtoParser.RULE_rpcMethod;
import static io.protostuff.compiler.parser.ProtoParser.RULE_rpcName;
import static io.protostuff.compiler.parser.ProtoParser.RULE_rpcType;
import static io.protostuff.compiler.parser.ProtoParser.RULE_serviceBlock;
import static io.protostuff.compiler.parser.ProtoParser.RULE_serviceName;
import static io.protostuff.compiler.parser.ProtoParser.RULE_standardFieldRerefence;
import static io.protostuff.compiler.parser.ProtoParser.RULE_syntaxName;
import static io.protostuff.compiler.parser.ProtoParser.RULE_syntaxStatement;
import static io.protostuff.compiler.parser.ProtoParser.RULE_tag;
import static io.protostuff.compiler.parser.ProtoParser.RULE_textFormat;
import static io.protostuff.compiler.parser.ProtoParser.RULE_textFormatEntry;
import static io.protostuff.compiler.parser.ProtoParser.RULE_textFormatOptionName;
import static io.protostuff.compiler.parser.ProtoParser.RULE_textFormatOptionValue;
import static io.protostuff.compiler.parser.ProtoParser.RULE_typeReference;
import static io.protostuff.jetbrains.plugin.ProtoParserDefinition.rule;

import com.intellij.formatting.Alignment;
import com.intellij.formatting.Block;
import com.intellij.formatting.Indent;
import com.intellij.lang.ASTNode;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.tree.IElementType;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

/**
 * Formatter's block factory.
 *
 * @author Kostiantyn Shchepanovskyi
 */
class BlockFactory {

    static final Map<IElementType, Factory> REGISTRY = new HashMap<>();

    private static final Factory FAIL_ROOT_NODE = (node, alignment, indent, settings) -> {
        throw new IllegalStateException("Root node cannot be handled here");
    };

    static {
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
        register(rule(RULE_fieldRerefence), LeafBlock::new);
        register(rule(RULE_standardFieldRerefence), LeafBlock::new);
        register(rule(RULE_customFieldReference), LeafBlock::new);
        register(rule(RULE_optionValue), LeafBlock::new);
        register(rule(RULE_fieldModifier), LeafBlock::new);
        register(rule(RULE_typeReference), LeafBlock::new);
        register(rule(RULE_range), StatementBlock::new);
        register(rule(RULE_reservedFieldNames), StatementBlock::new);
        register(rule(RULE_reservedFieldRanges), StatementBlock::new);
        register(rule(RULE_fieldOptions), StatementBlock::new);
        register(rule(RULE_map), StatementBlock::new);
        register(rule(RULE_syntaxStatement), StatementBlock::new);
        register(rule(RULE_syntaxName), LeafBlock::new);
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
        if (REGISTRY.containsKey(elementType)) {
            throw new IllegalStateException("Already registered: " + elementType);
        }
        REGISTRY.put(elementType, factory);
    }

    static Block createBlock(ASTNode node, Alignment alignment, Indent indent, CodeStyleSettings settings) {
        Factory factory = REGISTRY.get(node.getElementType());
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
