package io.protostuff.jetbrains.plugin.psi.presentation;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.util.PsiTreeUtil;
import io.protostuff.jetbrains.plugin.ProtostuffBundle;
import io.protostuff.jetbrains.plugin.psi.MessageNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public class PresentationUtil {

    @Nullable
    public static String getNameForElement(PsiElement element) {
        if (element instanceof PsiNamedElement) {
            PsiNamedElement message = (PsiNamedElement) element;
            String name = message.getName();
            String context = getContextName(message);
            if (context != null) {
                return ProtostuffBundle.message("element.context.display", name, context);
            } else {
                return name;
            }
        }
        return null;
    }

    private static String getContextName(@NotNull PsiElement element) {
        PsiElement parent = PsiTreeUtil.getParentOfType(element, MessageNode.class);
        if (parent == null) parent = element.getContainingFile();
        while (true) {
            if (parent == null) return null;
            if (parent instanceof PsiFile) return null;
            String name = getNameForElement(parent);
            if (name != null) return name;
            parent = parent.getParent();
        }
    }
}
