package io.protostuff.jetbrains.plugin.reference;

import com.intellij.psi.PsiElement;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import io.protostuff.jetbrains.plugin.psi.DataType;
import io.protostuff.jetbrains.plugin.psi.ProtoPsiFileRoot;
import org.junit.Assert;

/**
 * Tests for resolving type references.
 *
 * @author Kostiantyn Shchepanovskyi
 */
@SuppressWarnings("ConstantConditions")
public class ReferenceTest extends LightCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "src/test/resources/reference";
    }

    public void testMessageReference() {
        checkReferenceToDataType(".reference.MessageA", "MessageReferenceTestData.proto");
    }

    public void testMessageReferenceWithoutPackage() {
        checkReferenceToDataType(".MessageA", "MessageReferenceWithoutPackageTestData.proto");
    }

    public void testEnumReference() {
        checkReferenceToDataType(".reference.EnumA", "EnumReferenceTestData.proto");
    }

    public void testImportedMessageReference() {
        // package is not set as files are copied to relative source root directly
        checkReferenceToDataType(".ImportedMessage", "ImportedMessageReferenceTestData.proto", "ImportedTestData.proto");
    }

    private void checkReferenceToDataType(String typeReference, String... file) {
        myFixture.configureByFiles(file);
        ProtoPsiFileRoot proto = (ProtoPsiFileRoot) myFixture.getFile();
        PsiElement elementAtCaret = proto
                .findElementAt(myFixture.getCaretOffset());
        while (elementAtCaret != null && elementAtCaret.getReference() == null) {
            elementAtCaret = elementAtCaret.getParent();
        }
        assertNotNull(elementAtCaret);
        PsiElement target = elementAtCaret.getReference().resolve();
        Assert.assertTrue(target instanceof DataType);
        DataType dataType = (DataType) target;
        Assert.assertEquals(typeReference, dataType.getQualifiedName());
    }
}
