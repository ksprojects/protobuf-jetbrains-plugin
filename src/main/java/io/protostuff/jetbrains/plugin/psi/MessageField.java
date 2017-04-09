package io.protostuff.jetbrains.plugin.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;

/**
 * Message field node.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public interface MessageField extends PsiElement {

    String getFieldName();

    ASTNode getFieldNameNode();

    int getTag();

    ASTNode getTagNode();

    TypeReferenceNode getFieldType();
}
