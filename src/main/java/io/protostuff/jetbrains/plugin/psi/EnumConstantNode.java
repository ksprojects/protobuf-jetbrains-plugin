package io.protostuff.jetbrains.plugin.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import io.protostuff.compiler.parser.ProtoParser;
import org.antlr.jetbrains.adapter.psi.IdentifierDefSubtree;
import org.antlr.jetbrains.adapter.psi.ScopeNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static io.protostuff.jetbrains.plugin.ProtoParserDefinition.rule;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public class EnumConstantNode
        extends IdentifierDefSubtree
        implements ScopeNode {

    public EnumConstantNode(@NotNull ASTNode node) {
        super(node, rule(ProtoParser.RULE_enumFieldName));
    }

    @Nullable
    @Override
    public PsiElement resolve(PsiNamedElement element) {
        return null;
    }

}