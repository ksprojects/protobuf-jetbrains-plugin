package io.protostuff.jetbrains.plugin.rename.field;

import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import io.protostuff.jetbrains.plugin.psi.OneOfNode;
import org.junit.Assert;

/**
 * Tests for rename message refactoring.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public class RenameOneofTest extends LightCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "src/test/resources/";
    }

    public void testRenameField_caretAtFieldName() {
        myFixture.configureByFiles("rename/field/RenameOneof.proto");
        myFixture.renameElementAtCaret("NewName");
        OneOfNode option = myFixture.findElementByText("NewName", OneOfNode.class);
        Assert.assertNotNull(option);
    }

}
