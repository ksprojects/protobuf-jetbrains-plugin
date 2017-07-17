package io.protostuff.jetbrains.plugin.reference.file;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import io.protostuff.jetbrains.plugin.psi.ProtoPsiFileRoot;

/**
 * Returns source roots for libraries and SDK classes.
 *
 * @author Kostiantyn Shchepanovskyi
 */
class LibrariesAndSdkClassesRootsProvider implements FilePathReferenceProvider.SourceRootsProvider {
    @Override
    public VirtualFile[] getSourceRoots(Module module, ProtoPsiFileRoot psiFileRoot) {
        ModuleRootManager moduleRootManager = ModuleRootManager.getInstance(module);
        return moduleRootManager.orderEntries().getAllLibrariesAndSdkClassesRoots();
    }
}
