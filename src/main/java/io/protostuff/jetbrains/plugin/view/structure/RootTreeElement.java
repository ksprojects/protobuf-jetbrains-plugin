package io.protostuff.jetbrains.plugin.view.structure;

import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import io.protostuff.jetbrains.plugin.Icons;
import io.protostuff.jetbrains.plugin.psi.EnumNode;
import io.protostuff.jetbrains.plugin.psi.MessageNode;
import io.protostuff.jetbrains.plugin.psi.ProtoPsiFileRoot;
import io.protostuff.jetbrains.plugin.psi.ProtoRootNode;
import io.protostuff.jetbrains.plugin.psi.ServiceNode;
import io.protostuff.jetbrains.plugin.psi.presentation.ProtoItemPresentation;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

final class RootTreeElement extends AbstractTreeElement<ProtoRootNode> {

    private ProtoPsiFileRoot fileRoot;

    RootTreeElement(ProtoPsiFileRoot root, ProtoRootNode element) {
        super(element);
        fileRoot = root;
    }

    @NotNull
    @Override
    public ItemPresentation getPresentation() {
        String name = fileRoot.getVirtualFile().getNameWithoutExtension();
        return new ProtoItemPresentation(name, Icons.PROTO);
    }

    @NotNull
    @Override
    public TreeElement[] getChildren() {
        List<TreeElement> treeElements = new ArrayList<>();
        for (PsiElement node : element.getChildren()) {
            if (node instanceof MessageNode) {
                TreeElement element = new MessageTreeElement((MessageNode) node);
                treeElements.add(element);
            }
            if (node instanceof EnumNode) {
                TreeElement element = new EnumTreeElement((EnumNode) node);
                treeElements.add(element);
            }
            if (node instanceof ServiceNode) {
                TreeElement element = new ServiceTreeElement((ServiceNode) node);
                treeElements.add(element);
            }
        }
        return treeElements.toArray(new TreeElement[treeElements.size()]);
    }
}
