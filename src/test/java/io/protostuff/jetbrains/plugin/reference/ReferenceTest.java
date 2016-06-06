package io.protostuff.jetbrains.plugin.reference;

import com.intellij.psi.PsiElement;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import io.protostuff.jetbrains.plugin.psi.ProtoPsiFileRoot;
import io.protostuff.jetbrains.plugin.psi.TypeReferenceNode;
import io.protostuff.jetbrains.plugin.psi.UserType;
import org.junit.Assert;

/**
 * @author Kostiantyn Shchepanovskyi
 */
@SuppressWarnings("ConstantConditions")
public class ReferenceTest extends LightCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "src/test/resources/reference";
    }

    public void testMessageReference() {
        check(".reference.MessageA", "MessageReferenceTestData.proto");
    }

    public void testMessageReferenceWithoutPackage() {
        check(".MessageA", "MessageReferenceWithoutPackageTestData.proto");
    }

    public void testEnumReference() {
        check(".reference.EnumA", "EnumReferenceTestData.proto");
    }

    public void testImportedMessageReference() {
        // package is not set as files are copied to relative source root directly
        check(".ImportedMessage", "ImportedMessageReferenceTestData.proto", "ImportedTestData.proto");
    }

    private void check(String typeReference, String... file) {
        myFixture.configureByFiles(file);
        ProtoPsiFileRoot proto = (ProtoPsiFileRoot) myFixture.getFile();
        TypeReferenceNode element = (TypeReferenceNode) proto
                .findElementAt(myFixture.getCaretOffset()) // IDENT
                .getParent() // ident
                .getParent(); // typeReference
        PsiElement target = element.getReference().resolve();
        Assert.assertTrue(target instanceof UserType);
        UserType userType = (UserType) target;
        Assert.assertEquals(typeReference, userType.getQualifiedName());
    }
}
