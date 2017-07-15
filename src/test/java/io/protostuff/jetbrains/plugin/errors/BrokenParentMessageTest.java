package io.protostuff.jetbrains.plugin.errors;

import com.intellij.psi.PsiReference;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import io.protostuff.jetbrains.plugin.psi.FieldNode;
import io.protostuff.jetbrains.plugin.psi.TypeReferenceNode;

/**
 * Tests for rename enum refactoring.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public class BrokenParentMessageTest extends LightCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "src/test/resources/";
    }

    /**
     * Test for invalid input - containing message is not fully parsed
     * due to error.
     *
     * https://github.com/protostuff/protobuf-jetbrains-plugin/issues/63
     *
     * Type reference inside of such block should resolve to {@code null}
     * instead of throwing exception.
     */
    public void testContainingMessageIsErrorNode() {
        myFixture.configureByFile("errors/BrokenParentMessage.proto");
        FieldNode field = myFixture.findElementByText("parent", FieldNode.class);
        assertNotNull(field);
        TypeReferenceNode fieldType = field.getFieldType();
        assertNotNull(fieldType);
        PsiReference target = fieldType.getReference();
        assertNotNull(target);
        assertNull(target.resolve());
        myFixture.checkHighlighting();
    }

}
