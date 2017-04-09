package io.protostuff.jetbrains.plugin.psi;

import static io.protostuff.jetbrains.plugin.ProtoParserDefinition.R_FIELD_MODIFIER;
import static io.protostuff.jetbrains.plugin.ProtoParserDefinition.R_FIELD_NAME;
import static io.protostuff.jetbrains.plugin.ProtoParserDefinition.R_TAG;
import static io.protostuff.jetbrains.plugin.psi.Util.decodeIntegerFromText;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiElement;
import io.protostuff.compiler.parser.ProtoParser;
import io.protostuff.jetbrains.plugin.ProtoParserDefinition;
import java.util.Collection;
import java.util.Collections;
import org.antlr.jetbrains.adapter.psi.IdentifierDefSubtree;
import org.jetbrains.annotations.NotNull;

/**
 * One-of field node.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public class OneofFieldNode extends IdentifierDefSubtree implements KeywordsContainer, MessageField {

    private static final Logger LOGGER = Logger.getInstance(OneofFieldNode.class);

    public OneofFieldNode(@NotNull ASTNode node) {
        super(node, ProtoParserDefinition.rule(ProtoParser.RULE_fieldName));
    }

    @Override
    public Collection<PsiElement> keywords() {
        ASTNode node = getNode();
        ASTNode fieldModifier = node.findChildByType(R_FIELD_MODIFIER);
        if (fieldModifier != null) {
            return Util.findKeywords(fieldModifier);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public String getFieldName() {
        ASTNode nameNode = getFieldNameNode();
        if (nameNode != null) {
            return nameNode.getText();
        }
        return "";
    }

    @Override
    public ASTNode getFieldNameNode() {
        ASTNode node = getNode();
        return node.findChildByType(R_FIELD_NAME);
    }

    @Override
    public int getTag() {
        ASTNode tagNode = getTagNode();
        return decodeIntegerFromText(tagNode);
    }

    @Override
    public ASTNode getTagNode() {
        ASTNode node = getNode();
        return node.findChildByType(R_TAG);
    }

}
