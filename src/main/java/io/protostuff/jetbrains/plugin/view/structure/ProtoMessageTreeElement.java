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

public class ProtoMessageTreeElement extends ProtoStructureViewElement<MessageNode> {

    public ProtoMessageTreeElement(MessageNode element) {
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
                TreeElement element = new ProtoMessageTreeElement((MessageNode) node);
                treeElements.add(element);
            }
            if (node instanceof EnumNode) {
                TreeElement element = new ProtoEnumTreeElement((EnumNode) node);
                treeElements.add(element);
            }
        }
        return treeElements.toArray(new TreeElement[treeElements.size()]);
    }


}
