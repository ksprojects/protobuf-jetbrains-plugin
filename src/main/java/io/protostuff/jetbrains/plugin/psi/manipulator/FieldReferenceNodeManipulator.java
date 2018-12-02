package io.protostuff.jetbrains.plugin.psi.manipulator;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.AbstractElementManipulator;
import com.intellij.util.IncorrectOperationException;
import io.protostuff.jetbrains.plugin.psi.FieldReferenceNode;
import io.protostuff.jetbrains.plugin.psi.ProtoElementFactory;
import org.jetbrains.annotations.NotNull;

/**
 * Field reference node manipulator.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public class FieldReferenceNodeManipulator extends AbstractElementManipulator<FieldReferenceNode> {

    @Override
    public FieldReferenceNode handleContentChange(@NotNull FieldReferenceNode node,
            @NotNull TextRange range, String newContent) throws IncorrectOperationException {
        String oldText = node.getText();
        String newText = oldText.substring(0, range.getStartOffset()) + newContent + oldText
                .substring(range.getEndOffset());
        Project project = node.getProject();
        ProtoElementFactory elementFactory = project.getComponent(ProtoElementFactory.class);
        FieldReferenceNode newNode = elementFactory.createFieldReferenceNode(newText);
        return (FieldReferenceNode) node.replace(newNode);
    }
}
