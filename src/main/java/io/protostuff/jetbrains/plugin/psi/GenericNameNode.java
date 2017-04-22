package io.protostuff.jetbrains.plugin.psi;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.psi.impl.PsiFileFactoryImpl;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.IncorrectOperationException;
import io.protostuff.jetbrains.plugin.ProtoLanguage;
import io.protostuff.jetbrains.plugin.ProtoParserDefinition;
import org.antlr.jetbrains.adapter.lexer.RuleIElementType;
import org.antlr.jetbrains.adapter.psi.AntlrPsiNode;
import org.antlr.jetbrains.adapter.psi.ScopeNode;
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
        ASTNode node = getNode();
        IElementType elementType = node.getElementType();
        if (elementType instanceof RuleIElementType) {
            RuleIElementType ruleElementType = (RuleIElementType) elementType;
            int ruleIndex = ruleElementType.getRuleIndex();
            Project project = getProject();
            PsiFileFactoryImpl factory = (PsiFileFactoryImpl) PsiFileFactory.getInstance(project);
            RuleIElementType type = ProtoParserDefinition.rule(ruleIndex);
            ScopeNode context = getContext();
            PsiElement newNode = factory.createElementFromText(name, ProtoLanguage.INSTANCE, type, context);
            if (newNode == null) {
                throw new IncorrectOperationException();
            }
            return replace(newNode);
        }
        throw new IncorrectOperationException(OPERATION_NOT_SUPPORTED);
    }

}
