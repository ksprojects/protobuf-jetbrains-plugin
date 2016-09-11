package io.protostuff.jetbrains.plugin.settings;

import com.google.common.base.Objects;
import com.intellij.openapi.components.*;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess")
@State(
        name = "ProtobufSettings",
        storages = @Storage(
                id = "dir",
                file = StoragePathMacros.PROJECT_CONFIG_DIR + "/protobuf.xml",
                scheme = StorageScheme.DIRECTORY_BASED)
)
public class ProtobufSettings implements PersistentStateComponent<ProtobufSettings> {

    private List<String> includePaths = new ArrayList<String>();

    public static ProtobufSettings getInstance(Project project) {
        return ServiceManager.getService(project, ProtobufSettings.class);
    }

    @NotNull
    public List<String> getIncludePaths() {
        return includePaths;
    }

    public void setIncludePaths(@NotNull List<String> includePaths) {
        this.includePaths = new ArrayList<String>(includePaths);
    }

    @Override
    public ProtobufSettings getState() {
        return this;
    }

    @Override
    public void loadState(ProtobufSettings state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    public void copyFrom(ProtobufSettings settings) {
        setIncludePaths(settings.getIncludePaths());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProtobufSettings)) return false;
        ProtobufSettings that = (ProtobufSettings) o;
        return Objects.equal(includePaths, that.includePaths);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(includePaths);
    }
}
