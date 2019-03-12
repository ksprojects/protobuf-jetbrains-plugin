package io.protostuff.jetbrains.plugin.psi;

import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.ItemPresentationProviders;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.IncorrectOperationException;
import io.protostuff.jetbrains.plugin.Icons;
import io.protostuff.jetbrains.plugin.psi.stubs.DataTypeStub;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.swing.Icon;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Message node.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public class MessageNode extends DataType implements AntlrParserRuleNode, DataTypeContainer {

    @Nullable
    private Boolean syntaxErrors;

    public MessageNode(@NotNull ASTNode node) {
        super(node);
    }

    public MessageNode(DataTypeStub stub, IStubElementType type) {
        super(stub, type);
    }

    public MessageNode(DataTypeStub stub, IElementType type, ASTNode node) {
        super(stub, type, node);
    }

    /**
     * Returns message name.
     */
    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public PsiElement setName(@NotNull String name) throws IncorrectOperationException {
        return GenericNameNode.setName(this, name);
    }

    @Override
    public String getNamespace() {
        return getQualifiedName() + ".";
    }

    @Override
    public Collection<DataType> getDeclaredDataTypes() {
        List<DataType> types = new ArrayList<>();
        DataType[] direct = findChildrenByClass(DataType.class);
        Collections.addAll(types, direct);
        OneOfNode[] oneOfNodes = findChildrenByClass(OneOfNode.class);
        for (OneOfNode oneOfNode : oneOfNodes) {
            Collections.addAll(types, oneOfNode.getDeclaredDataTypes());
        }
        return types;
    }

    @Override
    public Collection<ExtendNode> getDeclaredExtensions() {
        return Arrays.asList(findChildrenByClass(ExtendNode.class));
    }

    /**
     * Returns all fields of this message.
     */
    public Collection<MessageField> getFields() {
        List<MessageField> result = new ArrayList<>();
        // normal fields and maps
        MessageField[] fields = findChildrenByClass(MessageField.class);
        result.addAll(Arrays.asList(fields));
        OneOfNode[] oneOfs = findChildrenByClass(OneOfNode.class);
        for (OneOfNode oneOf : oneOfs) {
            Collection<MessageField> oneOfFields = oneOf.getFields();
            result.addAll(oneOfFields);
        }
        return result;
    }

    @Nullable
    @Override
    public Icon getIcon(int flags) {
        return Icons.MESSAGE;
    }

    @Override
    public ItemPresentation getPresentation() {
        return ItemPresentationProviders.getItemPresentation(this);
    }

    @Override
    public boolean hasSyntaxErrors() {
        if (syntaxErrors == null) {
            syntaxErrors = Util.checkForSyntaxErrors(this);
        }
        return syntaxErrors;
    }

    public Collection<OneOfNode> getOneOfNodes() {
        OneOfNode[] oneOfs = findChildrenByClass(OneOfNode.class);
        return Arrays.asList(oneOfs);
    }
}