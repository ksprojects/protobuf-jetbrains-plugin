package io.protostuff.jetbrains.plugin.folding;

import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;

/**
 * Tests for rename enum refactoring.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public class FoldingTest extends LightCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "src/test/resources/";
    }

    public void testFoldComments() {
        check();
    }

    public void testFoldHeaderComments() {
        check(true);
    }

    public void testFoldMessage() {
        check();
    }

    public void testFoldEnum() {
        check();
    }

    public void testFoldService() {
        check();
    }

    public void testFoldGroup() {
        check();
    }

    public void testFoldOneof() {
        check();
    }

    public void testFoldExtension() {
        check();
    }

    private void check() {
        check(false);
    }

    private void check(boolean checkCollapseStatus) {
        String file = "folding/" + getTestName(false) + ".proto";
        myFixture.configureByFiles(file);
        if (checkCollapseStatus) {
            myFixture.testFoldingWithCollapseStatus(getTestDataPath() + file);
        } else {
            myFixture.testFolding(getTestDataPath() + file);
        }
    }

}
