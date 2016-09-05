package io.protostuff.jetbrains.plugin.psi;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiElement;
import io.protostuff.compiler.model.Field;
import io.protostuff.compiler.parser.ProtoLexer;
import org.antlr.jetbrains.adapter.psi.ANTLRPsiNode;
import org.jetbrains.annotations.NotNull;

import static io.protostuff.compiler.parser.ProtoParser.RULE_rangeFrom;
import static io.protostuff.compiler.parser.ProtoParser.RULE_rangeTo;
import static io.protostuff.jetbrains.plugin.ProtoParserDefinition.rule;
import static io.protostuff.jetbrains.plugin.ProtoParserDefinition.token;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public class RangeNode extends ANTLRPsiNode implements KeywordsContainer {

    public RangeNode(@NotNull ASTNode node) {
        super(node);
    }

    public int getFromValue() {
        PsiElement node = findChildByType(rule(RULE_rangeFrom));
        return Util.decodeIntegerFromText(node);
    }

    public int getToValue() {
        PsiElement to = findChildByType(rule(RULE_rangeTo));
        PsiElement max = findChildByType(token(ProtoLexer.MAX));
        if (to != null) {
            return Util.decodeIntegerFromText(to);
        }
        if (max != null) {
            return Field.MAX_TAG_VALUE;
        }
        return getFromValue();
    }

    public boolean contains(int tag) {
        int fromValue = getFromValue();
        int toValue = getToValue();
        return tag >= fromValue && tag <= toValue;
    }

}
