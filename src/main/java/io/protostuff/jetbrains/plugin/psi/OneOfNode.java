package io.protostuff.jetbrains.plugin.psi;

import com.intellij.lang.ASTNode;
import java.util.Arrays;
import java.util.Collection;
import org.jetbrains.annotations.NotNull;

/**
 * One-of node.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public class OneOfNode extends AbstractNamedNode implements KeywordsContainer {

    public OneOfNode(@NotNull ASTNode node) {
        super(node);
    }

    public Collection<MessageField> getFields() {
        MessageField[] fields = findChildrenByClass(FieldNode.class);
        return Arrays.asList(fields);
    }

    @NotNull
    public DataType[] getDeclaredDataTypes() {
        return findChildrenByClass(DataType.class);
    }

    /**
     * Returns name node, if present, null otherwise.
     */
    public ASTNode getOneofNameNode() {
        GenericNameNode nameIdentifier = getNameIdentifier();
        if (nameIdentifier == null) {
            return null;
        }
        return nameIdentifier.getNode();
    }
}
