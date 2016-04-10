package io.protostuff.jetbrains.plugin.view.structure;

import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import io.protostuff.jetbrains.plugin.Icons;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class ProtoItemPresentation implements ItemPresentation {
    protected final PsiElement element;

    protected ProtoItemPresentation(PsiElement element) {
        this.element = element;
    }

    @Nullable
    @Override
    public Icon getIcon(boolean unused) {
        return Icons.PROTO;
    }

    @Nullable
    @Override
    public String getPresentableText() {
        ASTNode node = element.getNode();
        return node.getText();
    }

    @Nullable
    @Override
    public String getLocationString() {
        return null;
    }
}
