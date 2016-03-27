package io.protostuff.jetbrains.plugin.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import io.protostuff.jetbrains.plugin.ProtoParserDefinition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public class Util {

    private Util() {
    }

    ;

    public static Collection<PsiElement> findChildren(ASTNode parent, IElementType elementType) {
        ASTNode message = parent.findChildByType(elementType);
        PsiElement psiElement = (PsiElement) message;
        return Collections.singleton(psiElement);
    }

    ;

    public static Collection<PsiElement> findKeywords(ASTNode parent) {
        ASTNode[] keywords = parent.getChildren(ProtoParserDefinition.KEYWORDS);
        List<PsiElement> result = new ArrayList<>(keywords.length);
        for (ASTNode keyword : keywords) {
            result.add((PsiElement) keyword);
        }
        return result;
    }

    ;

}
