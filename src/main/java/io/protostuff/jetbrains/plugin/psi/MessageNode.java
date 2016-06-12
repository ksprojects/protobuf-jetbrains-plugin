package io.protostuff.jetbrains.plugin.psi;

import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import io.protostuff.compiler.parser.ProtoParser;
import io.protostuff.jetbrains.plugin.Icons;
import io.protostuff.jetbrains.plugin.ProtoParserDefinition;
import io.protostuff.jetbrains.plugin.view.structure.ProtoItemPresentation;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public class MessageNode extends UserType implements UserTypeContainer {


    public MessageNode(@NotNull ASTNode node) {
        super(node, ProtoParserDefinition.rule(ProtoParser.RULE_messageName));
    }

    /**
     * Returns message name.
     */
    @Override
    public String getName() {
        return super.getName();
    }


    @Override
    public String getNamespace() {
        return getQualifiedName() + ".";
    }

    @Override
    public Collection<UserType> getChildrenTypes() {
        return Arrays.asList(findChildrenByClass(UserType.class));
    }

    @Override
    public ItemPresentation getPresentation() {
        String fullName = getFullName();
        return new ProtoItemPresentation(fullName, Icons.MESSAGE);
    }

}