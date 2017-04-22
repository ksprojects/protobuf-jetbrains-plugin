package io.protostuff.jetbrains.plugin.psi.presentation;

import io.protostuff.jetbrains.plugin.Icons;
import io.protostuff.jetbrains.plugin.psi.EnumConstantNode;
import javax.swing.Icon;
import org.jetbrains.annotations.NotNull;

/**
 * Message node presentation provider.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public class EnumValuePresentationProvider extends AbstractPresentationProvider<EnumConstantNode> {

    @Override
    protected Icon getIcon() {
        return Icons.CONSTANT;
    }

    @Override
    protected String getName(@NotNull EnumConstantNode item) {
        return PresentationUtil.getNameForElement(item);
    }
}
