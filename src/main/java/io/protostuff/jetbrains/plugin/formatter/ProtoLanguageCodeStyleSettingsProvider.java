package io.protostuff.jetbrains.plugin.formatter;

import com.google.common.base.Joiner;
import com.intellij.lang.Language;
import com.intellij.psi.codeStyle.CodeStyleSettingsCustomizable;
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider;
import io.protostuff.jetbrains.plugin.ProtoLanguage;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public class ProtoLanguageCodeStyleSettingsProvider extends LanguageCodeStyleSettingsProvider {

    @NotNull
    @Override
    public Language getLanguage() {
        return ProtoLanguage.INSTANCE;
    }


    @Override
    public void customizeSettings(@NotNull CodeStyleSettingsCustomizable consumer, @NotNull SettingsType settingsType) {
        switch (settingsType) {
            case INDENT_SETTINGS:
                consumer.showStandardOptions("INDENT_SIZE", "TAB_SIZE");
            case SPACING_SETTINGS:
                consumer.showStandardOptions("SPACE_AROUND_ASSIGNMENT_OPERATORS");
                break;
            case BLANK_LINES_SETTINGS:
                consumer.showStandardOptions("KEEP_BLANK_LINES_IN_CODE");
                break;
            default:
                break;
        }
//        consumer.showAllStandardOptions();
    }

    @Override
    public String getCodeSample(@NotNull SettingsType settingsType) {
        return Joiner.on('\n').join(
                "message Test {",
                "    optional int32 foo = 1;",
                "    optional string bar = 2;",
                "}"
        );
    }

}
