package io.protostuff.jetbrains.plugin.reference;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import io.protostuff.jetbrains.plugin.psi.ExtendEntryNode;
import io.protostuff.jetbrains.plugin.psi.FieldNode;
import io.protostuff.jetbrains.plugin.psi.MessageNode;
import org.junit.Assert;

/**
 * Tests for custom option references.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public class CustomOptionReferenceTest extends LightCodeInsightFixtureTestCase {

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
