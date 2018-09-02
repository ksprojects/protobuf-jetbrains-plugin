package io.protostuff.jetbrains.plugin.annotator;

import io.protostuff.jetbrains.plugin.AbstractProtobufLibraryDependentTestCase;

/**
 * Errors annotator tests.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public class ProtoErrorsAnnotatorTest extends AbstractProtobufLibraryDependentTestCase {

    @Override
    protected String getTestDataPath() {
        return "src/test/resources/annotator";
    }

    public void testNormalOneof() {
        check();
    }

    public void testIllegalOneofLabel() {
        check();
    }

    public void testInvalidTagValue() {
        check();
    }

    public void testDuplicateFieldName() {
        check();
    }

    public void testDuplicateTagValue() {
        check();
    }

    public void testDuplicateMapFieldName() {
        check();
    }

    public void testDuplicateMapTagValue() {
        check();
    }

    public void testDuplicateOneofFieldName() {
        check();
    }

    public void testDuplicateOneofTagValue() {
        check();
    }

    public void testReservedTagValue() {
        check();
    }

    public void testReservedFieldName() {
        check();
    }

    public void testReservedEnumValue() {
        check();
    }

    public void testReservedEnumName() {
        check();
    }

    public void testDuplicateEnumConstantName() {
        check();
    }

    public void testDuplicateEnumConstantValue() {
        check();
    }

    public void testDuplicateEnumConstantValue_Allowed() {
        check();
    }

    public void testDuplicateServiceMethodName() {
        check();
    }

    public void testProto2MissingFieldLabel() {
        check();
    }

    public void testProto3IllegalOptionalFieldLabel() {
        check();
    }

    public void testProto3IllegalRequiredFieldLabel() {
        check();
    }

    public void testProto3IllegalDefaultValueOption() {
        check();
    }

    public void testProto3IllegalGroup() {
        check();
    }

    public void testProto3ValidExtend() {
        check();
    }

    public void testProto2IllegalExtend() {
        check();
    }

    public void testProto3IllegalExtend() {
        check();
    }

    public void testProto3IllegalFirstEnumValue() {
        check();
    }

    public void testUnresolvedTypeReference_Field() {
        check();
    }

    public void testUnresolvedTypeReference_MapField() {
        check();
    }

    public void testUnresolvedTypeReference_ScalarType() {
        check();
    }

    public void testUnresolvedTypeReference_Extension() {
        check();
    }

    public void testUnresolvedTypeReference_Service() {
        check();
    }

    public void testUnresolvedOptionReference_StandardOption() {
        check();
    }

    public void testUnresolvedOptionReference_CustomOption() {
        check();
    }

    public void testUnresolvedFileReference() {
        check();
    }

    private void check() {
        String file = getTestName(false) + ".proto";
        myFixture.configureByFiles(file);
        myFixture.checkHighlighting();
    }

}
