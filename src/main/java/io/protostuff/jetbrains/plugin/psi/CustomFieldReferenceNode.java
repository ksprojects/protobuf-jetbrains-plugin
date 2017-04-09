package io.protostuff.jetbrains.plugin.psi;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

/**
 * Custom field reference node, part of option name (custom options).
 *
 * @author Kostiantyn Shchepanovskyi
 */
public class CustomFieldReferenceNode extends AbstractFieldReferenceNode {

    public CustomFieldReferenceNode(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public boolean isExtension() {
        return true;
    }

}
