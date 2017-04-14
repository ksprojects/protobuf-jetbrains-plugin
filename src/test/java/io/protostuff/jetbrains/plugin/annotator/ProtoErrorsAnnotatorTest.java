package io.protostuff.jetbrains.plugin.annotator;

import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;

/**
 * Errors annotator tests.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public class ProtoErrorsAnnotatorTest extends LightCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "src/test/resources/annotator";
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

    private void check() {
        String file = getTestName(false) + ".proto";
        myFixture.configureByFiles(file);
        myFixture.checkHighlighting();
    }

}
