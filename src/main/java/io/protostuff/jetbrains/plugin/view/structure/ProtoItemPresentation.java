package io.protostuff.jetbrains.plugin.view.structure;

import com.intellij.navigation.ItemPresentation;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author Kostiantyn Shchepanovskyi
 */
final class ProtoItemPresentation implements ItemPresentation {

    private final String name;
    private final Icon icon;

    ProtoItemPresentation(String name, Icon icon) {
        this.name = name;
        this.icon = icon;
    }

    @Nullable
    @Override
    public String getPresentableText() {
        return name;
    }

    @Nullable
    @Override
    public String getLocationString() {
        return null;
    }

    @Nullable
    @Override
    public Icon getIcon(boolean unused) {
        return icon;
    }
}
