package io.protostuff.jetbrains.plugin.psi.presentation;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.util.PsiTreeUtil;
import io.protostuff.jetbrains.plugin.ProtostuffBundle;
import io.protostuff.jetbrains.plugin.psi.DataType;
import io.protostuff.jetbrains.plugin.psi.DataTypeContainer;
import io.protostuff.jetbrains.plugin.psi.MessageField;
import io.protostuff.jetbrains.plugin.psi.ProtoRootNode;
import org.jetbrains.annotations.Nullable;

/**
 * Utility functions for working with item presentation.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public class PresentationUtil {

    /**
     * Returns presentation name for given element.
     */
    @Nullable
    public static String getNameForElement(PsiElement element) {
        if (element instanceof DataType) {
            DataType type = (DataType) element;
            return type.getFullName();
        }
        if (element instanceof ProtoRootNode) {
            ProtoRootNode rootNode = (ProtoRootNode) element;
            String packageName = rootNode.getPackageName();
            if (packageName.isEmpty()) {
                return null;
            }
            return packageName;
        }
        if (element instanceof MessageField) {
            MessageField field = (MessageField) element;
            String fieldName = field.getFieldName();
            DataTypeContainer container = PsiTreeUtil.getParentOfType(element, DataTypeContainer.class);
            String conteinerName = getNameForElement(container);
            if (conteinerName != null) {
                return ProtostuffBundle.message("element.context.display", fieldName, conteinerName);
            } else {
                return fieldName;
            }
        }
        if (element instanceof PsiNamedElement) {
            PsiNamedElement namedElement = (PsiNamedElement) element;
            return namedElement.getName();
        }
        return null;
    }

}
