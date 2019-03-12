package io.protostuff.jetbrains.plugin.reference;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import io.protostuff.jetbrains.plugin.AbstractProtobufLibraryDependentTestCase;
import io.protostuff.jetbrains.plugin.psi.ExtendEntryNode;
import io.protostuff.jetbrains.plugin.psi.FieldNode;
import io.protostuff.jetbrains.plugin.psi.GroupNode;
import io.protostuff.jetbrains.plugin.psi.MessageNode;
import org.junit.Assert;

/**
 * Tests for custom option references.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public class CustomOptionReferenceTest extends AbstractProtobufLibraryDependentTestCase {

    @Override
    protected String getTestDataPath() {
        return "src/test/resources/";
    }

    public void testCustomOptionReference() {
        PsiReference reference = myFixture.getReferenceAtCaretPositionWithAssertion(
                "reference/options/custom/SimpleExtensionField.proto"
        );
        PsiElement target = reference.resolve();
        Assert.assertNotNull(target);
        Assert.assertTrue(target instanceof FieldNode);
        FieldNode field = (FieldNode) target;
        Assert.assertEquals("bar", field.getFieldName());
        Assert.assertTrue(field.getParent() instanceof ExtendEntryNode);
    }

    public void testImportedCustomOptionReference() {
        myFixture.configureByFile("reference/options/custom/SimpleExtensionField.proto");
        PsiReference reference = myFixture.getReferenceAtCaretPositionWithAssertion(
                "reference/options/custom/ImportedExtension.proto"
        );
        PsiElement target = reference.resolve();
        Assert.assertNotNull(target);
        Assert.assertTrue(target instanceof FieldNode);
        FieldNode field = (FieldNode) target;
        Assert.assertEquals("bar", field.getFieldName());
        Assert.assertTrue(field.getParent() instanceof ExtendEntryNode);
    }

    public void testImportedCustomOptionReference_withDescriptorProtoImport() {
        myFixture.configureByFiles("reference/options/custom/SimpleExtensionField.proto",
                "google/protobuf/__descriptor.proto");
        PsiReference reference = myFixture.getReferenceAtCaretPositionWithAssertion(
                "reference/options/custom/ImportedExtension_withDescriptorProtoImport.proto"
        );
        PsiElement target = reference.resolve();
        Assert.assertNotNull(target);
        Assert.assertTrue(target instanceof FieldNode);
        FieldNode field = (FieldNode) target;
        Assert.assertEquals("bar", field.getFieldName());
        Assert.assertTrue(field.getParent() instanceof ExtendEntryNode);
    }

    public void testImportedImportedCustomOptionReference() {
        myFixture.configureByFiles(
                "reference/options/custom/SimpleExtensionField.proto",
                "reference/options/custom/ImportedExtension.proto"
        );
        PsiReference reference = myFixture.getReferenceAtCaretPositionWithAssertion(
                "reference/options/custom/ImportedImportedExtension.proto"
        );
        PsiElement target = reference.resolve();
        Assert.assertNotNull(target);
        Assert.assertTrue(target instanceof FieldNode);
        FieldNode field = (FieldNode) target;
        Assert.assertEquals("bar", field.getFieldName());
        Assert.assertTrue(field.getParent() instanceof ExtendEntryNode);
    }

    public void testExtensionIsMessage_PointToMessage() {
        PsiReference reference = myFixture.getReferenceAtCaretPositionWithAssertion(
                "reference/options/custom/ExtensionIsMessage.proto"
        );
        PsiElement target = reference.resolve();
        Assert.assertNotNull(target);
        Assert.assertTrue(target instanceof FieldNode);
        FieldNode field = (FieldNode) target;
        Assert.assertEquals("foo", field.getFieldName());
        Assert.assertTrue(field.getParent() instanceof ExtendEntryNode);
    }

    public void testExtensionIsGroupField_PointToGroupField() {
        PsiReference reference = myFixture.getReferenceAtCaretPositionWithAssertion(
                "reference/options/custom/ExtensionIsGroupField.proto"
        );
        PsiElement target = reference.resolve();
        Assert.assertNotNull(target);
        Assert.assertTrue(target instanceof FieldNode);
        FieldNode field = (FieldNode) target;
        Assert.assertEquals("xyzzy", field.getFieldName());
        Assert.assertTrue(field.getParent() instanceof GroupNode);
    }

    public void testExtensionIsMessage_PointToField() {
        PsiReference reference = myFixture.getReferenceAtCaretPositionWithAssertion(
                "reference/options/custom/FieldOfExtension.proto"
        );
        PsiElement target = reference.resolve();
        Assert.assertNotNull(target);
        Assert.assertTrue(target instanceof FieldNode);
        FieldNode field = (FieldNode) target;
        Assert.assertEquals("bar", field.getFieldName());
        Assert.assertTrue(field.getParent() instanceof MessageNode);
    }

    public void testExtensionIsMessage_PointToExtension() {
        PsiReference reference = myFixture.getReferenceAtCaretPositionWithAssertion(
                "reference/options/custom/ExtensionOfExtension.proto"
        );
        PsiElement target = reference.resolve();
        Assert.assertNotNull(target);
        Assert.assertTrue(target instanceof FieldNode);
        FieldNode field = (FieldNode) target;
        Assert.assertEquals("bar", field.getFieldName());
        Assert.assertTrue(field.getParent() instanceof ExtendEntryNode);
    }
}
