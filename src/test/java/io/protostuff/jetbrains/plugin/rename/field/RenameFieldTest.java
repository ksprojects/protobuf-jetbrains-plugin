package io.protostuff.jetbrains.plugin.rename.field;

import io.protostuff.jetbrains.plugin.AbstractProtobufLibraryDependentTestCase;
import io.protostuff.jetbrains.plugin.psi.FieldNode;
import io.protostuff.jetbrains.plugin.psi.MapNode;
import io.protostuff.jetbrains.plugin.psi.OptionNode;
import org.junit.Assert;

/**
 * Tests for rename message refactoring.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public class RenameFieldTest extends AbstractProtobufLibraryDependentTestCase {

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

    public void testRenameField_caretAtFieldName_OneofOption() {
        myFixture.configureByFiles("rename/field/RenameField_CaretAtField_OneofOption.proto");
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

    public void testRenameOneofField_caretAtFieldName() {
        myFixture.configureByFiles("rename/field/RenameOneofField_CaretAtField.proto");
        myFixture.renameElementAtCaret("NewName");
        OptionNode option = myFixture.findElementByText("(foo).NewName", OptionNode.class);
        Assert.assertNotNull(option);
    }

    public void testRenameOneofField_caretAtOption() {
        myFixture.configureByFiles("rename/field/RenameOneofField_CaretAtOption.proto");
        myFixture.renameElementAtCaret("NewName");
        FieldNode option = myFixture.findElementByText("NewName", FieldNode.class);
        Assert.assertNotNull(option);
    }

    public void testRenameMapField_caretAtFieldName() {
        myFixture.configureByFiles("rename/field/RenameMapField_CaretAtField.proto");
        myFixture.renameElementAtCaret("NewName");
        OptionNode option = myFixture.findElementByText("(foo).NewName", OptionNode.class);
        Assert.assertNotNull(option);
    }

    public void testRenameMapField_caretAtOption() {
        myFixture.configureByFiles("rename/field/RenameMapField_CaretAtOption.proto");
        myFixture.renameElementAtCaret("NewName");
        MapNode option = myFixture.findElementByText("NewName", MapNode.class);
        Assert.assertNotNull(option);
    }

    public void testRenameExtension_caretAtFieldName() {
        myFixture.configureByFiles("rename/field/RenameExtension_CaretAtField.proto");
        myFixture.renameElementAtCaret("NewName");
        FieldNode option = myFixture.findElementByText("NewName", FieldNode.class);
        Assert.assertNotNull(option);
    }

}
