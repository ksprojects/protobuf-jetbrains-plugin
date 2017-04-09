package io.protostuff.jetbrains.plugin.reference;

import com.intellij.psi.PsiReference;
import io.protostuff.jetbrains.plugin.psi.FieldReferenceNode;
import org.jetbrains.annotations.NotNull;

/**
 * Field reference provider used for resolving option references.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public interface FieldReferenceProvider {

    /**
     * Returns an array of field references for given element and option text.
     */
    @NotNull
    PsiReference[] getReferencesByElement(FieldReferenceNode fieldReferenceNode);
}
