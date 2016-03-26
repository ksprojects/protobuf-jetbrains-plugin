package io.protostuff.jetbrains.plugin.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import io.protostuff.jetbrains.plugin.ProtoParserDefinition;
import org.antlr.jetbrains.adapter.psi.IdentifierDefSubtree;
import org.antlr.jetbrains.adapter.psi.ScopeNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public class MessageBlockSubtree extends IdentifierDefSubtree implements ScopeNode {
    public MessageBlockSubtree(@NotNull ASTNode node) {
        super(node, ProtoParserDefinition.ID);
    }

    @Nullable
    @Override
    public PsiElement resolve(PsiNamedElement element) {
//		System.out.println(getClass().getSimpleName()+
//			                   ".resolve("+myElement.getName()+
//			                   " at "+Integer.toHexString(myElement.hashCode())+")");
//        return SymtabUtils.resolve(this, ProtoLanguage.INSTANCE,
//                element, "/proto/function/ID");
        return null;
    }
}