package io.protostuff.jetbrains.plugin.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import org.antlr.jetbrains.adapter.psi.IdentifierDefSubtree;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

import static io.protostuff.compiler.parser.ProtoParser.RULE_rpcName;
import static io.protostuff.jetbrains.plugin.ProtoParserDefinition.rule;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public class RpcMethodNode extends IdentifierDefSubtree implements KeywordsContainer {

    public RpcMethodNode(@NotNull ASTNode node) {
        super(node, rule(RULE_rpcName));
    }

    @NotNull
    public String getMethodName() {
        ASTNode nameNode = getMethodNameNode();
        if (nameNode != null) {
            return nameNode.getText();
        }
        return "";
    }

    @Nullable
    public ASTNode getMethodNameNode() {
        ASTNode node = getNode();
        return node.findChildByType(rule(RULE_rpcName));
    }

    @NotNull
    @Override
    public Collection<PsiElement> keywords() {
        return Util.findKeywords(getNode());
    }
}
