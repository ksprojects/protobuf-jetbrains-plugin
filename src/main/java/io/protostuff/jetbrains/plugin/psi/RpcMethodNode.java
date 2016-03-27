package io.protostuff.jetbrains.plugin.psi;

import com.intellij.lang.ASTNode;
import org.antlr.jetbrains.adapter.psi.ANTLRPsiNode;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public class RpcMethodNode extends ANTLRPsiNode implements KeywordsContainer {

    public RpcMethodNode(@NotNull ASTNode node) {
        super(node);
    }

}
