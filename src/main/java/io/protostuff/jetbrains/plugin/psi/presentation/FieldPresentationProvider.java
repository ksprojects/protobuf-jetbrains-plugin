package io.protostuff.jetbrains.plugin.psi.presentation;

import io.protostuff.jetbrains.plugin.Icons;
import io.protostuff.jetbrains.plugin.psi.MessageField;
import javax.swing.Icon;
import org.jetbrains.annotations.NotNull;

/**
 * Message node presentation provider.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public class FieldPresentationProvider extends AbstractPresentationProvider<MessageField> {

    @Override
    protected Icon getIcon() {
        return Icons.FIELD;
    }

    @Override
    protected String getName(@NotNull MessageField item) {
        return PresentationUtil.getNameForElement(item);
    }
}
