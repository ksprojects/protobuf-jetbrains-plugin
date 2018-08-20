package io.protostuff.jetbrains.plugin;

import static com.intellij.openapi.fileTypes.FileTypeFactory.FILE_TYPE_FACTORY_EP;

import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManager;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.extensions.ExtensionPoint;
import com.intellij.openapi.extensions.Extensions;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.fileTypes.ExtensionFileNameMatcher;
import com.intellij.openapi.fileTypes.FileNameMatcher;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeConsumer;
import com.intellij.openapi.fileTypes.FileTypeFactory;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.roots.libraries.Library;
import com.intellij.openapi.roots.libraries.LibraryTable;
import com.intellij.openapi.vfs.VirtualFile;
import io.protostuff.jetbrains.plugin.reference.file.BundledProtobufRootsProvider;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringTokenizer;
import java.util.stream.Collectors;
import javax.swing.event.HyperlinkEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ProtostuffPluginController implements ProjectComponent {

    public static final String PLUGIN_ID = "io.protostuff.protostuff-jetbrains-plugin";

    private static final String PLUGIN_NAME = "Protobuf Support";
    private static final Logger LOGGER = Logger.getInstance(ProtostuffPluginController.class);
    private final Project project;

    public ProtostuffPluginController(Project project) {
        this.project = project;
    }

    @Override
    public void projectClosed() {
        LOGGER.info("projectClosed " + project.getName());
    }

    @Override
    public void projectOpened() {
        try {
            checkConflictingPlugins();
        } catch (Exception e) {
            LOGGER.error("Could not detect or disable conflicting plugins", e);
        }
        Module[] modules = ModuleManager.getInstance(project).getModules();
        BundledProtobufRootsProvider bundledProtobufRootsProvider = new BundledProtobufRootsProvider();
        VirtualFile[] sourceRoots = bundledProtobufRootsProvider.getSourceRoots(null, null);
        VirtualFile sourceRoot = sourceRoots[0];
        for (Module module : modules) {
            attachJarLibrary(module, "Bundled Protobuf Distribution", sourceRoot);
        }
    }

    private void attachJarLibrary(@NotNull final Module module,
                                  @NotNull final String libName,
                                  @NotNull final VirtualFile bundle) {
        ApplicationManager.getApplication().runWriteAction(() -> {
            final ModuleRootManager rootManager = ModuleRootManager.getInstance(module);

            final ModifiableRootModel modifiableModel = rootManager.getModifiableModel();
            final LibraryTable table = modifiableModel.getModuleLibraryTable();
            Library library = table.getLibraryByName(libName);
            if (library == null) {
                library = table.createLibrary(libName);
                Library.ModifiableModel libraryModel = library.getModifiableModel();
                libraryModel.addRoot(bundle, OrderRootType.SOURCES);
                libraryModel.commit();
            }
            modifiableModel.commit();
        });
    }


    private void checkConflictingPlugins() {
        FileTypeUtil fileTypeUtil = new FileTypeUtil();
        Collection<IdeaPluginDescriptor> conflictingPlugins = fileTypeUtil.getPluginsForFile("file.proto");
        if (!conflictingPlugins.isEmpty()) {
            askUserToDisablePlugins(conflictingPlugins);
        }
    }

    private void askUserToDisablePlugins(Collection<IdeaPluginDescriptor> conflictingPlugins) {
        final String text = formatMessage(conflictingPlugins);
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

    @NotNull
    private String formatMessage(Collection<IdeaPluginDescriptor> conflictingPlugins) {
        String conflictList = formatHtmlPluginList(conflictingPlugins);
        return ProtostuffBundle.message("action.disable.conflicting.plugins", conflictList);
    }

    private String formatHtmlPluginList(Collection<IdeaPluginDescriptor> conflictingPlugins) {
        return String.join("\n", conflictingPlugins.stream()
                .map(IdeaPluginDescriptor::getName)
                .map(name -> "<li>" + name + "</li>")
                .collect(Collectors.toList()));
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

    private static class FileTypeUtil {

        private final IdeaPluginDescriptor self = PluginManager.getPlugin(PluginId.getId(PLUGIN_ID));
        private final Map<String, FileTypeEntry> fileTypes = new LinkedHashMap<>();

        FileTypeUtil() {
            ExtensionPoint<FileTypeFactory> ep = Extensions.getRootArea().getExtensionPoint(FILE_TYPE_FACTORY_EP);
            for (FileTypeFactory typeFactory : ep.getExtensions()) {
                typeFactory.createFileTypes(new FileTypeConsumer() {
                    @Override
                    public void consume(@NotNull FileType fileType) {
                        register(fileType, parse(fileType.getDefaultExtension()));
                    }

                    @Override
                    public void consume(@NotNull final FileType fileType, String extensions) {
                        register(fileType, parse(extensions));
                    }

                    @Override
                    public void consume(@NotNull final FileType fileType, @NotNull final FileNameMatcher... matchers) {
                        register(fileType, new ArrayList<>(Arrays.asList(matchers)));
                    }

                    @Override
                    public FileType getStandardFileTypeByName(@NotNull final String name) {
                        FileTypeEntry type = fileTypes.get(name);
                        return type != null ? type.fileType : null;
                    }

                    private void register(@NotNull FileType fileType, @NotNull List<FileNameMatcher> fileNameMatchers) {
                        FileTypeEntry type = fileTypes.get(fileType.getName());
                        if (type != null) {
                            type.matchers.addAll(fileNameMatchers);
                        } else {
                            fileTypes.put(fileType.getName(), new FileTypeEntry(fileType, fileNameMatchers));
                        }
                    }
                });
            }
        }

        @NotNull
        private List<FileNameMatcher> parse(@Nullable String semicolonDelimited) {
            if (semicolonDelimited == null) {
                return Collections.emptyList();
            }

            StringTokenizer tokenizer = new StringTokenizer(semicolonDelimited, FileTypeConsumer.EXTENSION_DELIMITER, false);
            ArrayList<FileNameMatcher> list = new ArrayList<>();
            while (tokenizer.hasMoreTokens()) {
                list.add(new ExtensionFileNameMatcher(tokenizer.nextToken().trim()));
            }
            return list;
        }

        Collection<IdeaPluginDescriptor> getPluginsForFile(String filename) {
            Map<PluginId, IdeaPluginDescriptor> plugins = new HashMap<>();
            fileTypes.values().forEach(type -> {
                if (type.match(filename)) {
                    Class<? extends FileType> fileTypeClass = type.fileType.getClass();
                    PluginId pluginId = PluginManager.getPluginByClassName(fileTypeClass.getName());
                    if (pluginId != null && !Objects.equals(self.getPluginId(), pluginId)) {
                        plugins.put(pluginId, PluginManager.getPlugin(pluginId));
                    }
                }
            });
            return plugins.values();
        }

        static class FileTypeEntry {
            @NotNull
            private final FileType fileType;
            @NotNull
            private final List<FileNameMatcher> matchers;

            private FileTypeEntry(@NotNull FileType fileType, @NotNull List<FileNameMatcher> matchers) {
                this.fileType = fileType;
                this.matchers = matchers;
            }

            boolean match(String filename) {
                for (FileNameMatcher matcher : matchers) {
                    if (matcher.accept(filename)) {
                        return true;
                    }
                }
                return false;
            }
        }


    }
}
