package io.protostuff.jetbrains.plugin.psi;

import com.intellij.lang.ASTNode;
import org.antlr.jetbrains.adapter.psi.AntlrPsiNode;
import org.jetbrains.annotations.NotNull;

/**
 * Extend node.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public class ExtendNode extends AntlrPsiNode implements KeywordsContainer {

    public ExtendNode(@NotNull ASTNode node) {
        super(node);
    }

}
