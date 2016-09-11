package io.protostuff.jetbrains.plugin.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import org.antlr.jetbrains.adapter.psi.ANTLRPsiNode;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public class OneOfNode extends ANTLRPsiNode implements KeywordsContainer {

    public OneOfNode(@NotNull ASTNode node) {
        super(node);
    }

    public Collection<MessageField> getFields() {
        MessageField[] fields = findChildrenByClass(OneofFieldNode.class);
        return Arrays.asList(fields);
    }

    @NotNull
    @Override
    public Collection<PsiElement> keywords() {
        return Util.findKeywords(getNode());
    }
}
