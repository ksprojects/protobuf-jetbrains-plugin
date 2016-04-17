package io.protostuff.jetbrains.plugin.formatter;

import com.intellij.formatting.Spacing;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public class Formatting {

    static final Spacing BLANK_LINE = Spacing.createSpacing(0, 0, 2, true, 2);
    static final Spacing LINE_BREAK = Spacing.createSpacing(0, 0, 1, true, 2);
    static final Spacing NONE = Spacing.createSpacing(0, 0, 0, true, 2);

    private Formatting() {
    }

    static boolean isNotEmpty(com.intellij.lang.ASTNode node) {
        return node.getText().trim().length() > 0;
    }
}
