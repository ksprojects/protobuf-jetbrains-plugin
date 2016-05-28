package io.protostuff.jetbrains.plugin;

import com.intellij.CommonBundle;
import org.jetbrains.annotations.PropertyKey;

import java.util.ResourceBundle;

public class ProtostuffBundle {
    private static final String BUNDLE_NAME = "io.protostuff.protobuf-jetbrains-plugin.messages.ProtostuffBundle";
    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

    public static String message(@PropertyKey(resourceBundle = BUNDLE_NAME) String key, Object... params) {
        return CommonBundle.message(BUNDLE, key, params);
    }
}
