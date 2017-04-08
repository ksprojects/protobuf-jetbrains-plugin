package io.protostuff.jetbrains.plugin.formatter;

import static io.protostuff.jetbrains.plugin.formatter.StatementBlock.ASSIGN;
import static io.protostuff.jetbrains.plugin.formatter.StatementBlock.COMMA;
import static io.protostuff.jetbrains.plugin.formatter.StatementBlock.GT;
import static io.protostuff.jetbrains.plugin.formatter.StatementBlock.LCURLY;
import static io.protostuff.jetbrains.plugin.formatter.StatementBlock.LINE_COMMENT;
import static io.protostuff.jetbrains.plugin.formatter.StatementBlock.LPAREN;
import static io.protostuff.jetbrains.plugin.formatter.StatementBlock.LSQUARE;
import static io.protostuff.jetbrains.plugin.formatter.StatementBlock.LT;
import static io.protostuff.jetbrains.plugin.formatter.StatementBlock.RCURLY;
import static io.protostuff.jetbrains.plugin.formatter.StatementBlock.RPAREN;
import static io.protostuff.jetbrains.plugin.formatter.StatementBlock.RSQUARE;
import static io.protostuff.jetbrains.plugin.formatter.StatementBlock.SEMICOLON;

import com.intellij.formatting.Alignment;
import com.intellij.formatting.FormattingModel;
import com.intellij.formatting.FormattingModelProvider;
import com.intellij.formatting.SpacingBuilder;
import com.intellij.formatting.Wrap;
import com.intellij.formatting.WrapType;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import io.protostuff.jetbrains.plugin.ProtoLanguage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public class FormattingModelBuilder implements com.intellij.formatting.FormattingModelBuilder {

    public static SpacingBuilder createSpacingBuilder(CodeStyleSettings settings) {
        CommonCodeStyleSettings protoSettings = settings.getCommonSettings(ProtoLanguage.INSTANCE);
        SpacingBuilder builder = new SpacingBuilder(settings, ProtoLanguage.INSTANCE);
        builder.around(ASSIGN).spacing(1, 1, 0, false, 0);
        builder.before(SEMICOLON).spacing(0, 0, 0, false, 0);
        builder.after(LINE_COMMENT).spacing(0, 0, 1, true, 2);
        builder.after(LCURLY).spacing(0, 0, 1, true, 2);
        builder.before(RCURLY).spacing(0, 0, 1, true, 2);
        builder.after(LPAREN).spacing(0, 0, 0, false, 0);
        builder.before(RPAREN).spacing(0, 0, 0, false, 0);
        builder.after(LSQUARE).spacing(0, 0, 0, false, 0);
        builder.before(RSQUARE).spacing(0, 0, 0, false, 0);
        builder.before(LT).spacing(0, 0, 0, false, 0);
        builder.after(LT).spacing(0, 0, 0, false, 0);
        builder.before(GT).spacing(0, 0, 0, false, 0);
        builder.before(COMMA).spacing(0, 0, 0, false, 0);
        builder.before(SEMICOLON).spacing(0, 0, 0, false, 0);
        builder.after(COMMA).spacing(1, 1, 0, false, 0);
        return builder;
    }

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
}
