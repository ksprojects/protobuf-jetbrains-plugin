package io.protostuff.jetbrains.plugin.psi;

import com.intellij.extapi.psi.StubBasedPsiElementBase;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.tree.IElementType;
import org.antlr.jetbrains.adapter.SymtabUtils;
import org.antlr.jetbrains.adapter.psi.ScopeNode;
import org.antlr.jetbrains.adapter.psi.Trees;
import org.jetbrains.annotations.NotNull;

/**
 * An equivalent of [AntlrPsiNode] for stub based PsiElements.
 */
public abstract class AntlrStubBasedNode<T extends StubElement> extends StubBasedPsiElementBase<T> {

    public AntlrStubBasedNode(@NotNull ASTNode node) {
        super(node);
    }

    public AntlrStubBasedNode(T stub, IStubElementType type) {
        super(stub, type);
    }

    public AntlrStubBasedNode(T stub, IElementType type, ASTNode node) {
        super(stub, type, node);
    }

    /**
     * For some reason, default impl of this only returns rule refs
     * (composite nodes in jetbrains speak) but we want ALL children.
     * Well, we don't want hidden channel stuff.
     */
    @Override
    @NotNull
    public PsiElement[] getChildren() {
        return Trees.getChildren(this);
    }

    /**
     * For this internal PSI node, look upward for our enclosing scope.
     * Start looking for a scope at our parent node so getContext()
     * returns the enclosing scope (context) when this is a ScopeNode.
     * <p>
     * From the return to scope node, you typically look for a declaration
     * by looking at its children.
     */
    @Override
    public ScopeNode getContext() {
        return SymtabUtils.getContextFor(this);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + getNode().getElementType().toString() + ")";
    }
}
