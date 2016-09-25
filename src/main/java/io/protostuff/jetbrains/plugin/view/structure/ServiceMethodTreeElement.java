package io.protostuff.jetbrains.plugin.view.structure;

import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.navigation.ItemPresentation;
import io.protostuff.jetbrains.plugin.Icons;
import io.protostuff.jetbrains.plugin.psi.RpcMethodNode;
import io.protostuff.jetbrains.plugin.psi.presentation.ProtoItemPresentation;
import org.jetbrains.annotations.NotNull;

final class ServiceMethodTreeElement extends AbstractTreeElement<RpcMethodNode> {

    ServiceMethodTreeElement(RpcMethodNode element) {
        super(element);
    }

    @NotNull
    @Override
    public ItemPresentation getPresentation() {
        return new ProtoItemPresentation(element.getName(), Icons.RPC);
    }

    @NotNull
    @Override
    public TreeElement[] getChildren() {
        // No default children for enums.
        return new TreeElement[0];
    }


}
