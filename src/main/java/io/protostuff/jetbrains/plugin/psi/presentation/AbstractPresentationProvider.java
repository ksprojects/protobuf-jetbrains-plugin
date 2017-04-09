package io.protostuff.jetbrains.plugin.psi.presentation;

import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.ItemPresentationProvider;
import com.intellij.navigation.NavigationItem;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import io.protostuff.jetbrains.plugin.psi.ProtoPsiFileRoot;
import javax.swing.Icon;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Base class for presentation providers.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public abstract class AbstractPresentationProvider<T extends PsiElement & NavigationItem>
        implements ItemPresentationProvider<T> {

    @Override
    public ItemPresentation getPresentation(@NotNull T item) {
        String name = getName(item);
        String location = getLocationString(item);
        Icon icon = getIcon();
        return new ProtoItemPresentation(name, location, icon);
    }

    protected abstract Icon getIcon();

    protected abstract String getName(@NotNull T item);

    @Nullable
    private String getLocationString(@NotNull T item) {
        PsiFile file = item.getContainingFile();
        if (file instanceof ProtoPsiFileRoot) {
            ProtoPsiFileRoot classOwner = (ProtoPsiFileRoot) file;
            String packageName = classOwner.getPackageName();
            if (packageName.isEmpty()) {
                return null;
            }
            return "(" + packageName + ")";
        }
        return null;
    }
}
