package io.protostuff.jetbrains.plugin.view.structure;

import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.navigation.ItemPresentation;
import io.protostuff.jetbrains.plugin.Icons;
import io.protostuff.jetbrains.plugin.psi.EnumNode;
import org.jetbrains.annotations.NotNull;

public class ProtoEnumTreeElement extends ProtoStructureViewElement<EnumNode> {

    public ProtoEnumTreeElement(EnumNode element) {
        super(element);
    }

    @NotNull
    @Override
    public ItemPresentation getPresentation() {
        return new ProtoItemPresentation(element.getName(), Icons.ENUM);
    }

    @NotNull
    @Override
    public TreeElement[] getChildren() {
        // No default children for enums.
        return new TreeElement[0];
    }


}
