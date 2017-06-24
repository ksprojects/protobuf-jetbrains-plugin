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
}
