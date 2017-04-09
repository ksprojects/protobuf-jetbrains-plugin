package io.protostuff.jetbrains.plugin.psi;

import com.intellij.lang.ASTNode;
import org.antlr.jetbrains.adapter.psi.AntlrPsiNode;
import org.jetbrains.annotations.NotNull;

/**
 * Base class for field reference node parts, used in option name.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public abstract class AbstractFieldReferenceNode extends AntlrPsiNode {

    public AbstractFieldReferenceNode(@NotNull ASTNode node) {
        super(node);
    }


    /**
     * Returns true if field reference refers to an extension field.
     */
    public abstract boolean isExtension();

}
