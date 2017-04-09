package io.protostuff.jetbrains.plugin.parsing;

import com.intellij.testFramework.ParsingTestCase;
import io.protostuff.jetbrains.plugin.ProtoParserDefinition;

/**
 * Parser tests.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public class ParsingTest extends ParsingTestCase {

    public ParsingTest() {
        super("parsing", "proto", new ProtoParserDefinition());
    }

    public void testMessage() {
        doTest(true);
    }

    public void testEnum() {
        doTest(true);
    }

    public void testService() {
        doTest(true);
    }

    public void testHeaderAndFooterComments() {
        doTest(true);
    }

    @Override
    protected String getTestDataPath() {
        return "src/test/resources";
    }

    @Override
    protected boolean skipSpaces() {
        return false;
    }

    @Override
    protected boolean includeRanges() {
        return true;
    }
}
