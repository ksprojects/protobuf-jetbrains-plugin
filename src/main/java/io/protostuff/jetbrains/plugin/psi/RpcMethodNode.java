package io.protostuff.jetbrains.plugin.psi;

import com.intellij.lang.ASTNode;
import io.protostuff.compiler.parser.ProtoParser;
import io.protostuff.jetbrains.plugin.ProtoParserDefinition;
import org.antlr.jetbrains.adapter.psi.IdentifierDefSubtree;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public class RpcMethodNode extends IdentifierDefSubtree implements KeywordsContainer {

    public RpcMethodNode(@NotNull ASTNode node) {
        super(node, ProtoParserDefinition.rule(ProtoParser.RULE_rpcName));
    }

}
