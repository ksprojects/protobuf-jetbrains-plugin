package io.protostuff.jetbrains.plugin.psi;

import com.intellij.psi.PsiElement;
import java.util.Collection;

/**
 * Container node that can hold user types - can be a message or proto file.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public interface DataTypeContainer extends PsiElement {

    /**
     * Returns string prefix that is common for all children full names.
     * For root container it is a dot if package is not set.
     */
    String getNamespace();

    Collection<DataType> getDeclaredDataTypes();

    Collection<ExtendNode> getDeclaredExtensions();

}
