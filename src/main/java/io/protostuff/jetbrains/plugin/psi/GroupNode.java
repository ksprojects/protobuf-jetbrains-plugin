package io.protostuff.jetbrains.plugin.psi;

import static io.protostuff.jetbrains.plugin.ProtoParserDefinition.R_FIELD_MODIFIER;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.antlr.jetbrains.adapter.psi.AntlrPsiNode;
import org.jetbrains.annotations.NotNull;

/**
 * Group node.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public class GroupNode extends AntlrPsiNode implements KeywordsContainer {

    public GroupNode(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public Collection<PsiElement> keywords() {
        ASTNode node = getNode();
        List<PsiElement> result = new ArrayList<>();
        result.addAll(Util.findKeywords(getNode()));
        ASTNode fieldModifier = node.findChildByType(R_FIELD_MODIFIER);
        if (fieldModifier != null) {
            result.addAll(Util.findKeywords(fieldModifier));
        }
        return result;
    }
}
