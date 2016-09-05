package io.protostuff.jetbrains.plugin.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import io.protostuff.compiler.parser.ProtoParser;
import org.antlr.jetbrains.adapter.psi.ANTLRPsiNode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static io.protostuff.compiler.parser.ProtoParser.RULE_reservedFieldName;
import static io.protostuff.compiler.parser.Util.removeFirstAndLastChar;
import static io.protostuff.jetbrains.plugin.ProtoParserDefinition.rule;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public class ReservedFieldNamesNode extends ANTLRPsiNode
        implements AntlrParserRuleNode, KeywordsContainer {

    public static final int RULE_INDEX = ProtoParser.RULE_reservedFieldNames;
    private Boolean syntaxErrors;

    public ReservedFieldNamesNode(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public int getRuleIndex() {
        return RULE_INDEX;
    }

    @Override
    public boolean hasSyntaxErrors() {
        if (syntaxErrors == null) {
            syntaxErrors = Util.checkForSyntaxErrors(this);
        }
        return syntaxErrors;
    }

    public List<String> getNames() {
        List<PsiElement> nodes = findChildrenByType(rule(RULE_reservedFieldName));
        List<String> result = new ArrayList<>();
        for (PsiElement node : nodes) {
            String s = removeFirstAndLastChar(node.getText());
            result.add(s);
        }
        return result;
    }

}
