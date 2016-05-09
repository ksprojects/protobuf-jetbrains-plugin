package io.protostuff.jetbrains.plugin.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import org.antlr.jetbrains.adapter.psi.ANTLRPsiNode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static io.protostuff.jetbrains.plugin.ProtoParserDefinition.R_FIELD_MODIFIER;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public class GroupNode extends ANTLRPsiNode implements KeywordsContainer {

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
