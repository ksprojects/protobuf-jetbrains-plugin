package io.protostuff.jetbrains.plugin.psi;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import io.protostuff.jetbrains.plugin.Icons;
import io.protostuff.jetbrains.plugin.ProtoFileType;
import io.protostuff.jetbrains.plugin.ProtoLanguage;
import org.antlr.jetbrains.adapter.psi.ScopeNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public class ProtoPsiFileRoot extends PsiFileBase implements ScopeNode {
    public ProtoPsiFileRoot(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, ProtoLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return ProtoFileType.INSTANCE;
    }

    @Override
    public String toString() {
        final VirtualFile virtualFile = getVirtualFile();
        return "ProtobufFile: " + (virtualFile != null ? virtualFile.getName() : "<unknown>");
    }

    @Override
    public Icon getIcon(int flags) {
        return Icons.PROTO;
    }

    /**
     * Return null since a file scope has no enclosing scope. It is
     * not itself in a scope.
     */
    @Override
    public ScopeNode getContext() {
        return null;
    }

    @Nullable
    @Override
    public PsiElement resolve(PsiNamedElement element) {
        return null;
    }
}
