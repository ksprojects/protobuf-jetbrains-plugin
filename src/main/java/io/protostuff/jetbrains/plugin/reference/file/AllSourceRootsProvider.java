package io.protostuff.jetbrains.plugin.reference.file;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import io.protostuff.jetbrains.plugin.psi.ProtoPsiFileRoot;

/**
 * Returns "all source roots". In the IntelliJ IDEA it include source & resource roots,
 * for both main and test source sets.
 *
 * @author Kostiantyn Shchepanovskyi
 */
class AllSourceRootsProvider implements FilePathReferenceProvider.SourceRootsProvider {
    @Override
    public VirtualFile[] getSourceRoots(Module module, ProtoPsiFileRoot psiFileRoot) {
        ModuleRootManager moduleRootManager = ModuleRootManager.getInstance(module);
        return moduleRootManager.orderEntries().getAllSourceRoots();
    }
}
