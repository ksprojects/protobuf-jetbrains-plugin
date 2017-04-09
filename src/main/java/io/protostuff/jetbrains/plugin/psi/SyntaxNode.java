package io.protostuff.jetbrains.plugin.psi;

import com.intellij.lang.ASTNode;
import org.antlr.jetbrains.adapter.psi.AntlrPsiNode;
import org.jetbrains.annotations.NotNull;

/**
 * Syntax node.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public class SyntaxNode extends AntlrPsiNode implements KeywordsContainer {

    public SyntaxNode(@NotNull ASTNode node) {
        super(node);
    }

}
