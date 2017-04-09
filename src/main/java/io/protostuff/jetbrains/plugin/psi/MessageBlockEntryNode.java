package io.protostuff.jetbrains.plugin.psi;

import com.intellij.lang.ASTNode;
import org.antlr.jetbrains.adapter.psi.AntlrPsiNode;
import org.jetbrains.annotations.NotNull;

/**
 * TODO: unused, delete.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public class MessageBlockEntryNode extends AntlrPsiNode implements KeywordsContainer {

    public MessageBlockEntryNode(@NotNull ASTNode node) {
        super(node);
    }

}