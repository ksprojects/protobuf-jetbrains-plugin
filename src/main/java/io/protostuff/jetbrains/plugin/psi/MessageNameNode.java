package io.protostuff.jetbrains.plugin.psi;

import com.intellij.lang.ASTNode;
import org.antlr.jetbrains.adapter.psi.AntlrPsiNode;
import org.jetbrains.annotations.NotNull;

/**
 * Message name node.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public class MessageNameNode extends AntlrPsiNode {

    public MessageNameNode(@NotNull ASTNode node) {
        super(node);
    }

}
