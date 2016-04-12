package io.protostuff.jetbrains.plugin.view.structure;

import com.intellij.ide.util.ActionShortcutProvider;
import com.intellij.ide.util.FileStructureNodeProvider;
import com.intellij.ide.util.treeView.smartTree.ActionPresentation;
import com.intellij.ide.util.treeView.smartTree.ActionPresentationData;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.openapi.actionSystem.Shortcut;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import io.protostuff.jetbrains.plugin.Icons;
import io.protostuff.jetbrains.plugin.ProtostuffBundle;
import io.protostuff.jetbrains.plugin.psi.*;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public class ProtoFieldsNodeProvider implements FileStructureNodeProvider<TreeElement>, ActionShortcutProvider {

    @NonNls
    public static final String ID = "SHOW_FIELDS";

    public static Collection<TreeElement> getChildrenFromWrappedNode(PsiElement wrapperNode) {
        List<TreeElement> treeElements = new ArrayList<>();
        for (PsiElement psiElement : wrapperNode.getChildren()) {
            if (psiElement instanceof MessageBlockEntryNode
                    || psiElement instanceof EnumBlockEntryNode
                    || psiElement instanceof ServiceBlockEntryNode) {
                // first and the only child
                PsiElement node = psiElement.getFirstChild();
                if (isField(node)) {
                    TreeElement element = new ProtoStructureViewElement(node);
                    treeElements.add(element);
                }
            }
        }
        return treeElements;
    }

    public static boolean isField(PsiElement element) {
        return element instanceof FieldNode
                || element instanceof RpcMethodNode
                || element instanceof EnumConstantNode;
    }

    @NotNull
    @Override
    public String getCheckBoxText() {
        return ProtostuffBundle.message("file.structure.toggle.show.fields");
    }

    @NotNull
    @Override
    public Shortcut[] getShortcut() {
        throw new IncorrectOperationException("see getActionIdForShortcut()");
    }

    @NotNull
    @Override
    public String getActionIdForShortcut() {
        return "FileStructurePopup";
    }

    @Override
    @NotNull
    public ActionPresentation getPresentation() {
        String message = ProtostuffBundle.message("action.structureview.show.fields");
        return new ActionPresentationData(message, null, Icons.FIELD);
    }

    @Override
    @NotNull
    public String getName() {
        return ID;
    }

    @NotNull
    @Override
    public Collection<TreeElement> provideNodes(@NotNull TreeElement node) {
        if (node instanceof ProtoStructureViewElement) {
            ProtoStructureViewElement element = (ProtoStructureViewElement) node;
            PsiElement psiElement = (PsiElement) element.getValue();
            return getChildrenFromWrappedNode(psiElement);
        }
        return Collections.emptyList();
    }

}
