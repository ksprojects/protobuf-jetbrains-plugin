package io.protostuff.jetbrains.plugin.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import java.util.Optional;

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

    Optional<FieldLabel> getFieldLabel();

    ASTNode getFieldLabelNode();

    TypeReferenceNode getFieldType();
}
