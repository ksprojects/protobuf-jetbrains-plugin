package io.protostuff.jetbrains.plugin.rename.type;

import com.google.common.collect.Iterables;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import io.protostuff.jetbrains.plugin.psi.MessageField;
import io.protostuff.jetbrains.plugin.psi.MessageNode;
import java.util.Collection;
import org.junit.Assert;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public class RenameMessageTest extends LightCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "src/test/resources/";
    }

    public void testRenameMessage_caretAtMessageName() {
        myFixture.configureByFiles("rename/type/RenameMessage_CaretAtMessageName.proto");
        myFixture.renameElementAtCaret("NewName");
        MessageNode message = myFixture.findElementByText("NewName", MessageNode.class);
        Assert.assertEquals("rename.type.NewName", message.getFullName());
    }

    public void testRenameMessage_caretAtField() {
        myFixture.configureByFiles("rename/type/RenameMessage_CaretAtField.proto");
        myFixture.renameElementAtCaret("NewName");
        MessageNode message = myFixture.findElementByText("NewName", MessageNode.class);
        Assert.assertEquals("rename.type.NewName", message.getFullName());
    }

    public void testRenameMessage_fieldReferenceIncludesPackage() {
        myFixture.configureByFiles("rename/type/RenameMessage_FieldReferenceIncludesPackage.proto");
        myFixture.renameElementAtCaret("NewName");
        MessageNode message = myFixture.findElementByText("NewName", MessageNode.class);
        Assert.assertEquals("rename.type.NewName", message.getFullName());
        MessageNode fieldParent = myFixture.findElementByText("TestMessage", MessageNode.class);
        Collection<MessageField> fields = fieldParent.getFields();
        MessageField field = Iterables.getOnlyElement(fields);
        assertEquals("rename.type.NewName", field.getFieldType().getText());
    }
}
