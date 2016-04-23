package io.protostuff.jetbrains.plugin.formatter;

import com.google.common.base.Joiner;
import com.intellij.application.options.IndentOptionsEditor;
import com.intellij.application.options.SmartIndentOptionsEditor;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.intellij.psi.codeStyle.FileTypeIndentOptionsProvider;
import io.protostuff.jetbrains.plugin.ProtoFileType;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public class ProtoIndentOptionsProvider implements FileTypeIndentOptionsProvider {

    @Override
    public CommonCodeStyleSettings.IndentOptions createIndentOptions() {
        CommonCodeStyleSettings.IndentOptions indentOptions = new CodeStyleSettings.IndentOptions();
        indentOptions.INDENT_SIZE = 2;
        return indentOptions;
    }

    @Override
    public FileType getFileType() {
        return ProtoFileType.INSTANCE;
    }

    @Override
    public IndentOptionsEditor createOptionsEditor() {
        return new SmartIndentOptionsEditor();
    }

    @Override
    public String getPreviewText() {
        return Joiner.on('\n').join(
                "message Test {",
                "    optional int32 foo = 1;",
                "    optional string bar = 2;",
                "}"
        );
    }

    @Override
    public void prepareForReformat(PsiFile psiFile) {

    }
}
