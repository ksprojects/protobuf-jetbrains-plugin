package io.protostuff.jetbrains.plugin.psi.indices;

import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.openapi.project.Project;
import com.intellij.psi.stubs.StringStubIndexExtension;
import com.intellij.psi.stubs.StubIndexKey;
import io.protostuff.jetbrains.plugin.psi.DataType;
import java.util.ArrayList;
import java.util.Collection;
import org.jetbrains.annotations.NotNull;

public class DataTypeIndex extends StringStubIndexExtension<DataType> {

    public static final StubIndexKey<String, DataType> KEY =
            StubIndexKey.createIndexKey("DataType");
    public static final DataTypeIndex INSTANCE = new DataTypeIndex();

    @NotNull
    @Override
    public StubIndexKey<String, DataType> getKey() {
        return KEY;
    }

    @NotNull
    @Override
    public Collection<String> getAllKeys(Project project) {
        try {
            return super.getAllKeys(project);
        } catch (ProcessCanceledException e) {
            return new ArrayList<>();
        }
    }
}
