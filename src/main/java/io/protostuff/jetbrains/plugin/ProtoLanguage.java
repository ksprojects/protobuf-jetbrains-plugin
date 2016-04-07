package io.protostuff.jetbrains.plugin;

import com.intellij.lang.Language;
import org.jetbrains.annotations.NotNull;

public class ProtoLanguage extends Language {
    public static final ProtoLanguage INSTANCE = new ProtoLanguage();

    private ProtoLanguage() {
        super("PROTO");
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return "Protobuf";
    }

    @Override
    public boolean isCaseSensitive() {
        return true;
    }

}
