package io.protostuff.jetbrains.plugin.psi;

import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import io.protostuff.compiler.parser.ProtoParser;
import io.protostuff.jetbrains.plugin.Icons;
import io.protostuff.jetbrains.plugin.ProtoParserDefinition;
import io.protostuff.jetbrains.plugin.view.structure.ProtoItemPresentation;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public class EnumNode
        extends UserType {

    public EnumNode(@NotNull ASTNode node) {
        super(node, ProtoParserDefinition.rule(ProtoParser.RULE_enumName));
    }

    @Override
    public ItemPresentation getPresentation() {
        String fullName = getFullName();
        return new ProtoItemPresentation(fullName, Icons.ENUM);
    }

}