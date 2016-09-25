package io.protostuff.jetbrains.plugin.psi;

import java.util.Collection;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public interface DataTypeContainer {

    /**
     * Returns string prefix that is common for all children full names.
     * For root container it is a dot if package is not set.
     */
    String getNamespace();

    Collection<DataType> getDeclaredDataTypes();

}
