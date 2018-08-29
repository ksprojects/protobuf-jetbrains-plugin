package io.protostuff.jetbrains.plugin.reference;

import com.intellij.psi.PsiElement;
import io.protostuff.jetbrains.plugin.AbstractProtobufLibraryDependentTestCase;
import io.protostuff.jetbrains.plugin.psi.DataType;
import io.protostuff.jetbrains.plugin.psi.FieldNode;
import io.protostuff.jetbrains.plugin.psi.ProtoPsiFileRoot;
import org.junit.Assert;

/**
 * Tests for resolving standard options.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public class StandardOptionReferenceTest extends AbstractProtobufLibraryDependentTestCase {

    @Override
    protected String getTestDataPath() {
        return "src/test/resources";
    }

    public void testStandardMessageOptionReference() {
        checkReferenceToField(".google.protobuf.MessageOptions", "deprecated",
                "reference/options/StandardMessageOptionReferenceTestData.proto");
    }

    public void testStandardFieldOptionReference() {
        checkReferenceToField(".google.protobuf.FieldOptions", "deprecated",
                "reference/options/StandardFieldOptionReferenceTestData.proto");
    }

    public void testStandardMapFieldOptionReference() {
        checkReferenceToField(".google.protobuf.FieldOptions", "deprecated",
                "reference/options/StandardMapFieldOptionReferenceTestData.proto");
    }

    public void testStandardEnumOptionReference() {
        checkReferenceToField(".google.protobuf.EnumOptions", "deprecated",
                "reference/options/StandardEnumOptionReferenceTestData.proto");
    }

    public void testStandardEnumConstantOptionReference() {
        checkReferenceToField(".google.protobuf.EnumValueOptions", "deprecated",
                "reference/options/StandardEnumConstantOptionReferenceTestData.proto");
    }

    public void testStandardServiceOptionReference() {
        checkReferenceToField(".google.protobuf.ServiceOptions", "deprecated",
                "reference/options/StandardServiceOptionReferenceTestData.proto");
    }

    public void testStandardServiceMethodOptionReference() {
        checkReferenceToField(".google.protobuf.MethodOptions", "deprecated",
                "reference/options/StandardServiceMethodOptionReferenceTestData.proto");
    }

    public void testStandardFileOptionReference() {
        checkReferenceToField(".google.protobuf.FileOptions", "deprecated",
                "reference/options/StandardFileOptionReferenceTestData.proto");
    }

    public void testPhpNamespaceReference() {
        checkReferenceToField(".google.protobuf.FileOptions", "php_namespace",
                "reference/options/Fix_php_namespace.proto");
    }

    private void checkReferenceToField(String typeReference, String fieldName, String... file) {
        myFixture.configureByFiles(file);
        ProtoPsiFileRoot proto = (ProtoPsiFileRoot) myFixture.getFile();
        PsiElement elementAtCaret = proto
                .findElementAt(myFixture.getCaretOffset());
        while (elementAtCaret != null
                && elementAtCaret.getReferences().length == 0) {
            elementAtCaret = elementAtCaret.getParent();
        }
        assertNotNull(elementAtCaret);
        PsiElement target = elementAtCaret.getReferences()[0].resolve();
        Assert.assertTrue(target instanceof FieldNode);
        FieldNode field = (FieldNode) target;
        Assert.assertEquals(fieldName, field.getFieldName());
        Assert.assertEquals(typeReference, ((DataType) field.getParent()).getQualifiedName());
    }
}
