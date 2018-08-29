package io.protostuff.jetbrains.plugin.reference.file;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.vfs.VirtualFile;
import io.protostuff.jetbrains.plugin.psi.ProtoPsiFileRoot;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.Nullable;

/**
 * Many projects are storing proto files in a locations that are not marked as source/resource root.
 * As a result, imports are highlighted with error "file does not exist", and all imported types are not resolvable.
 *
 * Plugin can try to resolve imports relative to source file's location using following rules:
 *
 * 1. Try to look in the same folder where source file is.
 * 2. Try to look from parent folder.
 * 3. Repeat step 2 until reference is resolved  or there's no parent folder anymore.
 *
 * For example:
 *
 * `/home/user/project/foo/bar/hello.proto`:
 *
 * ```proto
 * package foo.bar;
 * import "baz/import.proto";
 * ```
 *
 * Import should be resolved to `/home/user/project/baz/import.proto`.
 *
 * @author Kostiantyn Shchepanovskyi
 */
class ProtoFileRelativePathRootsProvider implements FilePathReferenceProvider.SourceRootsProvider {

    private static final VirtualFile[] NONE = new VirtualFile[0];
    private static final int MAX_NESTING_LEVEL = 10;

    @Override
    public VirtualFile[] getSourceRoots(Module module, @Nullable ProtoPsiFileRoot psiFileRoot) {
        if (psiFileRoot != null) {
            VirtualFile file = psiFileRoot.getVirtualFile();
            if (file != null) {
                VirtualFile dir = file;
                List<VirtualFile> result = new ArrayList<>();
                for (int i = 0; i < MAX_NESTING_LEVEL; i++) {
                    try {
                        dir = dir.getParent();
                        if (dir != null && dir.isDirectory()) {
                            result.add(dir);
                        } else {
                            break;
                        }
                    } catch (Exception e) {
                        break;
                    }
                }
                return result.toArray(new VirtualFile[0]);
            }
        }
        return NONE;
    }

}
