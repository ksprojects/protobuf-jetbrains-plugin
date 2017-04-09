package io.protostuff.jetbrains.plugin.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiErrorElement;
import com.intellij.psi.PsiRecursiveElementVisitor;
import com.intellij.psi.tree.IElementType;
import io.protostuff.jetbrains.plugin.ProtoParserDefinition;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * PSI utilities.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public class Util {

    private Util() {
        throw new IllegalAccessError("Utility class");
    }


    /**
     * Get all children nodes for given element.
     */
    public static Collection<PsiElement> findChildren(ASTNode parent, IElementType elementType) {
        ASTNode message = parent.findChildByType(elementType);
        PsiElement psiElement = (PsiElement) message;
        return Collections.singleton(psiElement);
    }

    /**
     * Find all elements for given node that are keywords.
     */
    public static Collection<PsiElement> findKeywords(ASTNode parent) {
        ASTNode[] keywords = parent.getChildren(ProtoParserDefinition.KEYWORDS);
        List<PsiElement> result = new ArrayList<>(keywords.length);
        for (ASTNode keyword : keywords) {
            result.add((PsiElement) keyword);
        }
        return result;
    }

    /**
     * Check given element recursively for syntax errors.
     */
    public static boolean checkForSyntaxErrors(PsiElement element) {
        final boolean[] hasErrors = {false};
        PsiRecursiveElementVisitor visitor = new PsiRecursiveElementVisitor() {
            @Override
            public void visitErrorElement(PsiErrorElement element) {
                hasErrors[0] = true;
            }
        };
        visitor.visitElement(element);
        return hasErrors[0];
    }

    /**
     * Parse node text as integer number.
     * Returns 0 if node text is not a number.
     */
    public static int decodeIntegerFromText(ASTNode node) {
        try {
            if (node != null) {
                String text = node.getText();
                return Integer.decode(text);
            }
        } catch (Exception e) {
            return 0;
        }
        return 0;
    }

    /**
     * Parse node text as integer number.
     * Returns 0 if node text is not a number.
     */
    public static int decodeIntegerFromText(PsiElement node) {
        try {
            if (node != null) {
                String text = node.getText();
                return Integer.decode(text);
            }
        } catch (Exception e) {
            return 0;
        }
        return 0;
    }

}
