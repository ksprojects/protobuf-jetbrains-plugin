package io.protostuff.jetbrains.plugin.view.structure;

import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.SortableTreeElement;
import com.intellij.psi.NavigatablePsiElement;
import com.intellij.psi.PsiNamedElement;
import org.jetbrains.annotations.NotNull;

abstract class AbstractTreeElement<ElementT extends NavigatablePsiElement> implements StructureViewTreeElement, SortableTreeElement {

    protected final ElementT element;

    AbstractTreeElement(@NotNull ElementT element) {
        this.element = element;
    }

    @Override
    public ElementT getValue() {
        return element;
    }

    @Override
    public void navigate(boolean requestFocus) {
        element.navigate(requestFocus);
    }

    @Override
    public boolean canNavigate() {
        return element.canNavigate();
    }

    @Override
    public boolean canNavigateToSource() {
        return element.canNavigateToSource();
    }

    @NotNull
    @Override
    public String getAlphaSortKey() {
        String s = element instanceof PsiNamedElement ? element.getName() : null;
        if (s == null) {
            return "unknown key";
        }
        return s;
    }

}
