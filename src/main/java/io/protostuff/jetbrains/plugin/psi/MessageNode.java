package io.protostuff.jetbrains.plugin.psi;

import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.ItemPresentationProviders;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.IncorrectOperationException;
import io.protostuff.jetbrains.plugin.psi.stubs.DataTypeStub;
import io.protostuff.jetbrains.plugin.psi.stubs.MessageStub;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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

    @Override
    public ItemPresentation getPresentation() {
        return ItemPresentationProviders.getItemPresentation(this);
    }

    /**
     * Returns reserved field ranges for this message.
     */
    @NotNull
    public List<RangeNode> getReservedFieldRanges() {
        return Stream.of(findChildrenByClass(ReservedFieldRangesNode.class))
                .flatMap(rangesNode -> Arrays.stream(rangesNode.getRanges()))
                .collect(Collectors.toList());
    }

    /**
     * Returns reserved field names for this message.
     */
    @NotNull
    public Set<String> getReservedFieldNames() {
        return Stream.of(findChildrenByClass(ReservedFieldNamesNode.class))
                .flatMap(namesNode -> namesNode.getNames().stream())
                .collect(Collectors.toSet());
    }

    @Override
    public boolean hasSyntaxErrors() {
        if (syntaxErrors == null) {
            syntaxErrors = Util.checkForSyntaxErrors(this);
        }
        return syntaxErrors;
    }

}