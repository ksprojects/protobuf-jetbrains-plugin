package io.protostuff.jetbrains.plugin;

import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManager;
import com.intellij.ide.plugins.cl.PluginClassLoader;
import com.intellij.lang.Language;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.SystemInfo;
import org.jetbrains.annotations.NotNull;

import javax.swing.event.HyperlinkEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ProtostuffPluginController implements ProjectComponent {
    private static final String PLUGIN_ID = "io.protostuff.protostuff-jetbrains-plugin";

    private static final Logger LOGGER = Logger.getInstance(ProtostuffPluginController.class);
    public static final String PLUGIN_NAME = "Protobuf Support";

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
        try {
            tryDisableConflictingPlugins();
        } catch (Exception e) {
            LOGGER.error("Could not detect or disable conflicting plugins", e);
        }
    }

    private void tryDisableConflictingPlugins() {
        List<IdeaPluginDescriptor> conflictingPlugins = new ArrayList<>();
        Collection<Language> languages = Language.getRegisteredLanguages();
        for (Language language : languages) {
            LanguageFileType associatedFileType = language.getAssociatedFileType();
            if (associatedFileType != null && associatedFileType != ProtoFileType.INSTANCE) {
                String extension = associatedFileType.getDefaultExtension();
                if (ProtoFileType.FILE_EXTENSION.equals(extension)) {
                    // TODO: How to match language to plugin? Is there a better way?
                    // https://github.com/protostuff/protobuf-jetbrains-plugin/issues/8
                    Class<? extends LanguageFileType> clazz = associatedFileType.getClass();
                    PluginClassLoader classLoader = (PluginClassLoader) clazz.getClassLoader();
                    PluginId pluginId = classLoader.getPluginId();
                    IdeaPluginDescriptor conflict = PluginManager.getPlugin(pluginId);
                    if (conflict != null && conflict.isEnabled()) {
                        LOGGER.warn("Detected conflicting plugin for *.proto files: " + conflict.getName() + " (" + pluginId + ")");
                        conflictingPlugins.add(conflict);
                    }
                }
            }
        }
        if (!conflictingPlugins.isEmpty()) {
            String conflictList = String.join("\n", conflictingPlugins.stream()
                    .map(IdeaPluginDescriptor::getName)
                    .map(name -> "<li>" + name + "</li>")
                    .collect(Collectors.toList()));
            final String text = ProtostuffBundle.message("action.disable.conflicting.plugins", conflictList);
            NotificationGroup ng = NotificationGroup.balloonGroup("Conflicting Plugins");
            ng.createNotification(PLUGIN_NAME, text, NotificationType.WARNING,
                    (notification, event) -> {
                        if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                            for (IdeaPluginDescriptor foreignPlugin : conflictingPlugins) {
                                PluginManager.disablePlugin(foreignPlugin.getPluginId().toString());
                            }
                            Application application = ApplicationManager.getApplication();
                            application.restart();
                        }
                    }).notify(project);
        }
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
