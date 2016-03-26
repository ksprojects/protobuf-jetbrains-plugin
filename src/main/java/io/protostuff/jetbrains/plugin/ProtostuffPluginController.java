package io.protostuff.jetbrains.plugin;

import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManager;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.SystemInfo;
import org.jetbrains.annotations.NotNull;

public class ProtostuffPluginController implements ProjectComponent {
    private static final String PLUGIN_ID = "io.protostuff.jetbrains.plugin";

    private static final Logger LOGGER = Logger.getInstance(ProtostuffPluginController.class);

    private Project project;

    public ProtostuffPluginController(Project project) {
        this.project = project;
    }

    @Override
    public void projectClosed() {
        LOGGER.info("projectClosed " + project.getName());
        project = null;
    }

    @Override
    public void projectOpened() {
        LOGGER.info("projectOpened " + project.getName());
        IdeaPluginDescriptor plugin = PluginManager.getPlugin(PluginId.getId(PLUGIN_ID));
        String version = "unknown";
        if (plugin != null) {
            version = plugin.getVersion();
        }
        LOGGER.info("Protostuff Plugin version " + version + ", Java version " + SystemInfo.JAVA_VERSION);
    }

    @Override
    public void initComponent() {
    }

    @Override
    public void disposeComponent() {
    }

    @NotNull
    @Override
    public String getComponentName() {
        return "ProtostuffPluginController";
    }
}
