package io.protostuff.jetbrains.plugin.psi;

import com.intellij.lang.ASTNode;
import org.antlr.jetbrains.adapter.psi.ANTLRPsiNode;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public class ProtoRootNode extends ANTLRPsiNode implements KeywordsContainer {

    public ProtoRootNode(@NotNull ASTNode node) {
        super(node);
    }

}