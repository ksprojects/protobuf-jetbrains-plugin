package io.protostuff.jetbrains.plugin.formatter;

import com.intellij.formatting.*;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import io.protostuff.jetbrains.plugin.ProtoLanguage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import static io.protostuff.jetbrains.plugin.formatter.StatementBlock.*;
/**
 * @author Kostiantyn Shchepanovskyi
 */
public class FormattingModelBuilder implements com.intellij.formatting.FormattingModelBuilder {

    @NotNull
    @Override
    public FormattingModel createModel(PsiElement element, CodeStyleSettings settings) {
        PsiFile containingFile = element.getContainingFile().getViewProvider().getPsi(ProtoLanguage.INSTANCE);
        ASTNode fileNode = containingFile.getNode();
        Wrap wrap = Wrap.createWrap(WrapType.NONE, false);
        Alignment alignment = Alignment.createAlignment();
        ProtoFileBlock block = new ProtoFileBlock(fileNode, wrap, alignment, settings);
        return FormattingModelProvider.createFormattingModelForPsiFile(containingFile, block, settings);
    }

    @Nullable
    @Override
    public TextRange getRangeAffectingIndent(PsiFile file, int offset, ASTNode elementAtOffset) {
        return null;
    }

    public static SpacingBuilder createSpacingBuilder(CodeStyleSettings settings) {
        CommonCodeStyleSettings protoSettings = settings.getCommonSettings(ProtoLanguage.INSTANCE);
        return new SpacingBuilder(settings, ProtoLanguage.INSTANCE)
                .around(ASSIGN).spaceIf(protoSettings.SPACE_AROUND_ASSIGNMENT_OPERATORS)
                .before(SEMICOLON).spaceIf(protoSettings.SPACE_BEFORE_SEMICOLON)
                .after(LINE_COMMENT).spacing(0, 0, 1, true, 2)
                .after(LCURLY).spacing(0, 0, 1, true, 2)
                .before(RCURLY).spacing(0, 0, 1, true, 2)
                .after(LPAREN).spacing(0, 0, 0, false, 0)
                .before(RPAREN).spacing(0, 0, 0, false, 0)
                .after(LSQUARE).spacing(0, 0, 0, false, 0)
                .before(RSQUARE).spacing(0, 0, 0, false, 0)
                .before(LT).spacing(0, 0, 0, false, 0)
                .after(LT).spacing(0, 0, 0, false, 0)
                .before(GT).spacing(0, 0, 0, false, 0)
                .before(COMMA).spacing(0, 0, 0, false, 0)
                .before(SEMICOLON).spacing(0, 0, 0, false, 0)
                .after(COMMA).spacing(1, 1, 0, false, 0);

    }
}
