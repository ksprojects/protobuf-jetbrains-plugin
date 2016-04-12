package io.protostuff.jetbrains.plugin.view.structure;

import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public interface PresentationFactory {

    ItemPresentation createPresentation(PsiElement element);

}
