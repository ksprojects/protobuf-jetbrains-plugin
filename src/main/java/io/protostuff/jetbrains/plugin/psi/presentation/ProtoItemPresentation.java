package io.protostuff.jetbrains.plugin.psi.presentation;

import com.intellij.navigation.ItemPresentation;
import javax.swing.Icon;
import org.jetbrains.annotations.Nullable;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public final class ProtoItemPresentation implements ItemPresentation {

    private final String name;
    private final String location;
    private final Icon icon;

    public ProtoItemPresentation(String name, Icon icon) {
        this.name = name;
        this.icon = icon;
        this.location = null;
    }

    public ProtoItemPresentation(String name, String location, Icon icon) {
        this.name = name;
        this.location = location;
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
        return location;
    }

    @Nullable
    @Override
    public Icon getIcon(boolean unused) {
        return icon;
    }
}
