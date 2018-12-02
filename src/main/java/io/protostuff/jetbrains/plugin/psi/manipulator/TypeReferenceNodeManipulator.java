package io.protostuff.jetbrains.plugin.psi.manipulator;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.AbstractElementManipulator;
import com.intellij.util.IncorrectOperationException;
import io.protostuff.jetbrains.plugin.psi.ProtoElementFactory;
import io.protostuff.jetbrains.plugin.psi.TypeReferenceNode;
import org.jetbrains.annotations.NotNull;

/**
 * Type reference node manipulator.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public class TypeReferenceNodeManipulator extends AbstractElementManipulator<TypeReferenceNode> {

    @Override
    public TypeReferenceNode handleContentChange(@NotNull TypeReferenceNode node,
            @NotNull TextRange range, String newContent) throws IncorrectOperationException {
        String oldText = node.getText();
        String newText = oldText.substring(0, range.getStartOffset()) + newContent + oldText
                .substring(range.getEndOffset());
        Project project = node.getProject();
        ProtoElementFactory elementFactory = project.getComponent(ProtoElementFactory.class);
        TypeReferenceNode newNode = elementFactory.crateTypeReferenceNode(newText);
        return (TypeReferenceNode) node.replace(newNode);
    }
}
