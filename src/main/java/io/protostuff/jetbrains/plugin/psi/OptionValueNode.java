package io.protostuff.jetbrains.plugin.psi;

import com.intellij.lang.ASTNode;
import org.antlr.jetbrains.adapter.psi.AntlrPsiNode;
import org.jetbrains.annotations.NotNull;

/**
 * Option value node.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public class OptionValueNode extends AntlrPsiNode implements KeywordsContainer {

    public OptionValueNode(@NotNull ASTNode node) {
        super(node);
    }

}
