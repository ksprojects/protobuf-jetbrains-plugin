package io.protostuff.jetbrains.plugin.psi;

import com.intellij.lang.ASTNode;
import org.antlr.jetbrains.adapter.psi.AntlrPsiNode;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public class OptionEntryNode extends AntlrPsiNode implements KeywordsContainer {

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
