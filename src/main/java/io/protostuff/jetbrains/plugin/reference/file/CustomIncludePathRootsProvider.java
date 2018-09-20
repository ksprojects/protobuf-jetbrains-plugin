package io.protostuff.jetbrains.plugin.reference.file;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import io.protostuff.jetbrains.plugin.psi.ProtoPsiFileRoot;
import io.protostuff.jetbrains.plugin.settings.ProtobufSettings;
import java.util.ArrayList;
import java.util.List;

/**
 * Returns source roots for custom include paths.
 *
 * @author Kostiantyn Shchepanovskyi
 */
class CustomIncludePathRootsProvider implements FilePathReferenceProvider.SourceRootsProvider {
    @Override
    public VirtualFile[] getSourceRoots(Module module, ProtoPsiFileRoot psiFileRoot) {
        List<VirtualFile> result = new ArrayList<>();
        ProtobufSettings settings = ProtobufSettings.getInstance();
        List<String> includePaths = settings.getIncludePaths();
        for (String includePath : includePaths) {
            VirtualFile path = LocalFileSystem.getInstance().findFileByPath(includePath);
            if (path != null && path.isDirectory()) {
                result.add(path);
            }
        }
        return result.toArray(new VirtualFile[0]);
    }
}
