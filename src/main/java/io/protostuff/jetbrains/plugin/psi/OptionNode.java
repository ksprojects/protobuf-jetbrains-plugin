package io.protostuff.jetbrains.plugin.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import io.protostuff.compiler.parser.ProtoParser;
import io.protostuff.jetbrains.plugin.ProtoParserDefinition;
import org.antlr.jetbrains.adapter.psi.ANTLRPsiNode;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public class OptionNode extends ANTLRPsiNode implements KeywordsContainer {

    public OptionNode(@NotNull ASTNode node) {
        super(node);
    }

    @NotNull
    public String getOptionNameText() {
        PsiElement element = findChildByType(ProtoParserDefinition.rule(ProtoParser.RULE_optionName));
        if (element != null) {
            return element.getText();
        }
        return "";
    }

    @NotNull
    public String getOptionValueText() {
        PsiElement element = findChildByType(ProtoParserDefinition.rule(ProtoParser.RULE_optionValue));
        if (element != null) {
            return element.getText();
        }
        return "";
    }

    @NotNull
    @Override
    public Collection<PsiElement> keywords() {
        return Util.findKeywords(getNode());
    }
}
