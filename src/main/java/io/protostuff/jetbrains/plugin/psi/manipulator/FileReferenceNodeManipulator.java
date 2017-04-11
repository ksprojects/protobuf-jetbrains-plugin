package io.protostuff.jetbrains.plugin.psi.manipulator;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.AbstractElementManipulator;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.impl.PsiFileFactoryImpl;
import com.intellij.util.IncorrectOperationException;
import io.protostuff.compiler.parser.ProtoParser;
import io.protostuff.jetbrains.plugin.ProtoLanguage;
import io.protostuff.jetbrains.plugin.ProtoParserDefinition;
import io.protostuff.jetbrains.plugin.psi.FileReferenceNode;
import org.antlr.jetbrains.adapter.lexer.RuleIElementType;
import org.antlr.jetbrains.adapter.psi.ScopeNode;
import org.jetbrains.annotations.NotNull;

/**
 * File reference node manipulator. Use for changing text of import nodes when
 * renaming proto files.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public class FileReferenceNodeManipulator extends AbstractElementManipulator<FileReferenceNode> {

    @Override
    public FileReferenceNode handleContentChange(@NotNull FileReferenceNode fileReferenceNode, @NotNull TextRange range, String newContent) throws IncorrectOperationException {
        String oldText = fileReferenceNode.getText();
        String newText = oldText.substring(0, range.getStartOffset()) + newContent + oldText.substring(range.getEndOffset());
        Project project = fileReferenceNode.getProject();
        PsiFileFactoryImpl factory = (PsiFileFactoryImpl) PsiFileFactory.getInstance(project);
        RuleIElementType type = ProtoParserDefinition.rule(ProtoParser.RULE_fileReference);
        ScopeNode context = fileReferenceNode.getContext();
        PsiElement newNode = factory.createElementFromText(newText, ProtoLanguage.INSTANCE, type, context);
        if (newNode == null) {
            throw new IncorrectOperationException();
        }
        return (FileReferenceNode) fileReferenceNode.replace(newNode);
    }
}
