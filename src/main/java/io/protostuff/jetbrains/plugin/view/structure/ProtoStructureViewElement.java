package io.protostuff.jetbrains.plugin.view.structure;

import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.SortableTreeElement;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.NavigationItem;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import io.protostuff.jetbrains.plugin.ProtoLanguage;
import io.protostuff.jetbrains.plugin.psi.*;
import org.antlr.jetbrains.adapter.xpath.XPath;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ProtoStructureViewElement implements StructureViewTreeElement, SortableTreeElement {
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
        if (element instanceof MessageNode) {
            return new ProtoMessagePresentation((MessageNode) element);
        } else if (element instanceof EnumNode) {
            return new ProtoEnumPresentation((EnumNode) element);
        } else if (element instanceof ServiceNode) {
            return new ProtoServicePresentation((ServiceNode) element);
        } else {
            return new ProtoItemPresentation(element);
        }
    }

    @NotNull
    @Override
    public TreeElement[] getChildren() {
        if (element instanceof ProtoPsiFileRoot) {
            ProtoPsiFileRoot root = (ProtoPsiFileRoot) element;
            ProtoRootNode protoRootNode = root.findChildByClass(ProtoRootNode.class);
            return getChildren(protoRootNode);
        }
        return new TreeElement[0];
    }

    public TreeElement[] getChildren(ProtoRootNode root) {
        List<TreeElement> treeElements = new ArrayList<>();
        for (PsiElement psiElement : root.getChildren()) {
            if (psiElement instanceof ProtoRootStatementNode) {
                PsiElement node = extractWrappedStatement((ProtoRootStatementNode) psiElement);
                ProtoStructureViewElement viewElement;
                if (node instanceof MessageNode) {
                    viewElement = new ProtoStructureViewElement(node);
                    treeElements.add(viewElement);
                } else if (node instanceof EnumNode) {
                    viewElement = new ProtoStructureViewElement(node);
                    treeElements.add(viewElement);
                } else if (node instanceof ServiceNode) {
                    viewElement = new ProtoStructureViewElement(node);
                    treeElements.add(viewElement);
                }
            }
        }
        return treeElements.toArray(new TreeElement[treeElements.size()]);
    }

    public PsiElement extractWrappedStatement(ProtoRootStatementNode statementNode) {
        // first and the only child
        PsiElement child = statementNode.getFirstChild();
        return child;
    }

}
