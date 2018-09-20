package io.protostuff.jetbrains.plugin.spellchecker;

import com.google.common.collect.ImmutableList;
import com.intellij.spellchecker.inspections.SpellCheckingInspection;
import io.protostuff.jetbrains.plugin.AbstractProtobufLibraryDependentTestCase;

/**
 * Test for spell checker.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public class SpellcheckerTest extends AbstractProtobufLibraryDependentTestCase {

    @Override
    protected String getTestDataPath() {
        return "src/test/resources/";
    }


    public void testSpellchecking() {
        myFixture.configureByFile("spellchecker/SpellcheckerTest.proto");
        myFixture.enableInspections(ImmutableList.of(SpellCheckingInspection.class));
        myFixture.checkHighlighting();
    }
}
