package io.protostuff.jetbrains.plugin.reference;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceBase;
import io.protostuff.jetbrains.plugin.psi.ProtoType;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Reference to a user type - message or enum.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public class TypeReference extends PsiReferenceBase<PsiElement> {

    private final PsiElement target;

    public TypeReference(PsiElement element, TextRange textRange, PsiElement target) {
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

    @Override
    public boolean isReferenceTo(@NotNull PsiElement element) {
        if (target instanceof ProtoType) {
            ProtoType targetType = (ProtoType) target;
            if (element instanceof ProtoType) {
                ProtoType elementType = (ProtoType) element;
                return Objects.equals(targetType.getFullName(), elementType.getFullName());
            }
        }
        return super.isReferenceTo(element);
    }
}
