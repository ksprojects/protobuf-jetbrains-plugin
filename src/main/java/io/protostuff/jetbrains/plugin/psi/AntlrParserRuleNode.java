package io.protostuff.jetbrains.plugin.psi;

/**
 * Base class for nodes that are mapped to corresponding ANTLR parser rule nodes.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public interface AntlrParserRuleNode {

    boolean hasSyntaxErrors();
}
