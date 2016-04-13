package io.protostuff.jetbrains.plugin.psi;

import com.intellij.lang.ASTNode;
import org.antlr.jetbrains.adapter.psi.ANTLRPsiNode;
import org.antlr.jetbrains.adapter.psi.IdentifierDefSubtree;
import org.jetbrains.annotations.NotNull;

import static io.protostuff.jetbrains.plugin.ProtoParserDefinition.R_NAME;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public class RpcMethodNode extends IdentifierDefSubtree implements KeywordsContainer {

    public RpcMethodNode(@NotNull ASTNode node) {
        super(node, R_NAME);
    }

}
