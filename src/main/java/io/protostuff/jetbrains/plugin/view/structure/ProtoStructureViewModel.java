package io.protostuff.jetbrains.plugin.view.structure;

import com.intellij.ide.structureView.StructureViewModel;
import com.intellij.ide.structureView.StructureViewModelBase;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.NodeProvider;
import com.intellij.ide.util.treeView.smartTree.Sorter;
import io.protostuff.jetbrains.plugin.psi.*;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;

public class ProtoStructureViewModel
        extends StructureViewModelBase
        implements StructureViewModel.ElementInfoProvider {
    private static final Collection<NodeProvider> NODE_PROVIDERS =
            Arrays.asList(new ProtoFieldsNodeProvider());

    public ProtoStructureViewModel(ProtoPsiFileRoot root) {
        super(root, getProtoRootElement(root));
    }

    @NotNull
    private static ProtoRootTreeElement getProtoRootElement(ProtoPsiFileRoot root) {
        return new ProtoRootTreeElement(root, root.findChildByClass(ProtoRootNode.class));
    }

    @Override
    @NotNull
    public Sorter[] getSorters() {
        return new Sorter[]{KindSorter.INSTANCE, Sorter.ALPHA_SORTER};
    }

    @Override
    public boolean isAlwaysLeaf(StructureViewTreeElement element) {
        Object value = element.getValue();
        return value instanceof RpcMethodNode
                || value instanceof EnumConstantNode
                || value instanceof FieldNode;
    }

    @Override
    public boolean isAlwaysShowsPlus(StructureViewTreeElement element) {
        Object value = element.getValue();
        return value instanceof ProtoPsiFileRoot;
    }

    @NotNull
    @Override
    public Collection<NodeProvider> getNodeProviders() {
        return NODE_PROVIDERS;
    }
}
