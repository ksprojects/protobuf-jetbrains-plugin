package io.protostuff.jetbrains.plugin.view.structure;

import com.intellij.ide.structureView.StructureViewModel;
import com.intellij.ide.structureView.StructureViewModelBase;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.NodeProvider;
import com.intellij.ide.util.treeView.smartTree.Sorter;
import com.intellij.openapi.editor.Editor;
import io.protostuff.jetbrains.plugin.psi.EnumConstantNode;
import io.protostuff.jetbrains.plugin.psi.EnumNode;
import io.protostuff.jetbrains.plugin.psi.FieldNode;
import io.protostuff.jetbrains.plugin.psi.MessageNode;
import io.protostuff.jetbrains.plugin.psi.ProtoPsiFileRoot;
import io.protostuff.jetbrains.plugin.psi.ProtoRootNode;
import io.protostuff.jetbrains.plugin.psi.RpcMethodNode;
import io.protostuff.jetbrains.plugin.psi.ServiceNode;
import java.util.Collection;
import java.util.Collections;
import org.jetbrains.annotations.NotNull;

final class ProtoStructureViewModel extends StructureViewModelBase
        implements StructureViewModel.ElementInfoProvider {

    private static final Collection<NodeProvider> NODE_PROVIDERS =
            Collections.singletonList(new ProtoFieldsNodeProvider());

    ProtoStructureViewModel(ProtoPsiFileRoot root, Editor editor) {
        super(root, editor, getProtoRootElement(root));
    }

    @NotNull
    private static RootTreeElement getProtoRootElement(ProtoPsiFileRoot root) {
        return new RootTreeElement(root, root.findChildByClass(ProtoRootNode.class));
    }

    /**
     * Used for "auto-scroll from source".
     */
    @NotNull
    @Override
    protected Class[] getSuitableClasses() {
        return new Class[]{MessageNode.class, EnumNode.class, ServiceNode.class,
                FieldNode.class, EnumConstantNode.class, RpcMethodNode.class};
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
