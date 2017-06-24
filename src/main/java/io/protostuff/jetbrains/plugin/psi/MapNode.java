package io.protostuff.jetbrains.plugin.psi;

import static io.protostuff.jetbrains.plugin.ProtoParserDefinition.R_FIELD_NAME;
import static io.protostuff.jetbrains.plugin.ProtoParserDefinition.R_TAG;
import static io.protostuff.jetbrains.plugin.psi.Util.decodeIntegerFromText;

import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.ItemPresentationProviders;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

/**
 * Map node.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public class MapNode extends AbstractNamedNode
        implements MessageField, KeywordsContainer {

    public MapNode(@NotNull ASTNode node) {
        super(node);
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

    @Override
    public TypeReferenceNode getFieldType() {
        // TODO
        return null;
    }

    @Override
    public Optional<FieldLabel> getFieldLabel() {
        return Optional.empty();
    }

    @Override
    public ASTNode getFieldLabelNode() {
        return null;
    }

    @Override
    public ItemPresentation getPresentation() {
        return ItemPresentationProviders.getItemPresentation(this);
    }

}
