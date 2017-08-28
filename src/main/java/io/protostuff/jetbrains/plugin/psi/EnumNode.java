package io.protostuff.jetbrains.plugin.psi;

import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.ItemPresentationProviders;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.IncorrectOperationException;
import io.protostuff.jetbrains.plugin.psi.stubs.EnumStub;
import io.protostuff.jetbrains.plugin.psi.stubs.MessageStub;
import java.util.Arrays;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * Enum node.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public class EnumNode extends DataType {

    public static final String ALLOW_ALIAS = "allow_alias";
    public static final String TRUE = "true";

    public EnumNode(@NotNull ASTNode node) {
        super(node);
    }

    public EnumNode(EnumStub stub, IStubElementType type) {
        super(stub, type);
    }

    public EnumNode(EnumStub stub, IElementType type, ASTNode node) {
        super(stub, type, node);
    }

    @Override
    public PsiElement setName(@NotNull String name) throws IncorrectOperationException {
        return GenericNameNode.setName(this, name);
    }

    @Override
    public ItemPresentation getPresentation() {
        return ItemPresentationProviders.getItemPresentation(this);
    }

    public List<EnumConstantNode> getConstants() {
        EnumConstantNode[] nodes = findChildrenByClass(EnumConstantNode.class);
        return Arrays.asList(nodes);
    }

    /**
     * Check if this enum allows aliases (option "allow_alias" should be set).
     */
    public boolean allowAlias() {
        OptionEntryNode[] options = findChildrenByClass(OptionEntryNode.class);
        for (OptionEntryNode optionEntry : options) {
            String name = optionEntry.getOptionNameText();
            String value = optionEntry.getOptionValueText();
            if (ALLOW_ALIAS.equals(name)
                    && TRUE.equals(value)) {
                return true;
            }
        }
        return false;
    }
}