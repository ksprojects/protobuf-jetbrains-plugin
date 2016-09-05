package io.protostuff.jetbrains.plugin.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import io.protostuff.compiler.parser.ProtoParser;
import io.protostuff.jetbrains.plugin.ProtoParserDefinition;
import org.antlr.jetbrains.adapter.psi.IdentifierDefSubtree;
import org.antlr.jetbrains.adapter.psi.ScopeNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static io.protostuff.compiler.parser.ProtoParser.RULE_enumFieldName;
import static io.protostuff.compiler.parser.ProtoParser.RULE_enumFieldValue;
import static io.protostuff.jetbrains.plugin.ProtoParserDefinition.R_FIELD_NAME;
import static io.protostuff.jetbrains.plugin.ProtoParserDefinition.rule;
import static io.protostuff.jetbrains.plugin.psi.Util.decodeIntegerFromText;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public class EnumConstantNode
        extends IdentifierDefSubtree
        implements ScopeNode {

    public EnumConstantNode(@NotNull ASTNode node) {
        super(node, rule(ProtoParser.RULE_enumFieldName));
    }

    @Nullable
    @Override
    public PsiElement resolve(PsiNamedElement element) {
        return null;
    }

    public String getConstantName() {
        ASTNode nameNode = getConstantNameNode();
        if (nameNode != null) {
            return nameNode.getText();
        }
        return "";
    }

    public ASTNode getConstantNameNode() {
        ASTNode node = getNode();
        return node.findChildByType(rule(RULE_enumFieldName));
    }

    public int getConstantValue() {
        ASTNode node = getConstantValueNode();
        return decodeIntegerFromText(node);
    }

    public ASTNode getConstantValueNode() {
        ASTNode node = getNode();
        return node.findChildByType(rule(RULE_enumFieldValue));
    }

}