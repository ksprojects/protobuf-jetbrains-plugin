package io.protostuff.jetbrains.plugin.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import io.protostuff.compiler.parser.ProtoParser;
import io.protostuff.jetbrains.plugin.ProtoParserDefinition;
import org.antlr.jetbrains.adapter.psi.ANTLRPsiNode;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static io.protostuff.jetbrains.plugin.ProtoParserDefinition.rule;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public class PackageStatement extends ANTLRPsiNode implements KeywordsContainer {

    public PackageStatement(@NotNull ASTNode node) {
        super(node);
    }

    public String getPackageName() {
        PsiElement element = findChildByType(rule(ProtoParser.RULE_packageName));
        if (element != null) {
            return element.getText();
        }
        return null;
    }
}
