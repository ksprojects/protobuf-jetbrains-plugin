package io.protostuff.jetbrains.plugin.view.structure;

import com.intellij.ide.structureView.StructureViewModel;
import com.intellij.ide.structureView.StructureViewModelBase;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.structureView.impl.java.*;
import com.intellij.ide.util.treeView.smartTree.Filter;
import com.intellij.ide.util.treeView.smartTree.NodeProvider;
import com.intellij.ide.util.treeView.smartTree.Sorter;
import com.intellij.psi.PsiElement;
import io.protostuff.jetbrains.plugin.psi.*;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;

public class ProtoStructureViewModel
        extends StructureViewModelBase
        implements StructureViewModel.ElementInfoProvider {
    public ProtoStructureViewModel(ProtoPsiFileRoot root) {
        super(root, new ProtoStructureViewRootElement(root));
    }

    private static final Collection<NodeProvider> NODE_PROVIDERS =
            Arrays.asList(new ProtoFieldsNodeProvider());


    @NotNull
    public Sorter[] getSorters() {
        return new Sorter[]{Sorter.ALPHA_SORTER};
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
