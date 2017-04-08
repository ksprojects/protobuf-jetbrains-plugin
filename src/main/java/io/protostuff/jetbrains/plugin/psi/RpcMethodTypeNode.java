package io.protostuff.jetbrains.plugin.psi;

import com.intellij.lang.ASTNode;
import org.antlr.jetbrains.adapter.psi.AntlrPsiNode;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public class RpcMethodTypeNode extends AntlrPsiNode implements KeywordsContainer {

    public RpcMethodTypeNode(@NotNull ASTNode node) {
        super(node);
    }

}
