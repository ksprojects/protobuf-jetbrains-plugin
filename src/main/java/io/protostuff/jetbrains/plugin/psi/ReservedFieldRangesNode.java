package io.protostuff.jetbrains.plugin.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import io.protostuff.compiler.parser.ProtoParser;
import org.antlr.jetbrains.adapter.psi.ANTLRPsiNode;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public class ReservedFieldRangesNode extends ANTLRPsiNode
        implements AntlrParserRuleNode, KeywordsContainer {

    public static final int RULE_INDEX = ProtoParser.RULE_reservedFieldRanges;

    public ReservedFieldRangesNode(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public int getRuleIndex() {
        return RULE_INDEX;
    }

    @Override
    public boolean hasSyntaxErrors() {
        return false;
    }

    public RangeNode[] getRanges() {
        return findChildrenByClass(RangeNode.class);
    }

    @NotNull
    @Override
    public Collection<PsiElement> keywords() {
        return Util.findKeywords(getNode());
    }
}
