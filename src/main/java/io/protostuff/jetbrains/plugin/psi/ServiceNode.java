package io.protostuff.jetbrains.plugin.psi;

import com.google.common.base.MoreObjects;
import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.ItemPresentationProviders;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import io.protostuff.compiler.parser.ProtoParser;
import io.protostuff.jetbrains.plugin.ProtoParserDefinition;
import org.antlr.jetbrains.adapter.psi.IdentifierDefSubtree;
import org.antlr.jetbrains.adapter.psi.ScopeNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public class ServiceNode
        extends IdentifierDefSubtree
        implements ScopeNode, KeywordsContainer, ProtoType {

    public ServiceNode(@NotNull ASTNode node) {
        super(node, ProtoParserDefinition.rule(ProtoParser.RULE_serviceName));
    }

    @Nullable
    @Override
    public PsiElement resolve(PsiNamedElement element) {
        return null;
    }

    public List<RpcMethodNode> getRpcMethods() {
        RpcMethodNode[] nodes = findChildrenByClass(RpcMethodNode.class);
        return Arrays.asList(nodes);
    }

    @Override
    public ItemPresentation getPresentation() {
        return ItemPresentationProviders.getItemPresentation(this);
    }

    @NotNull
    @Override
    public String getName() {
        String name = super.getName();
        return MoreObjects.firstNonNull(name, "<undefined>");
    }

    @NotNull
    @Override
    public String getFullName() {
        PsiElement parent = getParent();
        if (parent instanceof ProtoRootNode) {
            ProtoRootNode proto = (ProtoRootNode) parent;
            String packageName = proto.getPackageName();
            if (packageName.isEmpty()) {
                return getName();
            }
            return packageName + "." + getName();
        }
        return "<undefined>";
    }
}