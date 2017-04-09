package io.protostuff.jetbrains.plugin.psi.presentation;

import io.protostuff.jetbrains.plugin.Icons;
import io.protostuff.jetbrains.plugin.psi.EnumNode;
import javax.swing.Icon;
import org.jetbrains.annotations.NotNull;

/**
 * Enum node presentation provider.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public class EnumPresentationProvider extends AbstractPresentationProvider<EnumNode> {

    @Override
    protected Icon getIcon() {
        return Icons.ENUM;
    }

    @Override
    protected String getName(@NotNull EnumNode item) {
        return PresentationUtil.getNameForElement(item);
    }
}
