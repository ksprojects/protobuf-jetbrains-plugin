package io.protostuff.jetbrains.plugin.psi;

import com.intellij.lang.ASTNode;
import org.antlr.jetbrains.adapter.psi.ANTLRPsiNode;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public class TypeReferenceNode extends ANTLRPsiNode implements KeywordsContainer {

    public TypeReferenceNode(@NotNull ASTNode node) {
        super(node);
    }

}