package io.protostuff.jetbrains.plugin.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiReference;
import io.protostuff.jetbrains.plugin.reference.TypeReference;
import org.antlr.jetbrains.adapter.psi.ANTLRPsiNode;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public class MessageNameNode extends ANTLRPsiNode {

    public MessageNameNode(@NotNull ASTNode node) {
        super(node);
    }

}
