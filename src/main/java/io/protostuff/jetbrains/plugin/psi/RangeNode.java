package io.protostuff.jetbrains.plugin.psi;

import static io.protostuff.compiler.parser.ProtoParser.RULE_rangeFrom;
import static io.protostuff.compiler.parser.ProtoParser.RULE_rangeTo;
import static io.protostuff.jetbrains.plugin.ProtoParserDefinition.rule;
import static io.protostuff.jetbrains.plugin.ProtoParserDefinition.token;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import io.protostuff.compiler.model.Field;
import io.protostuff.compiler.parser.ProtoLexer;
import org.antlr.jetbrains.adapter.psi.AntlrPsiNode;
import org.jetbrains.annotations.NotNull;

/**
 * Range node for reserved field range node.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public class RangeNode extends AntlrPsiNode implements KeywordsContainer {

    public RangeNode(@NotNull ASTNode node) {
        super(node);
    }

    /**
     * Returns range start value.
     */
    public int getFromValue() {
        PsiElement node = findChildByType(rule(RULE_rangeFrom));
        return Util.decodeIntegerFromText(node);
    }

    /**
     * Returns range end value.
     */
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

    /**
     * Check if given tag is in range of this range node.
     */
    public boolean contains(int tag) {
        int fromValue = getFromValue();
        int toValue = getToValue();
        return tag >= fromValue && tag <= toValue;
    }

}
