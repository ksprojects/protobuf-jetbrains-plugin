package io.protostuff.jetbrains.plugin.psi.manipulator;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.AbstractElementManipulator;
import com.intellij.util.IncorrectOperationException;
import io.protostuff.jetbrains.plugin.psi.FileReferenceNode;
import io.protostuff.jetbrains.plugin.psi.ProtoElementFactory;
import org.jetbrains.annotations.NotNull;

/**
 * File reference node manipulator. Use for changing text of import nodes when
 * renaming proto files.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public class FileReferenceNodeManipulator extends AbstractElementManipulator<FileReferenceNode> {

    @Override
    public FileReferenceNode handleContentChange(@NotNull FileReferenceNode fileReferenceNode,
            @NotNull TextRange range, String newContent) throws IncorrectOperationException {
        String oldText = fileReferenceNode.getText();
        String newText = oldText.substring(0, range.getStartOffset()) + newContent + oldText
                .substring(range.getEndOffset());
        Project project = fileReferenceNode.getProject();
        ProtoElementFactory elementFactory = project.getComponent(ProtoElementFactory.class);
        FileReferenceNode newNode = elementFactory.createFileReferenceNode(newText);
        return (FileReferenceNode) fileReferenceNode.replace(newNode);
    }
}
