package io.protostuff.jetbrains.plugin;

import static io.protostuff.jetbrains.plugin.ProtoParserDefinition.GT;
import static io.protostuff.jetbrains.plugin.ProtoParserDefinition.LCURLY;
import static io.protostuff.jetbrains.plugin.ProtoParserDefinition.LPAREN;
import static io.protostuff.jetbrains.plugin.ProtoParserDefinition.LSQUARE;
import static io.protostuff.jetbrains.plugin.ProtoParserDefinition.LT;
import static io.protostuff.jetbrains.plugin.ProtoParserDefinition.RCURLY;
import static io.protostuff.jetbrains.plugin.ProtoParserDefinition.RPAREN;
import static io.protostuff.jetbrains.plugin.ProtoParserDefinition.RSQUARE;

import com.intellij.lang.BracePair;
import com.intellij.lang.PairedBraceMatcher;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public class ProtoBraceMatcher implements PairedBraceMatcher {

    private static final BracePair[] PAIRS = {
            new BracePair(LCURLY, RCURLY, true),
            new BracePair(LPAREN, RPAREN, false),
            new BracePair(LSQUARE, RSQUARE, false),
            new BracePair(LT, GT, false)
    };


    @Override
    public BracePair[] getPairs() {
        return PAIRS;
    }

    @Override
    public boolean isPairedBracesAllowedBeforeType(@NotNull IElementType iElementType, @Nullable IElementType iElementType1) {
        return true;
    }

    @Override
    public int getCodeConstructStart(PsiFile psiFile, int i) {
        return i;
    }
}
