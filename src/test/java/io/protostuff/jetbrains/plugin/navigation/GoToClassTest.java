package io.protostuff.jetbrains.plugin.navigation;

import com.google.common.io.Files;
import com.intellij.ide.util.gotoByName.ChooseByNameModel;
import com.intellij.ide.util.gotoByName.ChooseByNamePopup;
import com.intellij.ide.util.gotoByName.GotoClassModel2;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.util.Disposer;
import com.intellij.psi.PsiElement;
import com.intellij.testFramework.EdtTestUtil;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import io.protostuff.jetbrains.plugin.psi.ProtoPsiFileRoot;
import io.protostuff.jetbrains.plugin.psi.ProtoType;
import io.protostuff.jetbrains.plugin.settings.ProtobufSettings;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * "Go To Class" tests topic is not covered in plugin development documentation.
 * <p>
 * This test is created from the test in intellij-community:
 * https://github.com/JetBrains/intellij-community/blob/master/java/java-tests/testSrc/com/intellij/navigation/ChooseByNameTest.groovy
 *
 * @author Kostiantyn Shchepanovskyi
 */
public class GoToClassTest extends LightCodeInsightFixtureTestCase {

    private ChooseByNamePopup popup = null;
    private File tempDir;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        tempDir = Files.createTempDir();
        tempDir.deleteOnExit();
    }

    @Override
    protected void tearDown() throws Exception {
        popup = null;
        super.tearDown();
    }

    public void testGoToMessage() throws Exception {
        ProtoPsiFileRoot file = addProto("test.proto", String.join("\n",
                "package navigation;",
                "message MyProtobufMessage {}"));
        ProtoType message = findType(file, "navigation.MyProtobufMessage");
        List<Object> elements = getPopupElements(new GotoClassModel2(getProject()), "MyProto", false);
        Assert.assertEquals(1, elements.size());
        Assert.assertSame(message, elements.get(0));
    }

    public void testGoToNestedMessage() throws Exception {
        ProtoPsiFileRoot file = addProto("test.proto", String.join("\n",
                "package navigation;",
                "message MyProtobufMessage {",
                "enum MyProtobufNestedMessage {}",
                "}"));
        ProtoType message = findType(file, "navigation.MyProtobufMessage.MyProtobufNestedMessage");
        List<Object> elements = getPopupElements(new GotoClassModel2(getProject()), "MyProtoNes", false);
        Assert.assertEquals(1, elements.size());
        Assert.assertSame(message, elements.get(0));
    }

    public void testGoToEnum() throws Exception {
        ProtoPsiFileRoot file = addProto("test.proto", String.join("\n",
                "package navigation;",
                "enum MyProtobufEnum {}"));
        ProtoType anEnum = findType(file, "navigation.MyProtobufEnum");
        List<Object> elements = getPopupElements(new GotoClassModel2(getProject()), "MyProto", false);
        Assert.assertEquals(1, elements.size());
        Assert.assertSame(anEnum, elements.get(0));
    }

    public void testGoToNestedEnum() throws Exception {
        ProtoPsiFileRoot file = addProto("test.proto", String.join("\n",
                "package navigation;",
                "message MyProtobufMessage {",
                "enum MyProtobufNestedEnum {}",
                "}"));
        ProtoType anEnum = findType(file, "navigation.MyProtobufMessage.MyProtobufNestedEnum");
        List<Object> elements = getPopupElements(new GotoClassModel2(getProject()), "MyProtoNes", false);
        Assert.assertEquals(1, elements.size());
        Assert.assertSame(anEnum, elements.get(0));
    }

    public void testGoToService() throws Exception {
        ProtoPsiFileRoot file = addProto("test.proto", String.join("\n",
                "package navigation;",
                "service MyProtobufService {}"));
        ProtoType service = findType(file, "navigation.MyProtobufService");
        List<Object> elements = getPopupElements(new GotoClassModel2(getProject()), "MyProto", false);
        Assert.assertEquals(1, elements.size());
        Assert.assertSame(service, elements.get(0));
    }

    public void testMultipleElements() throws Exception {
        addProto("test.proto", String.join("\n",
                "package navigation;",
                "message MyProtobufMessage {}",
                "enum MyProtobufEnum {}",
                "service MyProtobufService {}"
        ));
        List<Object> elements = getPopupElements(new GotoClassModel2(getProject()), "MyProto", false);
        Assert.assertEquals(3, elements.size());
    }

    public void testGoToMessage_inCustomIncludePath() throws Exception {
        File includePathRoot = tempDir;
        ProtobufSettings settings = getProject().getComponent(ProtobufSettings.class);
        settings.setIncludePaths(Collections.singletonList(includePathRoot.getAbsolutePath()));
        File proto = new File(tempDir.getPath() + "/test.proto");
        Writer writer = new BufferedWriter(new FileWriter(proto));
        writer.write("message MessageInCustomPath {}");
        writer.close();

        List<Object> elements = getPopupElements(new GotoClassModel2(getProject()), "MeInCus", false);
        Assert.assertEquals(1, elements.size());
    }

    private ProtoPsiFileRoot addProto(String name, String text) {
        return (ProtoPsiFileRoot) myFixture.addFileToProject(name, text);
    }

    private ProtoType findType(final ProtoPsiFileRoot file, final String fullName) {
        return ApplicationManager.getApplication().runReadAction(new Computable<ProtoType>() {
            @Override
            public ProtoType compute() {
                return file.findType(fullName);
            }
        });
    }

    private List<Object> getPopupElements(ChooseByNameModel model, String text, boolean checkboxState) {
        return calcPopupElements(createPopup(model, null), text, checkboxState);
    }

    private List<Object> calcPopupElements(ChooseByNamePopup popup, String text, boolean checkboxState) {
        List<Object> elements = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(1);
        SwingUtilities.invokeLater(() ->
                popup.scheduleCalcElements(text, checkboxState, ModalityState.NON_MODAL,
                        set -> {
                            elements.addAll(set);
                            latch.countDown();
                        }));
        try {
            if (!latch.await(10, TimeUnit.SECONDS)) {
                Assert.fail();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return elements;
    }

    private ChooseByNamePopup createPopup(ChooseByNameModel model, PsiElement context) {
        if (popup != null) {
            popup.close(false);
        }
        EdtTestUtil.runInEdtAndWait(new Runnable() {
            @Override
            public void run() {
                ChooseByNamePopup popup = ChooseByNamePopup.createPopup(getProject(), model, context, "");
                Disposer.register(getTestRootDisposable(), () -> popup.close(false));
                GoToClassTest.this.popup = popup;
            }
        });
        return popup;
    }

    @Override
    protected boolean runInDispatchThread() {
        return false;
    }

    @Override
    protected void invokeTestRunnable(@NotNull Runnable runnable) throws Exception {
        runnable.run();
    }
}
