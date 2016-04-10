package io.protostuff.jetbrains.plugin.view.structure.presentation;

import com.intellij.navigation.ItemPresentation;
import io.protostuff.jetbrains.plugin.Icons;
import io.protostuff.jetbrains.plugin.psi.EnumConstantNode;
import io.protostuff.jetbrains.plugin.psi.RpcMethodNode;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class RpcMethodPresentation implements ItemPresentation {
    protected final RpcMethodNode element;

    protected RpcMethodPresentation(RpcMethodNode element) {
        this.element = element;
    }

    @Nullable
    @Override
    public Icon getIcon(boolean unused) {
        return Icons.RPC;
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
