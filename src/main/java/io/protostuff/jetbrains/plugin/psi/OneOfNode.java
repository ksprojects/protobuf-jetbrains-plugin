package io.protostuff.jetbrains.plugin.psi;

import com.intellij.lang.ASTNode;
import java.util.Arrays;
import java.util.Collection;
import org.antlr.jetbrains.adapter.psi.AntlrPsiNode;
import org.jetbrains.annotations.NotNull;

/**
 * One-of node.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public class OneOfNode extends AntlrPsiNode implements KeywordsContainer {

    public OneOfNode(@NotNull ASTNode node) {
        super(node);
    }

    public Collection<MessageField> getFields() {
        MessageField[] fields = findChildrenByClass(OneofFieldNode.class);
        return Arrays.asList(fields);
    }
}
