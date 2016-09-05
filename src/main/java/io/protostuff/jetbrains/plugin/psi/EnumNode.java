package io.protostuff.jetbrains.plugin.psi;

import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import io.protostuff.compiler.parser.ProtoParser;
import io.protostuff.jetbrains.plugin.Icons;
import io.protostuff.jetbrains.plugin.ProtoParserDefinition;
import io.protostuff.jetbrains.plugin.view.structure.ProtoItemPresentation;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public class EnumNode
        extends UserType {

    public static final String ALLOW_ALIAS = "allow_alias";
    public static final String TRUE = "true";

    public EnumNode(@NotNull ASTNode node) {
        super(node, ProtoParserDefinition.rule(ProtoParser.RULE_enumName));
    }

    @Override
    public ItemPresentation getPresentation() {
        String fullName = getFullName();
        return new ProtoItemPresentation(fullName, Icons.ENUM);
    }

    public List<EnumConstantNode> getConstants() {
        EnumConstantNode[] nodes = findChildrenByClass(EnumConstantNode.class);
        return Arrays.asList(nodes);
    }

    public boolean allowAlias() {
        OptionEntryNode[] options = findChildrenByClass(OptionEntryNode.class);
        for (OptionEntryNode optionEntry : options) {
            String name = optionEntry.getOptionNameText();
            String value = optionEntry.getOptionValueText();
            if (ALLOW_ALIAS.equals(name)
                    && TRUE.equals(value)) {
                return true;
            }
        }
        return false;
    }
}