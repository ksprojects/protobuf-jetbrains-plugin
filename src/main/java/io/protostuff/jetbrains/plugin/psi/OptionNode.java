package io.protostuff.jetbrains.plugin.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import io.protostuff.compiler.parser.ProtoParser;
import io.protostuff.jetbrains.plugin.ProtoParserDefinition;
import org.antlr.jetbrains.adapter.psi.AntlrPsiNode;
import org.jetbrains.annotations.NotNull;

/**
 * Option node.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public class OptionNode extends AntlrPsiNode implements KeywordsContainer {

    public OptionNode(@NotNull ASTNode node) {
        super(node);
    }

    /**
     * Returns option name text.
     */
    @NotNull
    public String getOptionNameText() {
        PsiElement element = findChildByType(ProtoParserDefinition.rule(ProtoParser.RULE_fieldRerefence));
        if (element != null) {
            return element.getText();
        }
        return "";
    }

    /**
     * Returns option value text.
     */
    @NotNull
    public String getOptionValueText() {
        PsiElement element = findChildByType(ProtoParserDefinition.rule(ProtoParser.RULE_optionValue));
        if (element != null) {
            return element.getText();
        }
        return "";
    }

}
