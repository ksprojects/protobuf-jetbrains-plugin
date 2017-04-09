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
import io.protostuff.jetbrains.plugin.psi.EnumConstantNode;
import io.protostuff.jetbrains.plugin.psi.FieldNode;
import io.protostuff.jetbrains.plugin.psi.RpcMethodNode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * Field nodes provider for Structure View.
 *
 * @author Kostiantyn Shchepanovskyi
 */
final class ProtoFieldsNodeProvider implements FileStructureNodeProvider<TreeElement>, ActionShortcutProvider {

    private static final String ID = "SHOW_FIELDS";

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
    public Collection<TreeElement> provideNodes(@NotNull TreeElement parent) {
        if (parent instanceof AbstractTreeElement) {
            AbstractTreeElement element = (AbstractTreeElement) parent;
            PsiElement psiElement = element.getValue();
            List<TreeElement> treeElements = new ArrayList<>();
            for (PsiElement node : psiElement.getChildren()) {
                if (node instanceof FieldNode) {
                    treeElements.add(new MessageFieldTreeElement((FieldNode) node));
                }
                if (node instanceof EnumConstantNode) {
                    treeElements.add(new EnumConstantTreeElement((EnumConstantNode) node));
                }
                if (node instanceof RpcMethodNode) {
                    treeElements.add(new ServiceMethodTreeElement((RpcMethodNode) node));
                }
            }
            return treeElements;
        }
        return Collections.emptyList();
    }

}
