package io.protostuff.jetbrains.plugin.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import io.protostuff.jetbrains.plugin.ProtoParserDefinition;
import org.antlr.jetbrains.adapter.psi.ANTLRPsiNode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public class ExtensionsNode extends ANTLRPsiNode implements KeywordsContainer {

    public ExtensionsNode(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public Collection<PsiElement> keywords() {
        ASTNode node = getNode();
        ASTNode toPart = node.findChildByType(ProtoParserDefinition.R_TO);
        if (toPart != null) {
            List<PsiElement> result = new ArrayList<>();
            result.addAll(Util.findKeywords(toPart));
            result.addAll(Util.findKeywords(node));
            return result;
        }
        return Util.findKeywords(node);
    }
}
