package io.protostuff.jetbrains.plugin.psi;

import com.intellij.navigation.NavigationItem;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

/**
 * User-defined proto type: message, enum or service.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public interface ProtoType extends PsiElement, NavigationItem {

    /**
     * Returns short name of the message/enum/service.
     */
    @NotNull
    String getName();

    /**
     * Returns full class name: [package.] + name (without leading dot).
     */
    @NotNull
    String getFullName();
}
