package io.protostuff.jetbrains.plugin.formatter;

import com.intellij.formatting.*;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import io.protostuff.jetbrains.plugin.ProtoLanguage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public class ProtoFormatterModelBuilder implements FormattingModelBuilder {

    @NotNull
    @Override
    public FormattingModel createModel(PsiElement element, CodeStyleSettings settings) {
        ASTNode node = element.getNode();
        PsiFile containingFile = element.getContainingFile().getViewProvider().getPsi(ProtoLanguage.INSTANCE);
        ASTNode fileNode = containingFile.getNode();
        Wrap wrap = Wrap.createWrap(WrapType.NONE, false);
        Alignment alignment = Alignment.createAlignment();
        ProtoFileBlock block = new ProtoFileBlock(fileNode, wrap, alignment);
        return FormattingModelProvider.createFormattingModelForPsiFile(containingFile, block, settings);
    }

    @Nullable
    @Override
    public TextRange getRangeAffectingIndent(PsiFile file, int offset, ASTNode elementAtOffset) {
        return null;
    }
}
