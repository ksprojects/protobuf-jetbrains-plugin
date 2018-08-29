package io.protostuff.jetbrains.plugin.reference;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import io.protostuff.jetbrains.plugin.AbstractProtobufLibraryDependentTestCase;
import io.protostuff.jetbrains.plugin.psi.DataType;
import io.protostuff.jetbrains.plugin.psi.ProtoPsiFileRoot;

/**
 * Tests for resolving type references.
 *
 * @author Kostiantyn Shchepanovskyi
 */
@SuppressWarnings("ConstantConditions")
public class ReferenceTest extends AbstractProtobufLibraryDependentTestCase {

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
        checkReferenceToDataType(".reference.ImportedMessage",
                "reference/ImportedMessageReferenceTestData.proto",
                "reference/ImportedTestData.proto");
    }

    public void testFix_import_empty_proto() {
        String[] file = new String[]{"reference/Fix_import_empty_proto.proto"};
        myFixture.configureByFiles(file);
        PsiReference referenceAtCaretPosition = myFixture.getReferenceAtCaretPositionWithAssertion(file);
        PsiElement target = referenceAtCaretPosition.resolve();
        assertNotNull(target);
        assertTrue(target instanceof ProtoPsiFileRoot);
        ProtoPsiFileRoot protoPsiFileRoot = (ProtoPsiFileRoot) target;
        assertEquals("empty.proto", protoPsiFileRoot.getName());
    }

    public void testImportedRelativelyMessageReference() {
        checkReferenceToDataType(".import.ImportedMessage1",
                "reference/relative/import/ImportedRelativelyMessageReferenceTestData.proto",
                "reference/relative/import/ImportedRelativelyTestData.proto");
    }

    public void testImportedRelativelyNoPackageMessageReference() {
        checkReferenceToDataType(".import.ImportedMessage1",
                "reference/relative/import/ImportedRelativelyNoPackageMessageReferenceTestData.proto",
                "reference/relative/import/ImportedRelativelyTestData.proto");
    }

    public void testImportedRelativelyTwoLevelsMessageReference() {
        checkReferenceToDataType(".import.ImportedMessage1",
                "reference/relative/import/ImportedRelativelyTwoLevelsMessageReferenceTestData.proto",
                "reference/relative/import/ImportedRelativelyTestData.proto");
    }

    public void testImportedRelativelyFileReference() {
        String[] file = new String[]{"reference/relative/import/ImportedRelativelyFileReferenceTestData.proto",
                "reference/relative/import/ImportedRelativelyTestData.proto"};
        myFixture.configureByFiles(file);
        PsiReference referenceAtCaretPosition = myFixture.getReferenceAtCaretPositionWithAssertion(file);
        PsiElement target = referenceAtCaretPosition.resolve();
        assertNotNull(target);
        assertTrue(target instanceof ProtoPsiFileRoot);
        ProtoPsiFileRoot protoPsiFileRoot = (ProtoPsiFileRoot) target;
        assertEquals("ImportedRelativelyTestData.proto", protoPsiFileRoot.getName());
    }

    /**
     * Check that type references work correctly inside of group block.
     *
     * https://github.com/protostuff/protobuf-jetbrains-plugin/issues/62
     */
    public void testReferenceInsideOfGroup() {
        checkReferenceToDataType(".reference.Message.Group.NestedMessage", "reference/GroupReference.proto");
    }

    /**
     * Check that type references work correctly inside of oneof+group block.
     *
     * https://github.com/protostuff/protobuf-jetbrains-plugin/issues/72
     */
    public void testReferenceInsideOfOneof() {
        checkReferenceToDataType(".reference.Message.Group2.NestedMessage", "reference/OneofReference.proto");
    }

    public void testProtoFileImportsItself() {
        // referenced type does not exist, so we expect that it is not resolvable
        // the only thing that we check here - is that we do not get StackOverflowError
        checkReferenceToDataType(null, "reference/ProtoFileImportsItself.proto");
    }

    private void checkReferenceToDataType(String typeReference, String... file) {
        myFixture.configureByFiles(file);
        PsiReference referenceAtCaretPosition = myFixture.getReferenceAtCaretPositionWithAssertion(file);
        PsiElement target = referenceAtCaretPosition.resolve();
        if (typeReference != null) {
            assertNotNull(target);
            assertTrue(target instanceof DataType);
            DataType dataType = (DataType) target;
            assertEquals(typeReference, dataType.getQualifiedName());
        }
    }
}
