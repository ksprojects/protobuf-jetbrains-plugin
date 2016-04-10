package io.protostuff.jetbrains.plugin.view.structure.presentation;

import com.intellij.navigation.ItemPresentation;
import io.protostuff.jetbrains.plugin.Icons;
import io.protostuff.jetbrains.plugin.psi.EnumNode;
import io.protostuff.jetbrains.plugin.psi.FieldNode;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class MessageFieldPresentation implements ItemPresentation {
    protected final FieldNode element;

    protected MessageFieldPresentation(FieldNode element) {
        this.element = element;
    }

    @Nullable
    @Override
    public Icon getIcon(boolean unused) {
        return Icons.FIELD;
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
