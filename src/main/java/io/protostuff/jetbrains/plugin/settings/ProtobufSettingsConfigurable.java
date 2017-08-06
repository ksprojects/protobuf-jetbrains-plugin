package io.protostuff.jetbrains.plugin.settings;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import java.util.Objects;
import javax.swing.JComponent;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

public class ProtobufSettingsConfigurable implements Configurable {

    private final ProtobufSettings settings;
    private final Project project;

    @Nullable
    private SettingsForm settingsForm;


    public ProtobufSettingsConfigurable(Project project) {
        this.project = project;
        this.settings = ProtobufSettings.getInstance(project);
    }

    @Nls
    @Override
    public String getDisplayName() {
        return "Protobuf Support";
    }

    @Override
    public String getHelpTopic() {
        return null;
    }

    @Override
    public JComponent createComponent() {
        settingsForm = new SettingsForm(project, settings);
        return settingsForm.getPanel();
    }

    @Override
    public boolean isModified() {
        return settingsForm != null
                && !Objects.equals(settings, settingsForm.getSettings());
    }

    @Override
    public void apply() throws ConfigurationException {
        if (settingsForm != null && settings != null) {
            settings.copyFrom(settingsForm.getSettings());
        }
    }

    @Override
    public void reset() {
        if (settingsForm != null && settings != null) {
            settingsForm.reset(settings);
        }
    }

    @Override
    public void disposeUIResources() {
        settingsForm = null;
    }


}
