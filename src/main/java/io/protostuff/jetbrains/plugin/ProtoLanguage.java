package io.protostuff.jetbrains.plugin;

import com.intellij.lang.Language;

public class ProtoLanguage extends Language {
    public static final ProtoLanguage INSTANCE = new ProtoLanguage();

    private ProtoLanguage() {
        super("Protobuf");
    }
}
