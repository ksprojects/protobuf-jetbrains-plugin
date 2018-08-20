package io.protostuff.jetbrains.plugin.reference.file;

import static io.protostuff.jetbrains.plugin.ProtostuffPluginController.PLUGIN_ID;

import com.google.common.base.Splitter;
import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.IdeaPluginDescriptorImpl;
import com.intellij.ide.plugins.PluginManager;
import com.intellij.ide.plugins.cl.PluginClassLoader;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.vfs.JarFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import io.protostuff.jetbrains.plugin.psi.ProtoPsiFileRoot;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;

/**
 * Returns source roots for files bundled in protobuf-java jar.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public class BundledProtobufRootsProvider implements FilePathReferenceProvider.SourceRootsProvider {

    private static final String PREFIX = "protobuf-java-";
    private static final String SUFFIX = "-sources";
    private VirtualFile[] protobufJarVfs;

    @SuppressWarnings("OptionalIsPresent")
    @Override
    public VirtualFile[] getSourceRoots(Module module, ProtoPsiFileRoot psiFileRoot) {

        if (protobufJarVfs == null) {
            Optional<VirtualFile> pluginLibFolder = tryPluginLibFolder();
            Optional<VirtualFile> classpath = tryClasspath();
            if (pluginLibFolder.isPresent()) {
                protobufJarVfs = new VirtualFile[]{pluginLibFolder.get()};
            } else if (classpath.isPresent()) {
                protobufJarVfs = new VirtualFile[]{classpath.get()};
            } else {
                throw new IllegalStateException("Could not find protobuf sources jar");
            }
        }
        return protobufJarVfs;
    }

    private Optional<VirtualFile> tryClasspath() {
        String classpath = System.getProperty("java.class.path");
        String separator = System.getProperty("path.separator");
        Iterable<String> classpathEntries = Splitter.on(separator).split(classpath);
        return StreamSupport.stream(classpathEntries.spliterator(), false)
                .filter(entry -> entry.contains(PREFIX))
                .filter(entry -> entry.contains(SUFFIX))
                .findAny()
                .map(protobufSourcesJar -> {
                    JarFileSystem instance = JarFileSystem.getInstance();
                    return instance.findLocalVirtualFileByPath(protobufSourcesJar);
                });
    }

    private Optional<VirtualFile> tryPluginLibFolder() {
        IdeaPluginDescriptor plugin = Objects.requireNonNull(PluginManager.getPlugin(PluginId.getId(PLUGIN_ID)));
        return ((IdeaPluginDescriptorImpl) plugin).getClassPath().stream()
                .filter(entry -> entry.getName().contains(PREFIX))
                .filter(entry -> entry.getName().contains(SUFFIX))
                .findAny()
                .map(protobufSourcesJarUrl -> {
                    JarFileSystem instance = JarFileSystem.getInstance();
                    return instance.findLocalVirtualFileByPath(protobufSourcesJarUrl.getPath());
                });
    }
}
