package io.protostuff.jetbrains.plugin;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.psi.PsiElement;
import io.protostuff.jetbrains.plugin.psi.MessageBlockSubtree;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public class ProtoSyntaxAnnotator implements Annotator {

    private static void setHighlighting(@NotNull PsiElement element, @NotNull AnnotationHolder holder,
                                        @NotNull TextAttributesKey key) {
        holder.createInfoAnnotation(element, null).setEnforcedTextAttributes(TextAttributes.ERASE_MARKER);
        holder.createInfoAnnotation(element, null).setEnforcedTextAttributes(
                EditorColorsManager.getInstance().getGlobalScheme().getAttributes(key));
    }

    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        if (element instanceof MessageBlockSubtree) {
            MessageBlockSubtree literalExpression = (MessageBlockSubtree) element;

//            System.out.println(literalExpression);

            PsiElement message = (PsiElement) literalExpression.getNode().getFirstChildNode();
            setHighlighting(message, holder, ProtoSyntaxHighlighter.KEYWORD);
//            element.accept(new SimpleVisitor() {
//                @Override
//                public void visitCommand(@NotNull SimpleCommand o) {
//                    super.visitCommand(o);
//                    setHighlighting(o, holder, SimpleSyntaxHighlighter.COMMAND);
//                }
//            });
//            String value = literalExpression.getValue() instanceof String ? (String) literalExpression.getValue() : null;
//
//            if (value != null && value.startsWith("simple" + ":")) {
//                Project project = element.getProject();
//                String key = value.substring(7);
//                List<SimpleProperty> properties = SimpleUtil.findProperties(project, key);
//                if (properties.size() == 1) {
//                    TextRange range = new TextRange(element.getTextRange().getStartOffset() + 7,
//                            element.getTextRange().getStartOffset() + 7);
//                    Annotation annotation = holder.createInfoAnnotation(range, null);
//                    annotation.setTextAttributes(DefaultLanguageHighlighterColors.LINE_COMMENT);
//                } else if (properties.size() == 0) {
//                    TextRange range = new TextRange(element.getTextRange().getStartOffset() + 8,
//                            element.getTextRange().getEndOffset());
//                    holder.createErrorAnnotation(range, "Unresolved property").
//                            registerFix(new CreatePropertyQuickFix(key));
//                }
//            }
//        }
        }
    }
}
