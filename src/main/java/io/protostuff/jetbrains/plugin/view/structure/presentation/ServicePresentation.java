package io.protostuff.jetbrains.plugin.view.structure.presentation;

import com.intellij.navigation.ItemPresentation;
import io.protostuff.jetbrains.plugin.Icons;
import io.protostuff.jetbrains.plugin.psi.ServiceNode;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class ServicePresentation implements ItemPresentation {
    protected final ServiceNode element;

    protected ServicePresentation(ServiceNode element) {
        this.element = element;
    }

    @Nullable
    @Override
    public Icon getIcon(boolean unused) {
        return Icons.SERVICE;
    }

    @Nullable
    @Override
    public String getPresentableText() {
        return element.getName();
    }

    @Nullable
    @Override
    public String getLocationString() {
        return null;
    }
}
