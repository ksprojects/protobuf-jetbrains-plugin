package io.protostuff.jetbrains.plugin.view.structure;

import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiFile;
import io.protostuff.jetbrains.plugin.Icons;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class ProtoRootPresentation implements ItemPresentation {
    protected final PsiFile element;

    protected ProtoRootPresentation(PsiFile element) {
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
        return element.getVirtualFile().getNameWithoutExtension();
    }

    @Nullable
    @Override
    public String getLocationString() {
        return null;
    }
}
