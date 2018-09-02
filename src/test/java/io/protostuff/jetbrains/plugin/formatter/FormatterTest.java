package io.protostuff.jetbrains.plugin.formatter;

import com.intellij.application.options.CodeStyle;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import io.protostuff.jetbrains.plugin.ProtoLanguage;
import java.util.function.Consumer;

/**
 * Formatter tests.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public class FormatterTest extends LightCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "src/test/resources/formatting";
    }

    private void run(String test) {
        run(test, commonCodeStyleSettings -> {
        });
    }

    private void run(String test, Consumer<CommonCodeStyleSettings> settings) {
        myFixture.configureByFiles(test + "/Source.proto");
        CodeStyleSettings codeStyleSettings = CodeStyle.getSettings(getProject());
        CommonCodeStyleSettings protoSettings = codeStyleSettings.getCommonSettings(ProtoLanguage.INSTANCE);
        settings.accept(protoSettings);
        WriteCommandAction.writeCommandAction(getProject())
                .run(() -> CodeStyleManager.getInstance(getProject()).reformat(myFixture.getFile()));
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
