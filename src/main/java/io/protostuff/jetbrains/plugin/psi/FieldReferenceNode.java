package io.protostuff.jetbrains.plugin.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiReference;
import io.protostuff.jetbrains.plugin.reference.FieldReferenceProvider;
import org.antlr.jetbrains.adapter.psi.AntlrPsiNode;
import org.jetbrains.annotations.NotNull;

/**
 * Option name node.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public class FieldReferenceNode extends AntlrPsiNode implements KeywordsContainer {

    public FieldReferenceNode(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public PsiReference getReference() {
        PsiReference[] references = getReferences();
        if (references.length > 0) {
            return references[0];
        }
        return null;
    }

    @NotNull
    @Override
    public PsiReference[] getReferences() {
        FieldReferenceProvider referenceProvider = getProject().getComponent(FieldReferenceProvider.class);
        return referenceProvider.getReferencesByElement(this, getText());
    }
}
