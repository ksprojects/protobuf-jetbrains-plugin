package io.protostuff.jetbrains.plugin.reference.file;

import com.google.common.base.Strings;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.containers.MultiMap;
import io.protostuff.jetbrains.plugin.psi.ProtoPsiFileRoot;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * A workaround for getting resource roots in WebStorm 2017.1.
 *
 * https://github.com/protostuff/protobuf-jetbrains-plugin/issues/30
 * https://intellij-support.jetbrains.com/hc/en-us/community/posts/115000163684-Resource-roots-in-WebStorm
 *
 * @author Kostiantyn Shchepanovskyi
 */
class WebCoreResourcePathRootsProvider implements FilePathReferenceProvider.SourceRootsProvider {

    private static final Logger LOGGER = Logger.getInstance(WebCoreResourcePathRootsProvider.class);

    private static final Method GET_INSTANCE;
    private static final Method GET_RESOURCE_ROOTS;

    public static final String CLASS_NAME = "com.intellij.webcore.resourceRoots.WebResourcesPathsConfiguration";

    static {
        Method getInstance = null;
        Method getResourceRoots = null;
        try {
            Class<?> clazz = Class.forName(CLASS_NAME);
            getInstance = clazz.getMethod("getInstance", Project.class);
            getResourceRoots = clazz.getMethod("getResourceRoots");
        } catch (Exception e) {
            LOGGER.info("Could not load class " + CLASS_NAME);
        }
        GET_INSTANCE = getInstance;
        GET_RESOURCE_ROOTS = getResourceRoots;
    }


    @SuppressWarnings("unchecked")
    @Override
    public VirtualFile[] getSourceRoots(Module module, ProtoPsiFileRoot psiFileRoot) {
        try {
            if (GET_INSTANCE != null && GET_RESOURCE_ROOTS != null) {
                Object configurationInstance = GET_INSTANCE.invoke(null, module.getProject());
                if (configurationInstance != null) {
                    List<VirtualFile> result = new ArrayList<>();
                    MultiMap<String, String> resourceRoots = (MultiMap<String, String>) GET_RESOURCE_ROOTS.invoke(configurationInstance);
                    for (Map.Entry<String, Collection<String>> entry : resourceRoots.entrySet()) {
                        for (String uri : entry.getValue()) {
                            if (!Strings.isNullOrEmpty(uri) && uri.startsWith("file://")) {
                                String path = uri.substring(7);
                                VirtualFile file = LocalFileSystem.getInstance().findFileByPath(path);
                                if (file != null && file.isDirectory()) {
                                    result.add(file);
                                }
                            }
                        }
                    }
                    return result.toArray(new VirtualFile[0]);
                }
            }
        } catch (Exception e) {
            LOGGER.warn("Could not get source roots for WebCore IDE", e);
        }
        return new VirtualFile[0];
    }
}
