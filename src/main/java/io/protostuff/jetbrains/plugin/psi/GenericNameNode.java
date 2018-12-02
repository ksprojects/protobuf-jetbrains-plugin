package io.protostuff.jetbrains.plugin.psi;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.util.IncorrectOperationException;
import org.antlr.jetbrains.adapter.psi.AntlrPsiNode;
import org.jetbrains.annotations.NotNull;

/**
 * Generic name node.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public class GenericNameNode extends AntlrPsiNode {

    public static final String OPERATION_NOT_SUPPORTED = "Rename is not supported for this element";

    public GenericNameNode(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public String getName() {
        return getText();
    }

    public ASTNode identNode() {
        return getFirstChild().getNode();
    }

    /**
     * Set name for a given element if it implements {@link PsiNameIdentifierOwner} and
     * {@link PsiNameIdentifierOwner#getNameIdentifier()} resolves to an instance of
     * {@link GenericNameNode}.
     */
    public static PsiElement setName(PsiNameIdentifierOwner element, String name) {
        PsiElement nameIdentifier = element.getNameIdentifier();
        if (nameIdentifier instanceof GenericNameNode) {
            GenericNameNode nameNode = (GenericNameNode) nameIdentifier;
            return nameNode.setName(name);
        }
        throw new IncorrectOperationException(OPERATION_NOT_SUPPORTED);
    }

    /**
     * Set name.
     */
    public PsiElement setName(@NotNull String name) throws IncorrectOperationException {
        Project project = getProject();
        ProtoElementFactory elementFactory = project.getComponent(ProtoElementFactory.class);
        GenericNameNode newNameNode = elementFactory.createGenericNameNode(name);
        identNode().replaceChild(identNode().getFirstChildNode(), newNameNode.identNode().getFirstChildNode());
        return this;
    }

}
