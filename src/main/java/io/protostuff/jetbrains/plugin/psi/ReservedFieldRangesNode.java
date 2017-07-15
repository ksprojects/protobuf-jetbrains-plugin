package io.protostuff.jetbrains.plugin.psi;

import com.intellij.lang.ASTNode;
import io.protostuff.compiler.parser.ProtoParser;
import org.antlr.jetbrains.adapter.psi.AntlrPsiNode;
import org.jetbrains.annotations.NotNull;

/**
 * Reserved field ranges node.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public class ReservedFieldRangesNode extends AntlrPsiNode
        implements AntlrParserRuleNode, KeywordsContainer {

    public ReservedFieldRangesNode(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public boolean hasSyntaxErrors() {
        return false;
    }

    public RangeNode[] getRanges() {
        return findChildrenByClass(RangeNode.class);
    }
}
