package io.protostuff.jetbrains.plugin.reference.file;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.vfs.VirtualFile;
import io.protostuff.jetbrains.plugin.psi.ProtoPsiFileRoot;
import java.util.Arrays;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Many projects are storing proto files in a locations that are not marked as source/resource root.
 * As a result, imports are highlighted with error "file does not exist", and all imported types are not resolvable.
 *
 * Plugin can try to resolve imports relative to source file's location using following rules:
 *
 * 1. If `package` is not set, try to look in the same folder where source file is.
 * 2. If package is set, try to look from parent folder.
 * 3. Parent folder should be computed as source file's folders, with removed corresponding parts of the package.
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

    @Override
    public VirtualFile[] getSourceRoots(Module module, @Nullable ProtoPsiFileRoot psiFileRoot) {
        if (psiFileRoot != null) {
            VirtualFile file = psiFileRoot.getVirtualFile();
            if (file != null) {
                String packageName = psiFileRoot.getPackageName();
                // no package - return parent dir
                VirtualFile dir = file.getParent();
                int nestingLevel = computeNestingLevel(packageName);
                dir = goUp(dir, nestingLevel);
                if (dir != null) {
                    return toArray(dir);
                }
            }
        }
        return NONE;
    }

    @Nullable
    private VirtualFile goUp(VirtualFile dir, int times) {
        while (times > 0 && dir != null) {
            dir = dir.getParent();
            times--;
        }
        return dir;
    }

    private int computeNestingLevel(String packageName) {
        // compute nesting level
        int nestingLevel = 0;
        if (!packageName.isEmpty()) {
            nestingLevel++;
            for (char c : packageName.toCharArray()) {
                if (c == '.') {
                    nestingLevel++;
                }
            }
        }
        return nestingLevel;
    }

    @NotNull
    private VirtualFile[] toArray(VirtualFile dir) {
        return new VirtualFile[]{dir};
    }
}
