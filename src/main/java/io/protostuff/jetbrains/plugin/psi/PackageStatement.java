package io.protostuff.jetbrains.plugin.psi;

import static io.protostuff.jetbrains.plugin.ProtoParserDefinition.rule;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import io.protostuff.compiler.parser.ProtoParser;
import org.antlr.jetbrains.adapter.psi.AntlrPsiNode;
import org.jetbrains.annotations.NotNull;

/**
 * Package statement node.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public class PackageStatement extends AntlrPsiNode implements KeywordsContainer {

    public PackageStatement(@NotNull ASTNode node) {
        super(node);
    }

    /**
     * Returns package name.
     */
    public String getPackageName() {
        PsiElement element = findChildByType(rule(ProtoParser.RULE_packageName));
        if (element != null) {
            return element.getText();
        }
        return null;
    }
}
