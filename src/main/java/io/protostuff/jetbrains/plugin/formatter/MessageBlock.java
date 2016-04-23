package io.protostuff.jetbrains.plugin.formatter;

import com.intellij.formatting.Alignment;
import com.intellij.formatting.Block;
import com.intellij.formatting.Indent;
import com.intellij.formatting.Wrap;
import com.intellij.lang.ASTNode;
import io.protostuff.jetbrains.plugin.psi.MessageNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public class MessageBlock extends AbstractParentBlock {


    protected MessageBlock(@NotNull ASTNode node, @Nullable Alignment alignment, Indent indent) {
        super(node, null, alignment, indent);
    }

    @Override
    Block createChildBlock(ASTNode child, Wrap wrap, Alignment childAlignment, Indent childrenIndent) {
        if (child instanceof MessageNode) {
            return new MessageBlock(child, childAlignment, childrenIndent);
        } else {
            return new GenericBlock(child, childAlignment, childrenIndent);
        }
    }

}
