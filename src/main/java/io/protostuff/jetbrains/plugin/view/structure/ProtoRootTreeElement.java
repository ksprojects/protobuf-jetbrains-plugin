package io.protostuff.jetbrains.plugin.view.structure;

import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import io.protostuff.jetbrains.plugin.Icons;
import io.protostuff.jetbrains.plugin.psi.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ProtoRootTreeElement extends ProtoStructureViewElement<ProtoRootNode> {

    private ProtoPsiFileRoot fileRoot;

    public ProtoRootTreeElement(ProtoPsiFileRoot root, ProtoRootNode element) {
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
        for (PsiElement psiElement : element.getChildren()) {
            if (psiElement instanceof ProtoRootStatementNode) {
                // first and the only child
                PsiElement node = psiElement.getFirstChild();
                if (node instanceof MessageNode) {
                    TreeElement element = new ProtoMessageTreeElement((MessageNode) node);
                    treeElements.add(element);
                }
                if (node instanceof EnumNode) {
                    TreeElement element = new ProtoEnumTreeElement((EnumNode) node);
                    treeElements.add(element);
                }
                if (node instanceof ServiceNode) {
                    TreeElement element = new ProtoServiceTreeElement((ServiceNode) node);
                    treeElements.add(element);
                }
            }
        }
        return treeElements.toArray(new TreeElement[treeElements.size()]);
    }
}
