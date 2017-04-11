package io.protostuff.jetbrains.plugin.rename;

import com.intellij.openapi.extensions.Extensions;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.refactoring.rename.RenameProcessor;
import com.intellij.refactoring.rename.naming.AutomaticRenamerFactory;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import io.protostuff.jetbrains.plugin.psi.ProtoPsiFileRoot;
import org.junit.Assert;

/**
 * Tests for rename refactoring - proto file rename.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public class RenameImportedProtoTest extends LightCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "src/test/resources/";
    }

    public void testRenameImportedProtoAtCaretPosition() {
        PsiFile[] files = myFixture.configureByFiles(
                "rename/import/import.proto",
                "rename/import/source.proto"
        );

        PsiElement elementAtCaret = myFixture.getElementAtCaret();
        Project project = myFixture.getProject();
        RenameProcessor processor = new RenameProcessor(project, elementAtCaret, "renamed.proto", false, false);
        for (AutomaticRenamerFactory factory : Extensions.getExtensions(AutomaticRenamerFactory.EP_NAME)) {
            processor.addRenamerFactory(factory);
        }
        processor.run();

        Assert.assertEquals("renamed.proto", files[0].getName());
        Assert.assertEquals("import \"rename/import/renamed.proto\";",
                ((ProtoPsiFileRoot) files[1]).getProtoRoot().getImports().get(0).getText());
    }

}
