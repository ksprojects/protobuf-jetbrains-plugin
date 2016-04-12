package io.protostuff.jetbrains.plugin.view.structure;

import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.SortableTreeElement;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.NavigationItem;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import io.protostuff.jetbrains.plugin.psi.*;
import io.protostuff.jetbrains.plugin.view.structure.presentation.PresentationFactoryImpl;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ProtoStructureViewElement implements StructureViewTreeElement, SortableTreeElement {

    private static final PresentationFactory presentationFactory = new PresentationFactoryImpl();

    protected final PsiElement element;

    public ProtoStructureViewElement(PsiElement element) {
        this.element = element;
    }

    @Override
    public Object getValue() {
        return element;
    }

    @Override
    public void navigate(boolean requestFocus) {
        if (element instanceof NavigationItem) {
            ((NavigationItem) element).navigate(requestFocus);
        }
    }

    @Override
    public boolean canNavigate() {
        return element instanceof NavigationItem &&
                ((NavigationItem) element).canNavigate();
    }

    @Override
    public boolean canNavigateToSource() {
        return element instanceof NavigationItem &&
                ((NavigationItem) element).canNavigateToSource();
    }

    @NotNull
    @Override
    public String getAlphaSortKey() {
        String s = element instanceof PsiNamedElement ? ((PsiNamedElement) element).getName() : null;
        if (s == null) return "unknown key";
        return s;
    }

    @NotNull
    @Override
    public ItemPresentation getPresentation() {
        return presentationFactory.createPresentation(element);
    }

    @NotNull
    @Override
    public TreeElement[] getChildren() {
        if (element instanceof ProtoPsiFileRoot) {
            ProtoPsiFileRoot root = (ProtoPsiFileRoot) element;
            ProtoRootNode protoRootNode = root.findChildByClass(ProtoRootNode.class);
            return getChildrenFromWrappedNode(protoRootNode);
        }
        if (element instanceof MessageNode
                || element instanceof EnumNode
                || element instanceof ServiceNode) {
            return getChildrenFromWrappedNode(element);
        }
        return new TreeElement[0];
    }

    public TreeElement[] getChildrenFromWrappedNode(PsiElement wrapperNode) {
        List<TreeElement> treeElements = new ArrayList<>();
        for (PsiElement psiElement : wrapperNode.getChildren()) {
            if (psiElement instanceof ProtoRootStatementNode
                    || psiElement instanceof MessageBlockEntryNode
                    || psiElement instanceof EnumBlockEntryNode
                    || psiElement instanceof ServiceBlockEntryNode) {
                // first and the only child
                PsiElement node = psiElement.getFirstChild();
                if (hasPresentation(node)) {
                    TreeElement element = new ProtoStructureViewElement(node);
                    treeElements.add(element);
                }
            }
        }
        return treeElements.toArray(new TreeElement[treeElements.size()]);
    }

    public boolean hasPresentation(PsiElement element) {
        return element instanceof MessageNode
                || element instanceof EnumNode
                || element instanceof ServiceNode;
    }

}
