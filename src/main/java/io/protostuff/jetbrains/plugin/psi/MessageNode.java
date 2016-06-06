package io.protostuff.jetbrains.plugin.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.util.IncorrectOperationException;
import io.protostuff.compiler.parser.ProtoParser;
import io.protostuff.jetbrains.plugin.ProtoParserDefinition;
import org.antlr.jetbrains.adapter.psi.IdentifierDefSubtree;
import org.antlr.jetbrains.adapter.psi.ScopeNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
}