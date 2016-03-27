package io.protostuff.jetbrains.plugin.psi;

import com.intellij.lang.ASTNode;
import org.antlr.jetbrains.adapter.psi.ANTLRPsiNode;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public class OneOfNode extends ANTLRPsiNode implements KeywordsContainer {

    public OneOfNode(@NotNull ASTNode node) {
        super(node);
    }

}
