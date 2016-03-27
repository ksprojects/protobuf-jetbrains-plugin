package io.protostuff.jetbrains.plugin.psi;

import com.intellij.lang.ASTNode;
import org.antlr.jetbrains.adapter.psi.ANTLRPsiNode;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public class GroupNode extends ANTLRPsiNode implements KeywordsContainer {

    public GroupNode(@NotNull ASTNode node) {
        super(node);
    }

}
