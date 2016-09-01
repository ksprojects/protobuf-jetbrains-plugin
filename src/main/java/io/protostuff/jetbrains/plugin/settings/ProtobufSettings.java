package io.protostuff.jetbrains.plugin.settings;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("WeakerAccess")
@State(name = "ProtobufSettings", storages = @Storage("tools.protobuf.xml"))
public class ProtobufSettings implements PersistentStateComponent<ProtobufSettings> {

    private List<String> includePaths = new ArrayList<>();

    public static ProtobufSettings getInstance(Project project) {
        return ServiceManager.getService(project, ProtobufSettings.class);
    }

    @NotNull
    public List<String> getIncludePaths() {
        return includePaths;
    }

    public void setIncludePaths(@NotNull List<String> includePaths) {
        this.includePaths = new ArrayList<>(includePaths);
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
        return Objects.equals(includePaths, that.includePaths);
    }

    @Override
    public int hashCode() {
        return Objects.hash(includePaths);
    }
}
