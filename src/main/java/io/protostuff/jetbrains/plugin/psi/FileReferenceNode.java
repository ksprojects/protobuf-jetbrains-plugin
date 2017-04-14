package io.protostuff.jetbrains.plugin.psi;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileSystemItem;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiReference;
import io.protostuff.compiler.parser.Util;
import io.protostuff.jetbrains.plugin.ProtoLanguage;
import io.protostuff.jetbrains.plugin.reference.file.FilePathReferenceProvider;
import io.protostuff.jetbrains.plugin.resources.BundledFileProvider;
import java.util.Collection;
import org.antlr.jetbrains.adapter.psi.AntlrPsiNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * File reference node.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public class FileReferenceNode extends AntlrPsiNode {

    private final FilePathReferenceProvider referenceProvider;

    public FileReferenceNode(@NotNull ASTNode node) {
        super(node);
        referenceProvider = new FilePathReferenceProvider(true);
    }

    @NotNull
    @Override
    public PsiReference[] getReferences() {
        String filename = getFilename();
        if (filename == null) {
            return new PsiReference[0];
        }
        Module module = ModuleUtilCore.findModuleForPsiElement(this);
        if (module != null) {
            referenceProvider.getReferencesByElement(this, filename, 1, true);
        }
        // fallback: if we are inside of a dependency, current module is null
        // in this case we try to resolve reference in all dependencies of all modules
        // (might be not fully correct, but better than nothing)
        ModuleManager moduleManager = ModuleManager.getInstance(getProject());
        Module[] modules = moduleManager.getModules();
        return referenceProvider.getReferencesByElement(this, filename, 1, true, modules);
    }

    /**
     * Returns target proto PSI file root node.
     */
    @Nullable
    public ProtoPsiFileRoot getTarget() {
        String filename = getFilename();
        if (filename == null) {
            return null;
        }
        Module module = ModuleUtilCore.findModuleForPsiElement(this);
        if (module != null) {
            return getTarget(filename, module);
        }
        // fallback: if we are inside of a dependency, current module is null
        // in this case we try to resolve reference in all dependencies of all modules
        // (might be not fully correct, but better than nothing)
        ModuleManager moduleManager = ModuleManager.getInstance(getProject());
        Module[] modules = moduleManager.getModules();
        for (Module m : modules) {
            ProtoPsiFileRoot target = getTarget(filename, m);
            if (target != null) {
                return target;
            }
        }
        return null;
    }

    private ProtoPsiFileRoot getTarget(@NotNull String filename, @NotNull Module module) {
        Collection<PsiFileSystemItem> roots = new FilePathReferenceProvider().getRoots(module);
        for (PsiFileSystemItem root : roots) {
            VirtualFile file = root.getVirtualFile().findFileByRelativePath(getFilename());
            if (file != null) {
                PsiManager psiManager = PsiManager.getInstance(getProject());
                PsiFile psiFile = psiManager.findFile(file);
                if (psiFile instanceof ProtoPsiFileRoot) {
                    return (ProtoPsiFileRoot) psiFile;
                }
            }
        }
        // Local lookup failed, check bundled resources
        if (BundledFileProvider.DESCRIPTOR_PROTO_NAME.equals(filename)) {
            BundledFileProvider bundledFileProvider = getProject().getComponent(BundledFileProvider.class);
            PsiFile file = bundledFileProvider.getFile(BundledFileProvider.DESCRIPTOR_PROTO_RESOURCE,
                    ProtoLanguage.INSTANCE,
                    BundledFileProvider.DESCRIPTOR_PROTO_NAME);
            return (ProtoPsiFileRoot) file;
        }
        return null;
    }

    @Nullable
    private String getFilename() {
        String text = getText();
        if (text == null
                || text.length() < 2
                || !text.startsWith("\"")
                || !text.endsWith("\"")) {
            return null;
        }
        return Util.removeFirstAndLastChar(text);
    }

}
