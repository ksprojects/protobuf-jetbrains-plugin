package io.protostuff.jetbrains.plugin.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import io.protostuff.compiler.parser.ProtoParser;
import io.protostuff.jetbrains.plugin.ProtoParserDefinition;
import org.antlr.jetbrains.adapter.psi.ANTLRPsiNode;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public class OptionEntryNode extends ANTLRPsiNode implements KeywordsContainer {

    public OptionEntryNode(@NotNull ASTNode node) {
        super(node);
    }

    @NotNull
    public String getOptionNameText() {
        OptionNode node = findChildByClass(OptionNode.class);
        if (node != null) {
            return node.getOptionNameText();
        }
        return "";
    }

    @NotNull
    public String getOptionValueText() {
        OptionNode node = findChildByClass(OptionNode.class);
        if (node != null) {
            return node.getOptionValueText();
        }
        return "";
    }

}
