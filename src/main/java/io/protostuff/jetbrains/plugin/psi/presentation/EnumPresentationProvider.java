package io.protostuff.jetbrains.plugin.psi.presentation;

import io.protostuff.jetbrains.plugin.Icons;
import io.protostuff.jetbrains.plugin.psi.EnumNode;
import io.protostuff.jetbrains.plugin.psi.MessageNode;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
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
