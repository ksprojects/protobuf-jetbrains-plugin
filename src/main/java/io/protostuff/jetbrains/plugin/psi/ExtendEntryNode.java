package io.protostuff.jetbrains.plugin.psi;

import com.intellij.lang.ASTNode;
import org.antlr.jetbrains.adapter.psi.AntlrPsiNode;
import org.jetbrains.annotations.NotNull;

/**
 * Extend entry node.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public class ExtendEntryNode extends AntlrPsiNode implements KeywordsContainer {

    public ExtendEntryNode(@NotNull ASTNode node) {
        super(node);
    }

}
