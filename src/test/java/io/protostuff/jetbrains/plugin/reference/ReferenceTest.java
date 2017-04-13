package io.protostuff.jetbrains.plugin.reference;

import com.intellij.psi.PsiElement;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import io.protostuff.jetbrains.plugin.psi.DataType;
import io.protostuff.jetbrains.plugin.psi.ProtoPsiFileRoot;

/**
 * Tests for resolving type references.
 *
 * @author Kostiantyn Shchepanovskyi
 */
@SuppressWarnings("ConstantConditions")
public class ReferenceTest extends LightCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "src/test/resources";
    }

    public void testMessageReference() {
        checkReferenceToDataType(".reference.MessageA", "reference/MessageReferenceTestData.proto");
    }

    public void testMessageReferenceWithoutPackage() {
        checkReferenceToDataType(".MessageA", "reference/MessageReferenceWithoutPackageTestData.proto");
    }

    public void testEnumReference() {
        checkReferenceToDataType(".reference.EnumA", "reference/EnumReferenceTestData.proto");
    }

    public void testImportedMessageReference() {
        checkReferenceToDataType(".reference.ImportedMessage", "reference/ImportedMessageReferenceTestData.proto", "reference/ImportedTestData.proto");
    }

    public void testProtoFileImportsItself() {
        // referenced type does not exist, so we expect that it is not resolvable
        // the only thing that we check here - is that we do not get StackOverflowError
        checkReferenceToDataType(null, "reference/ProtoFileImportsItself.proto");
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
        if (typeReference != null) {
            assertNotNull(target);
            assertTrue(target instanceof DataType);
            DataType dataType = (DataType) target;
            assertEquals(typeReference, dataType.getQualifiedName());
        }
    }
}
