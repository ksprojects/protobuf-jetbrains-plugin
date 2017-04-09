package io.protostuff.jetbrains.plugin.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import java.util.HashMap;
import java.util.Map;
import org.antlr.jetbrains.adapter.psi.AntlrPsiNode;
import org.jetbrains.annotations.NotNull;

/**
 * Extend node.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public class ExtendNode extends AntlrPsiNode implements KeywordsContainer {

    public ExtendNode(@NotNull ASTNode node) {
        super(node);
    }

    public TypeReferenceNode getTarget() {
        return findChildByClass(TypeReferenceNode.class);
    }

    /**
     * Returns namespace for fields of this extension.
     */
    public String getNamespace() {
        PsiElement parent = getParent();
        while (parent != null) {
            if (parent instanceof DataTypeContainer) {
                DataTypeContainer message = (DataTypeContainer) parent;
                return message.getNamespace();
            }
        }
        return ".";
    }

    /**
     * Returns extension fields.
     */
    public Map<String, FieldNode> getExtensionFields() {
        Map<String, FieldNode> result = new HashMap<>();
        ExtendEntryNode[] entries = findChildrenByClass(ExtendEntryNode.class);
        for (ExtendEntryNode entry : entries) {
            for (PsiElement element : entry.getChildren()) {
                if (element instanceof FieldNode) {
                    FieldNode field = (FieldNode) element;
                    result.put(field.getFieldName(), field);
                }
            }
        }
        return result;
    }

}
