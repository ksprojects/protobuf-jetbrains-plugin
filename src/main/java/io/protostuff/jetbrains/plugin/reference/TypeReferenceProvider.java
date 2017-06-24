package io.protostuff.jetbrains.plugin.reference;

import com.intellij.psi.PsiReference;
import io.protostuff.jetbrains.plugin.psi.TypeReferenceNode;
import org.jetbrains.annotations.NotNull;

/**
 * Type reference provider used for resolving field type references.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public interface TypeReferenceProvider {

    /**
     * Returns an array of type references for given element.
     */
    @NotNull
    PsiReference[] getReferencesByElement(TypeReferenceNode typeReferenceNode);
}
