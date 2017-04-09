package io.protostuff.jetbrains.plugin.psi.presentation;

import io.protostuff.jetbrains.plugin.Icons;
import io.protostuff.jetbrains.plugin.psi.ServiceNode;
import javax.swing.Icon;
import org.jetbrains.annotations.NotNull;

/**
 * Service node presentation provider.
 *
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
