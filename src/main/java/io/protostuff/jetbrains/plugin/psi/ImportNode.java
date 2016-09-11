package io.protostuff.jetbrains.plugin.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import io.protostuff.compiler.parser.ProtoLexer;
import io.protostuff.jetbrains.plugin.ProtoParserDefinition;
import org.antlr.jetbrains.adapter.psi.ANTLRPsiNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public class ImportNode extends ANTLRPsiNode implements KeywordsContainer {

    public ImportNode(@NotNull ASTNode node) {
        super(node);
    }

    @Nullable
    public ProtoPsiFileRoot getTarget() {
        FileReferenceNode fileReference = findChildByClass(FileReferenceNode.class);
        if (fileReference != null) {
            return fileReference.getTarget();
        }
        return null;
    }

    @Nullable
    public ProtoRootNode getTargetProto() {
        ProtoPsiFileRoot fileRoot = getTarget();
        if (fileRoot != null) {
            return fileRoot.findChildByClass(ProtoRootNode.class);
        }
        return null;
    }

    public boolean isPublic() {
        return findChildrenByType(ProtoParserDefinition.token(ProtoLexer.PUBLIC)) != null;
    }

    @NotNull
    @Override
    public Collection<PsiElement> keywords() {
        return Util.findKeywords(getNode());
    }
}
