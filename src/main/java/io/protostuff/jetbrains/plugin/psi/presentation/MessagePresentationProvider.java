package io.protostuff.jetbrains.plugin.psi.presentation;

import io.protostuff.jetbrains.plugin.Icons;
import io.protostuff.jetbrains.plugin.psi.MessageNode;
import javax.swing.Icon;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public class MessagePresentationProvider extends AbstractPresentationProvider<MessageNode> {

    @Override
    protected Icon getIcon() {
        return Icons.MESSAGE;
    }

    @Override
    protected String getName(@NotNull MessageNode item) {
        return PresentationUtil.getNameForElement(item);
    }
}
