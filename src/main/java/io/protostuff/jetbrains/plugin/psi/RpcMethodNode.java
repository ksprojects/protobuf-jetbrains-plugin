package io.protostuff.jetbrains.plugin.psi;

import static io.protostuff.compiler.parser.ProtoParser.RULE_rpcName;
import static io.protostuff.jetbrains.plugin.ProtoParserDefinition.rule;

import com.intellij.lang.ASTNode;
import io.protostuff.jetbrains.plugin.Icons;
import javax.swing.Icon;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Rpc method node.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public class RpcMethodNode extends AbstractNamedNode implements KeywordsContainer {

    public RpcMethodNode(@NotNull ASTNode node) {
        super(node);
    }

    /**
     * Returns method name.
     */
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

    @Nullable
    @Override
    public Icon getIcon(int flags) {
        return Icons.ENUM;
    }
    
}
