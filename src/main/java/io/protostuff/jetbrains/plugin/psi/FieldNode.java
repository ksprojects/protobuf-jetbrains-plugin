package io.protostuff.jetbrains.plugin.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import io.protostuff.jetbrains.plugin.ProtoParserDefinition;
import org.antlr.jetbrains.adapter.psi.ANTLRPsiNode;
import org.antlr.jetbrains.adapter.psi.IdentifierDefSubtree;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;

import static io.protostuff.jetbrains.plugin.ProtoParserDefinition.R_FIELD_MODIFIER;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public class FieldNode extends IdentifierDefSubtree implements KeywordsContainer {

    public FieldNode(@NotNull ASTNode node) {
        super(node, ProtoParserDefinition.R_NAME);
    }

    @Override
    public Collection<PsiElement> keywords() {
        ASTNode node = getNode();
        ASTNode fieldModifier = node.findChildByType(R_FIELD_MODIFIER);
        if (fieldModifier != null) {
            return Util.findKeywords(fieldModifier);
        } else {
            return Collections.emptyList();
        }
    }
}
