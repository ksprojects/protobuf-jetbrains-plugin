package io.protostuff.jetbrains.plugin.psi;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiReference;
import io.protostuff.jetbrains.plugin.reference.TypeReference;
import org.antlr.jetbrains.adapter.psi.AntlrPsiNode;
import org.jetbrains.annotations.NotNull;

/**
 * Type reference node.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public class TypeReferenceNode extends AntlrPsiNode implements KeywordsContainer {

    public TypeReferenceNode(@NotNull ASTNode node) {
        super(node);
    }

    @NotNull
    @Override
    public PsiReference getReference() {
        return new TypeReference(this, TextRange.create(0, getNode().getTextLength()));
    }
}