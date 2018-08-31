package io.protostuff.jetbrains.plugin.psi;

import static io.protostuff.jetbrains.plugin.ProtoParserDefinition.R_FIELD_MODIFIER;
import static io.protostuff.jetbrains.plugin.ProtoParserDefinition.R_GROUP_NAME;
import static io.protostuff.jetbrains.plugin.ProtoParserDefinition.R_TAG;
import static io.protostuff.jetbrains.plugin.psi.Util.decodeIntegerFromText;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.tree.IElementType;
import io.protostuff.jetbrains.plugin.psi.stubs.GroupStub;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

/**
 * Group node.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public class GroupNode extends MessageNode implements KeywordsContainer, MessageField {

    public GroupNode(@NotNull ASTNode node) {
        super(node);
    }

    public GroupNode(GroupStub stub, IStubElementType type) {
        super(stub, type);
    }

    public GroupNode(GroupStub stub, IElementType type, ASTNode node) {
        super(stub, type, node);
    }

    @Override
    public Collection<PsiElement> keywords() {
        ASTNode node = getNode();
        List<PsiElement> result = new ArrayList<>();
        result.addAll(Util.findKeywords(getNode()));
        ASTNode fieldModifier = node.findChildByType(R_FIELD_MODIFIER);
        if (fieldModifier != null) {
            result.addAll(Util.findKeywords(fieldModifier));
        }
        return result;
    }

    @Override
    public String getFieldName() {
        ASTNode nameNode = getFieldNameNode();
        if (nameNode != null) {
            String text = nameNode.getText();
            return text.toLowerCase();
        }
        return "";
    }

    @Override
    public ASTNode getFieldNameNode() {
        ASTNode node = getNode();
        return node.findChildByType(R_GROUP_NAME);
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
    public TypeReferenceNode getFieldType() {
        return null;
    }
}
