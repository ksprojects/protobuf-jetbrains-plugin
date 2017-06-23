package io.protostuff.jetbrains.plugin.rename.field;

import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import io.protostuff.jetbrains.plugin.psi.FieldNode;
import io.protostuff.jetbrains.plugin.psi.OptionNode;
import org.junit.Assert;

/**
 * Tests for rename message refactoring.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public class RenameFieldTest extends LightCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "src/test/resources/";
    }

    public void testRenameField_caretAtFieldName() {
        myFixture.configureByFiles("rename/field/RenameField_CaretAtField.proto");
        myFixture.renameElementAtCaret("NewName");
        OptionNode option = myFixture.findElementByText("(foo).NewName", OptionNode.class);
        Assert.assertNotNull(option);
    }

    public void testRenameField_caretAtOption() {
        myFixture.configureByFiles("rename/field/RenameField_CaretAtOption.proto");
        myFixture.renameElementAtCaret("NewName");
        FieldNode option = myFixture.findElementByText("NewName", FieldNode.class);
        Assert.assertNotNull(option);
    }

}
