package io.protostuff.jetbrains.plugin.view.structure;

import com.intellij.ide.structureView.StructureViewModel;
import com.intellij.ide.structureView.StructureViewModelBase;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.Sorter;
import com.intellij.psi.PsiElement;
import io.protostuff.jetbrains.plugin.psi.*;
import org.jetbrains.annotations.NotNull;

public class ProtoStructureViewModel
        extends StructureViewModelBase
        implements StructureViewModel.ElementInfoProvider {
    public ProtoStructureViewModel(ProtoPsiFileRoot root) {
        super(root, new ProtoStructureViewRootElement(root));
    }

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
}
