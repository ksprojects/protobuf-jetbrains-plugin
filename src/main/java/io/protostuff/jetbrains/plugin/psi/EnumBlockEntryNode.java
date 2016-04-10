package io.protostuff.jetbrains.plugin.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import io.protostuff.jetbrains.plugin.ProtoParserDefinition;
import org.antlr.jetbrains.adapter.psi.IdentifierDefSubtree;
import org.antlr.jetbrains.adapter.psi.ScopeNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public class EnumBlockEntryNode
        extends IdentifierDefSubtree
        implements ScopeNode, KeywordsContainer {

    public EnumBlockEntryNode(@NotNull ASTNode node) {
        super(node, ProtoParserDefinition.ID);
    }

    @Nullable
    @Override
    public PsiElement resolve(PsiNamedElement element) {
        return null;
    }

}