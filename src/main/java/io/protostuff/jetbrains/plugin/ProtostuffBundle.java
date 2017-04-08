package io.protostuff.jetbrains.plugin;

import com.intellij.CommonBundle;
import java.util.ResourceBundle;
import org.jetbrains.annotations.PropertyKey;

public class ProtostuffBundle {
    private static final String BUNDLE_NAME = "io.protostuff.protobuf-jetbrains-plugin.messages.ProtostuffBundle";
    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

    public static String message(@PropertyKey(resourceBundle = BUNDLE_NAME) String key, Object... params) {
        return CommonBundle.message(BUNDLE, key, params);
    }
}
