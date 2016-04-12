package io.protostuff.jetbrains.plugin.view.structure;

import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.SortableTreeElement;
import com.intellij.navigation.NavigationItem;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import org.jetbrains.annotations.NotNull;

public abstract class ProtoStructureViewElement<ElementT extends PsiElement> implements StructureViewTreeElement, SortableTreeElement {


    protected final ElementT element;

    public ProtoStructureViewElement(ElementT element) {
        this.element = element;
    }

    @Override
    public ElementT getValue() {
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

}
