package io.protostuff.jetbrains.plugin.reference;

import com.intellij.psi.PsiElement;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import io.protostuff.jetbrains.plugin.psi.FieldNode;
import io.protostuff.jetbrains.plugin.psi.ProtoPsiFileRoot;
import org.junit.Assert;

/**
 * Tests for custom option references.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public class CustomOptionReferenceTest extends LightCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "src/test/resources/reference/options";
    }

    // TODO
    public void testCustomOptionReference() {
        PsiElement target = findReferenceElementAtCaret(new String[]{"CustomOptionReferenceTestData.proto"});
        Assert.assertNotNull(target);
        Assert.assertTrue(target instanceof FieldNode);
        // FieldNode field = (FieldNode) target;
        // Assert.assertEquals("deprecated", field.getFieldName());
        // Assert.assertEquals(".reference.options.bar", ((DataType)field.getParent()).getQualifiedName());
    }

    private PsiElement findReferenceElementAtCaret(String[] file) {
        myFixture.configureByFiles(file);
        ProtoPsiFileRoot proto = (ProtoPsiFileRoot) myFixture.getFile();
        PsiElement elementAtCaret = proto
                .findElementAt(myFixture.getCaretOffset());
        while (elementAtCaret != null && elementAtCaret.getReference() == null) {
            elementAtCaret = elementAtCaret.getParent();
        }
        assertNotNull(elementAtCaret);
        assertNotNull(elementAtCaret.getReference());
        return elementAtCaret.getReference().resolve();
    }
}
