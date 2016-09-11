package io.protostuff.jetbrains.plugin.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public interface KeywordsContainer extends PsiElement {

    /**
     * Returns a collection of keyword elements in this block.
     */
    @NotNull
    Collection<PsiElement> keywords();

}
