package io.protostuff.jetbrains.plugin.psi;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiReference;
import io.protostuff.jetbrains.plugin.reference.TypeReference;
import io.protostuff.jetbrains.plugin.reference.TypeReferenceProvider;
import org.antlr.jetbrains.adapter.psi.AntlrPsiNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Type reference node.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public class TypeReferenceNode extends AntlrPsiNode implements KeywordsContainer {

    public TypeReferenceNode(@NotNull ASTNode node) {
        super(node);
    }

    @Nullable
    @Override
    public PsiReference getReference() {
        PsiReference[] references = getReferences();
        if (references.length > 0) {
            return references[0];
        }
        return new TypeReference(this, TextRange.create(0, getTextLength()), null);
    }

    @NotNull
    @Override
    public PsiReference[] getReferences() {
        TypeReferenceProvider referenceProvider = getProject().getComponent(TypeReferenceProvider.class);
        PsiReference[] references = referenceProvider.getReferencesByElement(this);
        return references;
    }

}