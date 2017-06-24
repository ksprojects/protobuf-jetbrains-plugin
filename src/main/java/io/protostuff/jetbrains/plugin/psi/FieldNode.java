package io.protostuff.jetbrains.plugin.psi;

import static io.protostuff.jetbrains.plugin.ProtoParserDefinition.R_FIELD_MODIFIER;
import static io.protostuff.jetbrains.plugin.ProtoParserDefinition.R_FIELD_NAME;
import static io.protostuff.jetbrains.plugin.ProtoParserDefinition.R_TAG;
import static io.protostuff.jetbrains.plugin.psi.Util.decodeIntegerFromText;

import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.ItemPresentationProviders;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiElement;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

/**
 * Field node.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public class FieldNode extends AbstractNamedNode implements KeywordsContainer, MessageField {

    private static final Logger LOGGER = Logger.getInstance(FieldNode.class);

    public FieldNode(@NotNull ASTNode node) {
        super(node);
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

    public TypeReferenceNode getFieldType() {
        return findChildByClass(TypeReferenceNode.class);
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

    @Override
    public Optional<FieldLabel> getFieldLabel() {
        ASTNode label = getFieldLabelNode();
        if (label == null) {
            return Optional.empty();
        }
        return FieldLabel.forString(label.getText());
    }

    @Override
    public ASTNode getFieldLabelNode() {
        ASTNode node = getNode();
        return node.findChildByType(R_FIELD_MODIFIER);
    }

    @Override
    public ItemPresentation getPresentation() {
        return ItemPresentationProviders.getItemPresentation(this);
    }

    @Override
    public String toString() {
        return "FieldNode(" + getFieldName() + "=" + getTag() + ")";
    }
}
