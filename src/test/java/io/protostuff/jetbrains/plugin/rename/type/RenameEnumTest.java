package io.protostuff.jetbrains.plugin.rename.type;

import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import io.protostuff.jetbrains.plugin.psi.EnumNode;
import org.junit.Assert;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public class RenameEnumTest extends LightCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "src/test/resources/";
    }

    public void testRenameMessage_caretAtMessageName() {
        myFixture.configureByFiles("rename/type/RenameEnum_CaretAtEnumName.proto");
        myFixture.renameElementAtCaret("NewName");
        EnumNode element = myFixture.findElementByText("NewName", EnumNode.class);
        Assert.assertEquals("rename.type.NewName", element.getFullName());
    }

    public void testRenameMessage_caretAtField() {
        myFixture.configureByFiles("rename/type/RenameEnum_CaretAtField.proto");
        myFixture.renameElementAtCaret("NewName");
        EnumNode element = myFixture.findElementByText("NewName", EnumNode.class);
        Assert.assertEquals("rename.type.NewName", element.getFullName());
    }
}
