package io.protostuff.jetbrains.plugin.formatter;

import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CustomCodeStyleSettings;

public class ProtoCodeStyleSettings extends CustomCodeStyleSettings {
    public ProtoCodeStyleSettings(CodeStyleSettings settings) {
        super("ProtoCodeStyleSettings", settings);
    }


}