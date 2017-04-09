package io.protostuff.jetbrains.plugin.psi;

import com.intellij.lang.ASTNode;
import org.antlr.jetbrains.adapter.psi.AntlrPsiNode;
import org.jetbrains.annotations.NotNull;

/**
 * TODO: unused, delete.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public class EnumBlockEntryNode extends AntlrPsiNode implements KeywordsContainer {

    public EnumBlockEntryNode(@NotNull ASTNode node) {
        super(node);
    }

}