package io.protostuff.jetbrains.plugin.reference;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Option reference - referencing a field (or multiple fields) for
 * complex custom options.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public class OptionReference extends PsiReferenceBase<PsiElement> {

    private final PsiElement target;

    public OptionReference(PsiElement element, TextRange textRange, PsiElement target) {
        super(element, textRange, false);
        this.target = target;
    }

    @Nullable
    @Override
    public PsiElement resolve() {
        return target;
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        return new Object[0];
    }
}
