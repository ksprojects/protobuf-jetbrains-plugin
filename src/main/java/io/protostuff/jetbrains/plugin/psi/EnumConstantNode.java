package io.protostuff.jetbrains.plugin.psi;

import static io.protostuff.compiler.parser.ProtoParser.RULE_enumFieldName;
import static io.protostuff.compiler.parser.ProtoParser.RULE_enumFieldValue;
import static io.protostuff.jetbrains.plugin.ProtoParserDefinition.rule;
import static io.protostuff.jetbrains.plugin.psi.Util.decodeIntegerFromText;

import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.ItemPresentationProviders;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import io.protostuff.jetbrains.plugin.Icons;
import javax.swing.Icon;
import org.antlr.jetbrains.adapter.psi.ScopeNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Enum constant node.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public class EnumConstantNode
        extends AbstractNamedNode
        implements ScopeNode {

    public EnumConstantNode(@NotNull ASTNode node) {
        super(node);
    }

    @Nullable
    @Override
    public PsiElement resolve(PsiNamedElement element) {
        return null;
    }

    /**
     * Get enum constant name.
     */
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

    @Nullable
    @Override
    public Icon getIcon(int flags) {
        return Icons.CONSTANT;
    }

    @Override
    public ItemPresentation getPresentation() {
        return ItemPresentationProviders.getItemPresentation(this);
    }

}