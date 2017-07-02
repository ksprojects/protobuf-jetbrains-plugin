package io.protostuff.jetbrains.plugin.errorreporting;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.util.SystemProperties;

/**
 * This class allows to test the error reporter functionality by throwing a runtime exception when the action is
 * invoked.
 */
public class TriggerExceptionAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        throw new RuntimeException("Test exception.");
    }

    @Override
    public void update(AnActionEvent e) {
        boolean visible = SystemProperties.getBooleanProperty("trigger_exception_action.visible", false);
        e.getPresentation().setEnabledAndVisible(visible);
    }
}
