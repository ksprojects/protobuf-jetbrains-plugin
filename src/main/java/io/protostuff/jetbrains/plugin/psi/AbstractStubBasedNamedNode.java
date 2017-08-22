package io.protostuff.jetbrains.plugin.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.NamedStub;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.IncorrectOperationException;
import org.antlr.jetbrains.adapter.psi.AntlrStubBasedPsiNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Base class for named stub based nodes - messages, enums, etc.
 */
public abstract class AbstractStubBasedNamedNode<T extends NamedStub> extends
        AntlrStubBasedPsiNode<T> implements PsiNameIdentifierOwner {

    public AbstractStubBasedNamedNode(@NotNull ASTNode node) {
        super(node);
    }

    public AbstractStubBasedNamedNode(T stub, IStubElementType type) {
        super(stub, type);
    }

    public AbstractStubBasedNamedNode(T stub, IElementType type, ASTNode node) {
        super(stub, type, node);
    }

    @Nullable
    @Override
    public GenericNameNode getNameIdentifier() {
        return findChildByClass(GenericNameNode.class);
    }

    @Override
    public int getTextOffset() {
        PsiElement id = getNameIdentifier();
        return id != null ? id.getTextOffset() : super.getTextOffset();
    }

    @Override
    public String getName() {
        if (getStub() != null) {
            return getStub().getName();
        }

        GenericNameNode nameIdentifier = getNameIdentifier();
        if (nameIdentifier == null) {
            return null;
        }
        return nameIdentifier.getName();
    }

    @Override
    public PsiElement setName(@NotNull String name) throws IncorrectOperationException {
        return GenericNameNode.setName(this, name);
    }
}
