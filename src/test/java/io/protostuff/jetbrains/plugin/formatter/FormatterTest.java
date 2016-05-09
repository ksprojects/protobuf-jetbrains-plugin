package io.protostuff.jetbrains.plugin.formatter;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.vfs.newvfs.impl.VfsRootAccess;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CodeStyleSettingsManager;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import io.protostuff.jetbrains.plugin.ProtoLanguage;

import java.util.function.Consumer;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public class FormatterTest extends LightCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "src/test/resources/formatting";
    }

    private void run(String test) {
       run(test, commonCodeStyleSettings -> {});
    }

    private void run(String test, Consumer<CommonCodeStyleSettings> settings) {
        myFixture.configureByFiles(test + "/Source.proto");
        CodeStyleSettings codeStyleSettings = CodeStyleSettingsManager.getSettings(getProject());
        CommonCodeStyleSettings protoSettings = codeStyleSettings.getCommonSettings(ProtoLanguage.INSTANCE);
        settings.accept(protoSettings);
        new WriteCommandAction.Simple(getProject()) {
            @Override
            protected void run() throws Throwable {
                CodeStyleManager.getInstance(getProject()).reformat(myFixture.getFile());
            }
        }.execute();
        myFixture.checkResultByFile(test + "/Expected.proto");
    }

    public void testDefaultFormatter() {
       run("default");
    }

    public void testTab() {
       run("use_tab", settings -> {
           CommonCodeStyleSettings.IndentOptions indentOptions = settings.initIndentOptions();
           indentOptions.USE_TAB_CHARACTER = true;
       });
    }

    public void testCustomIndent() {
        run("custom_indent", settings -> {
            CommonCodeStyleSettings.IndentOptions indentOptions = settings.initIndentOptions();
            indentOptions.INDENT_SIZE = 2;
        });
    }

    public void testTrailingComments() {
        run("trailing_line_comments");
    }
}
