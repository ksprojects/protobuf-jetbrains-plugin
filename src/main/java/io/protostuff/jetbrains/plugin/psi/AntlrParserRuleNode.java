package io.protostuff.jetbrains.plugin.psi;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public interface AntlrParserRuleNode {

    int getRuleIndex();

    boolean hasSyntaxErrors();
}
