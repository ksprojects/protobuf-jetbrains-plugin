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
import java.util.*;

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
        return "Protobuf File: " + (virtualFile != null ? virtualFile.getName() : "<unknown>");
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

    /**
     * Returns all messages, enums and services defined in this proto file.
     */
    public Collection<ProtoType> getAllTypes() {
        ProtoRootNode root = findChildByClass(ProtoRootNode.class);
        if (root != null) {
            List<ProtoType> result = new ArrayList<>();
            Collection<ProtoType> declaredTypes = root.getDeclaredTypes();
            result.addAll(declaredTypes);
            addChildrenRecursively(declaredTypes, result);
            return result;
        }
        return Collections.emptyList();
    }

    private void addChildrenRecursively(Collection<? extends ProtoType> elements, List<ProtoType> result) {
        for (ProtoType element : elements) {
            if (element instanceof DataTypeContainer) {
                DataTypeContainer container = (DataTypeContainer) element;
                Collection<DataType> declaredDataTypes = container.getDeclaredDataTypes();
                result.addAll(declaredDataTypes);
                addChildrenRecursively(declaredDataTypes, result);
            }
        }
    }

    @NotNull
    public String getPackageName() {
        ProtoRootNode root = findChildByClass(ProtoRootNode.class);
        if (root != null) {
            return root.getPackageName();
        }
        return "";
    }

    @Nullable
    public ProtoType findType(String fullName) {
        Collection<ProtoType> all = getAllTypes();
        for (ProtoType type : all) {
            if (Objects.equals(fullName, type.getFullName())) {
                return type;
            }
        }
        return null;
    }
}
