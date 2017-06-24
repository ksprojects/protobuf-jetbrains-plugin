package io.protostuff.jetbrains.plugin.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.util.IncorrectOperationException;
import org.antlr.jetbrains.adapter.psi.AntlrPsiNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Base class for named nodes - messages, enums, fields, etc.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public abstract class AbstractNamedNode extends AntlrPsiNode implements PsiNameIdentifierOwner {


    public AbstractNamedNode(@NotNull ASTNode node) {
        super(node);
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
