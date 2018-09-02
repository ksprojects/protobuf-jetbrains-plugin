package io.protostuff.jetbrains.plugin.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiErrorElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.IncorrectOperationException;
import io.protostuff.jetbrains.plugin.psi.stubs.DataTypeStub;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.antlr.jetbrains.adapter.psi.ScopeNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * User-defined proto type that can be used as a field - message or enum.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public class DataType
        extends AbstractStubBasedNamedNode<DataTypeStub>
        implements ScopeNode, KeywordsContainer, ProtoType {

    private static final String ERROR_ELEMENT = ".__ERROR_ELEMENT";

    public DataType(@NotNull ASTNode node) {
        super(node);
    }

    public DataType(DataTypeStub stub, IStubElementType type) {
        super(stub, type);
    }

    public DataType(DataTypeStub stub, IElementType type, ASTNode node) {
        super(stub, type, node);
    }

    /**
     * Returns fully qualified name of this message (starting with dot).
     */
    @NotNull
    public String getQualifiedName() {
        PsiElement parent = getParent();
        if (parent instanceof ProtoRootNode) {
            ProtoRootNode proto = (ProtoRootNode) parent;
            String packageName = proto.getPackageName();
            if (packageName.isEmpty()) {
                return "." + getName();
            }
            return "." + packageName + "." + getName();
        }
        if (parent instanceof OneOfNode) {
            OneOfNode oneOfNode = (OneOfNode) parent;
            MessageNode parentMessage = (MessageNode) oneOfNode.getParent();
            String parentMessageQualifiedName = parentMessage.getQualifiedName();
            return parentMessageQualifiedName + "." + getName();
        }
        if (parent instanceof MessageNode) {
            MessageNode parentMessage = (MessageNode) parent;
            String parentMessageQualifiedName = parentMessage.getQualifiedName();
            return parentMessageQualifiedName + "." + getName();
        }
        if (parent instanceof PsiErrorElement) {
            return ERROR_ELEMENT;
        }
        if (parent instanceof ExtendEntryNode) {
            ExtendEntryNode parentExtendEntry = (ExtendEntryNode) parent;
            ExtendNode parentExtendNode = (ExtendNode) parentExtendEntry.getParent();
            PsiElement extendNodeParent = parentExtendNode.getParent();
            String name = String.valueOf(getName());
            if (extendNodeParent instanceof MessageNode) {
                MessageNode parentMessage = (MessageNode) extendNodeParent;
                String parentMessageQualifiedName = parentMessage.getQualifiedName();
                return parentMessageQualifiedName + "." + name;
            }
            if (extendNodeParent instanceof ProtoRootNode) {
                ProtoRootNode proto = (ProtoRootNode) extendNodeParent;
                String packageName = proto.getPackageName();
                if (packageName.isEmpty()) {
                    return "." + name;
                }
                return "." + packageName + "." + name;
            }
        }
        throw new IncorrectOperationException("Could not detect qualified name in given context: "
                + parent.getClass().getName());
    }

    /**
     * Returns full name of this type without leading dot.
     */
    @NotNull
    public String getFullName() {
        if (getStub() != null) {
            return getStub().getFullName();
        }

        return getQualifiedName().substring(1);
    }

    /**
     * Returns reserved field ranges for this message.
     */
    @NotNull
    public List<RangeNode> getReservedRanges() {
        return Stream.of(findChildrenByClass(ReservedFieldRangesNode.class))
                .flatMap(rangesNode -> Arrays.stream(rangesNode.getRanges()))
                .collect(Collectors.toList());
    }

    /**
     * Returns reserved field names for this message.
     */
    @NotNull
    public Set<String> getReservedNames() {
        return Stream.of(findChildrenByClass(ReservedFieldNamesNode.class))
                .flatMap(namesNode -> namesNode.getNames().stream())
                .collect(Collectors.toSet());
    }

    @Nullable
    @Override
    public PsiElement resolve(PsiNamedElement element) {
        return null;
    }
}
