package io.protostuff.jetbrains.plugin.psi;

import com.intellij.lang.ASTNode;
import org.antlr.jetbrains.adapter.psi.ANTLRPsiNode;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public class OptionValueNode extends ANTLRPsiNode implements KeywordsContainer {

    public OptionValueNode(@NotNull ASTNode node) {
        super(node);
    }

}
