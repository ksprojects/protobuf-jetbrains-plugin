package io.protostuff.jetbrains.plugin.view.structure;

import static com.intellij.testFramework.PlatformTestUtil.assertTreeEqual;

import com.intellij.testFramework.PlatformTestUtil;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;

/**
 * Tests for structure view.
 *
 * @author Kostiantyn Shchepanovskyi
 */
@SuppressWarnings("unchecked")
public class StructureViewTest extends LightCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "src/test/resources";
    }

    public void testStructureView() throws Exception {
        myFixture.configureByFile("structure/StructureViewTest.proto");
        doTest("-StructureViewTest\n"
                + " -Message\n"
                + "  field\n"
                + " -Enum\n"
                + "  VAL0", true);
    }

    private void doTest(final String expected, final boolean showFields) {
        myFixture.testStructureView(component -> {
            component.setActionActive("SHOW_FIELDS", showFields);
            PlatformTestUtil.waitWhileBusy(component.getTree());
            assertTreeEqual(component.getTree(), expected);
        });
    }

}
