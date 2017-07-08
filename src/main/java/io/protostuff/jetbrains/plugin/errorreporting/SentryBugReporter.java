package io.protostuff.jetbrains.plugin.errorreporting;

import static com.intellij.openapi.diagnostic.SubmittedReportInfo.SubmissionStatus.NEW_ISSUE;
import static io.protostuff.jetbrains.plugin.ProtostuffPluginController.PLUGIN_ID;

import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManager;
import com.intellij.openapi.application.ApplicationInfo;
import com.intellij.openapi.diagnostic.ErrorReportSubmitter;
import com.intellij.openapi.diagnostic.IdeaLoggingEvent;
import com.intellij.openapi.diagnostic.SubmittedReportInfo;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.util.Consumer;
import io.sentry.Sentry;
import io.sentry.SentryClient;
import io.sentry.event.EventBuilder;
import io.sentry.event.interfaces.ExceptionInterface;
import java.awt.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.stringtemplate.v4.ST;

/**
 * Report plugin errors to https://sentry.io.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public class SentryBugReporter extends ErrorReportSubmitter {

    private static final String REPORT_ACTION_TEXT = "Submit error to plugin maintainer";
    private static final String DEFAULT_RESPONSE = "Thank you for your report.";
    private static final String DEFAULT_RESPONSE_TITLE = "Report Submitted";

    private static final String EXTRA_OTHER_PLUGINS = "Other Plugins";
    private static final String EXTRA_MORE_EVENTS = "More Events";
    private static final String EXTRA_ADDITIONAL_INFO = "Additional Info";

    private static final String TAG_PLATFORM_VERSION = "platform";
    private static final String TAG_OS = "os";
    private static final String TAG_OS_VERSION = "os_version";
    private static final String TAG_OS_ARCH = "os_arch";
    private static final String TAG_JAVA_VERSION = "java_version";
    private static final String TAG_JAVA_RUNTIME_VERSION = "java_runtime_version";

    private static final String DSN = "https://0c8a8ad650c4467f9a17ac5179161e3f:b202d70375054565a02f452e0661f60a@sentry.io/186533";

    private static final SentryClient sentry = Sentry.init(DSN);

    @NotNull
    @Override
    public String getReportActionText() {
        return REPORT_ACTION_TEXT;
    }

    @Override
    public boolean submit(@NotNull IdeaLoggingEvent[] events, @Nullable String additionalInfo,
                          @NotNull Component parentComponent, @NotNull Consumer<SubmittedReportInfo> consumer) {
        EventBuilder eventBuilder = createEvent(events, additionalInfo);
        sentry.sendEvent(eventBuilder);
        consumer.consume(new SubmittedReportInfo(null, null, NEW_ISSUE));
        Messages.showInfoMessage(parentComponent, DEFAULT_RESPONSE, DEFAULT_RESPONSE_TITLE);
        return true;
    }

    @NotNull
    private EventBuilder createEvent(@NotNull IdeaLoggingEvent[] events, @Nullable String additionalInfo) {
        IdeaLoggingEvent ideaEvent = events[0];
        EventBuilder eventBuilder = new EventBuilder();
        eventBuilder.withMessage(ideaEvent.getMessage());
        eventBuilder.withRelease(getPluginVersion());
        eventBuilder.withTag(TAG_PLATFORM_VERSION, getPlatformVersion());
        eventBuilder.withTag(TAG_OS, SystemInfo.OS_NAME);
        eventBuilder.withTag(TAG_OS_VERSION, SystemInfo.OS_VERSION);
        eventBuilder.withTag(TAG_OS_ARCH, SystemInfo.OS_ARCH);
        eventBuilder.withTag(TAG_JAVA_VERSION, SystemInfo.JAVA_VERSION);
        eventBuilder.withTag(TAG_JAVA_RUNTIME_VERSION, SystemInfo.JAVA_RUNTIME_VERSION);
        if (ideaEvent.getThrowable() != null) {
            eventBuilder.withSentryInterface(new ExceptionInterface(ideaEvent.getThrowable()));
        }
        if (additionalInfo != null) {
            eventBuilder.withExtra(EXTRA_ADDITIONAL_INFO, additionalInfo);
        }
        if (events.length > 1) {
            eventBuilder.withExtra(EXTRA_MORE_EVENTS, getMoreEvents(events));
        }
        eventBuilder.withExtra(EXTRA_OTHER_PLUGINS, getOtherPluginsInfo());
        return eventBuilder;
    }

    @NotNull
    private StringBuilder getMoreEvents(@NotNull IdeaLoggingEvent[] events) {
        StringBuilder moreEvents = new StringBuilder();
        for (int i = 1; i < events.length; i++) {
            IdeaLoggingEvent event = events[i];
            moreEvents.append(event.toString());
            moreEvents.append("\n");
        }
        return moreEvents;
    }

    private String getPlatformVersion() {
        return ApplicationInfo.getInstance().getBuild().asString();
    }

    private static String getPluginVersion() {
        IdeaPluginDescriptor plugin = PluginManager.getPlugin(PluginId.getId(PLUGIN_ID));
        if (plugin == null) {
            return "unknown";
        }
        return plugin.getVersion();
    }

    private String getOtherPluginsInfo() {
        try {
            String template = "<%1:{plugin|<plugin.name> (<plugin.pluginId>:<plugin.version>)}; separator=\", \">";
            return ST.format(template, (Object) PluginManager.getPlugins());
        } catch (Exception e) {
            return String.valueOf(e);
        }
    }

}
