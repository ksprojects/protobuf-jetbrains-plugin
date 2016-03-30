package io.protostuff.jetbrains.plugin;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class ProtoColorSettingsPage implements ColorSettingsPage {
    private static final AttributesDescriptor[] DESCRIPTORS = new AttributesDescriptor[]{
            new AttributesDescriptor("Keyword", ProtoSyntaxHighlighter.KEYWORD),
            new AttributesDescriptor("String", ProtoSyntaxHighlighter.STRING),
            new AttributesDescriptor("Number", ProtoSyntaxHighlighter.NUMBER),
            new AttributesDescriptor("Line comment", ProtoSyntaxHighlighter.LINE_COMMENT),
            new AttributesDescriptor("Block comment", ProtoSyntaxHighlighter.BLOCK_COMMENT),
            new AttributesDescriptor("Enum constant", ProtoSyntaxHighlighter.ENUM_CONSTANT)
    };

    private Map<String, TextAttributesKey> additionalTags;

    public ProtoColorSettingsPage() {
        additionalTags = new HashMap<>();
        additionalTags.put("keyword", ProtoSyntaxHighlighter.KEYWORD);
        additionalTags.put("constant", ProtoSyntaxHighlighter.ENUM_CONSTANT);

    }

    @Nullable
    @Override
    public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
        return additionalTags;
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return Icons.PROTO_ICON;
    }

    @NotNull
    @Override
    public SyntaxHighlighter getHighlighter() {
        return new ProtoSyntaxHighlighter();
    }

    @NotNull
    @Override
    public String getDemoText() {
        return
                "/* block comment */\n" +
                        "<keyword>message</keyword> Foo {\n" +
                        "   // line comment\n" +
                        "   <keyword>optional</keyword> <keyword>int32</keyword> x = 1;\n\n" +
                        "   <keyword>enum</keyword> Bar {\n" +
                        "       <constant>CONSTANT</constant> = 0 [baz = \"daf\"];\n" +
                        "   }\n" +
                        "}\n";
    }

    @NotNull
    @Override
    public AttributesDescriptor[] getAttributeDescriptors() {
        return DESCRIPTORS;
    }

    @NotNull
    @Override
    public ColorDescriptor[] getColorDescriptors() {
        return ColorDescriptor.EMPTY_ARRAY;
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return "Protobuf";
    }
}
