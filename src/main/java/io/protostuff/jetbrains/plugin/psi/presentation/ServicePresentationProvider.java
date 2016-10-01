package io.protostuff.jetbrains.plugin.psi.presentation;

import io.protostuff.jetbrains.plugin.Icons;
import io.protostuff.jetbrains.plugin.psi.MessageNode;
import io.protostuff.jetbrains.plugin.psi.ServiceNode;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public class ServicePresentationProvider extends AbstractPresentationProvider<ServiceNode> {

    @Override
    protected Icon getIcon() {
        return Icons.SERVICE;
    }

    @Override
    protected String getName(@NotNull ServiceNode item) {
        return PresentationUtil.getNameForElement(item);
    }
}
