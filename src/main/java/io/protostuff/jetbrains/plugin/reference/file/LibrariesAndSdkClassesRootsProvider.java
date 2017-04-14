package io.protostuff.jetbrains.plugin.reference.file;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VirtualFile;

/**
 * Returns source roots for libraries and SDK classes.
 *
 * @author Kostiantyn Shchepanovskyi
 */
class LibrariesAndSdkClassesRootsProvider implements FilePathReferenceProvider.SourceRootsProvider {
    @Override
    public VirtualFile[] getSourceRoots(Module module) {
        ModuleRootManager moduleRootManager = ModuleRootManager.getInstance(module);
        return moduleRootManager.orderEntries().getAllLibrariesAndSdkClassesRoots();
    }
}
