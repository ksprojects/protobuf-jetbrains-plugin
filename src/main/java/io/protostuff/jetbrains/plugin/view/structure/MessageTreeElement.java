package io.protostuff.jetbrains.plugin.view.structure;

import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import io.protostuff.jetbrains.plugin.Icons;
import io.protostuff.jetbrains.plugin.psi.EnumNode;
import io.protostuff.jetbrains.plugin.psi.MessageNode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

final class MessageTreeElement extends AbstractTreeElement<MessageNode> {

    MessageTreeElement(MessageNode element) {
        super(element);
    }

    @NotNull
    @Override
    public ItemPresentation getPresentation() {
        return new ProtoItemPresentation(element.getName(), Icons.MESSAGE);
    }

    @NotNull
    @Override
    public TreeElement[] getChildren() {
        List<TreeElement> treeElements = new ArrayList<>();
        for (PsiElement psiElement : element.getChildren()) {
            // first and the only child
            PsiElement node = psiElement.getFirstChild();
            if (node instanceof MessageNode) {
                TreeElement element = new MessageTreeElement((MessageNode) node);
                treeElements.add(element);
            }
            if (node instanceof EnumNode) {
                TreeElement element = new EnumTreeElement((EnumNode) node);
                treeElements.add(element);
            }
        }
        return treeElements.toArray(new TreeElement[treeElements.size()]);
    }


}
