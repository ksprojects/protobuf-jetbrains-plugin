package io.protostuff.jetbrains.plugin.psi;

import com.intellij.psi.PsiElement;

import java.util.Collection;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public interface KeywordsContainer extends PsiElement {

    /**
     * Returns a collection of keyword elements in this block.
     */
    default Collection<PsiElement> keywords() {
        return Util.findKeywords(getNode());
    }

}
