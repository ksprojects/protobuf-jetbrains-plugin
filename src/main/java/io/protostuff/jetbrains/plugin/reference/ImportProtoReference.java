package io.protostuff.jetbrains.plugin.reference;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFileSystemItem;
import com.intellij.psi.PsiReferenceBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A reference to an imported proto file.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public class ImportProtoReference extends PsiReferenceBase<PsiElement> {

    private final PsiFileSystemItem target;

    public ImportProtoReference(@NotNull PsiElement element, TextRange rangeInElement, PsiFileSystemItem target) {
        super(element, rangeInElement, false);
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
