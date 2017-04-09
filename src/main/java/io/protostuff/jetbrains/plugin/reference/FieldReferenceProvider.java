package io.protostuff.jetbrains.plugin.reference;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;

/**
 * Field reference provider used for resolving option references.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public interface FieldReferenceProvider {

    /**
     * Returns an array of field references for given element and option text.
     */
    PsiReference[] getReferencesByElement(PsiElement element, String text);
}
