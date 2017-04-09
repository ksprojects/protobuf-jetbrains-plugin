package io.protostuff.jetbrains.plugin.psi;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

/**
 * Standard field reference node, part of option name.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public class StandardFieldReferenceNode extends AbstractFieldReferenceNode {

    public StandardFieldReferenceNode(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public boolean isExtension() {
        return false;
    }

}
