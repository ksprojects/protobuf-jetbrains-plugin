package io.protostuff.jetbrains.plugin.view.structure;

import com.intellij.ide.structureView.StructureViewModel;
import com.intellij.ide.structureView.StructureViewModelBase;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.Sorter;
import com.intellij.psi.PsiElement;
import io.protostuff.jetbrains.plugin.psi.EnumNode;
import io.protostuff.jetbrains.plugin.psi.MessageNode;
import io.protostuff.jetbrains.plugin.psi.ProtoPsiFileRoot;
import io.protostuff.jetbrains.plugin.psi.ServiceNode;
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
    protected boolean isSuitable(PsiElement element) {
        if (super.isSuitable(element)) {
            return element instanceof MessageNode
                    || element instanceof ServiceNode
                    || element instanceof EnumNode;
        }
        return false;
    }

    @Override
    public boolean isAlwaysLeaf(StructureViewTreeElement element) {
        return !isAlwaysShowsPlus(element);
    }

    @Override
    public boolean isAlwaysShowsPlus(StructureViewTreeElement element) {
        Object value = element.getValue();
        return value instanceof ProtoPsiFileRoot;
    }
}
