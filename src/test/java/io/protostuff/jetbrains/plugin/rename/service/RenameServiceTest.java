package io.protostuff.jetbrains.plugin.rename.service;

import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import io.protostuff.jetbrains.plugin.psi.ServiceNode;
import org.junit.Assert;

/**
 * Tests for rename message refactoring.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public class RenameServiceTest extends LightCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "src/test/resources/";
    }

    public void testRenameService() {
        myFixture.configureByFiles("rename/service/RenameService.proto");
        myFixture.renameElementAtCaret("NewName");
        ServiceNode option = myFixture.findElementByText("NewName", ServiceNode.class);
        Assert.assertNotNull(option);
    }

    public void testRenameRpc() {
        myFixture.configureByFiles("rename/service/RenameRpc.proto");
        myFixture.renameElementAtCaret("NewName");
        ServiceNode option = myFixture.findElementByText("NewName", ServiceNode.class);
        Assert.assertNotNull(option);
    }

}
