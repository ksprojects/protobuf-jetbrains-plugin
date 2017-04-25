package io.protostuff.jetbrains.plugin.reference;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import io.protostuff.jetbrains.plugin.psi.DataType;
import io.protostuff.jetbrains.plugin.psi.DataTypeContainer;
import io.protostuff.jetbrains.plugin.psi.ProtoRootNode;
import io.protostuff.jetbrains.plugin.psi.TypeReferenceNode;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Type reference provider.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public class TypeReferenceProviderImpl implements TypeReferenceProvider {

    private static final Logger LOGGER = Logger.getInstance(TypeReferenceProviderImpl.class);

    private final Project project;

    public TypeReferenceProviderImpl(Project project) {
        this.project = project;
    }

    @NotNull
    @Override
    public PsiReference[] getReferencesByElement(TypeReferenceNode typeReferenceNode) {
        DataType target = resolveInScope(typeReferenceNode);
        if (target == null) {
            return new PsiReference[]{
                    new TypeReference(typeReferenceNode, TextRange.create(0, typeReferenceNode.getTextLength()), null)
            };
        }
        String text = typeReferenceNode.getText();
        PsiElement element = target;
        List<PsiReference> refs = new ArrayList<>();
        while (element != null && text.length() > 0) {
            if (element instanceof DataType) {
                DataType type = (DataType) element;
                String name = type.getName();
                if (name != null && text.endsWith(name)) {
                    // a.b.c | c
                    TextRange textRange = TextRange.create(text.length() - name.length(), text.length());
                    TypeReference ref = new TypeReference(typeReferenceNode, textRange, type);
                    refs.add(ref);
                    if (name.length() < text.length()) {
                        text = text.substring(0, text.length() - name.length() - 1);
                    } else {
                        text = "";
                    }
                }
            }
            if (element instanceof ProtoRootNode) {
                ProtoRootNode root = (ProtoRootNode) element;
                String pkg = root.getPackageName();
                if (pkg.endsWith(text)) {
                    TextRange textRange = TextRange.create(0, text.length());
                    TypeReference ref = new TypeReference(typeReferenceNode, textRange, root.getPackageStatement());
                    refs.add(ref);
                    text = "";
                }
            }
            element = element.getParent();
        }
        return refs.toArray(new PsiReference[refs.size()]);
    }

    @Nullable
    private static DataType resolveInScope(TypeReferenceNode typeReferenceNode) {
        DataTypeContainer scope = getScopeElement(typeReferenceNode);
        ProtoRootNode proto = getProtoRoot(typeReferenceNode);
        if (scope == null || proto == null) {
            return null;
        }
        Deque<String> scopeLookupList = createScopeLookupList(scope);
        return proto.resolve(typeReferenceNode.getText(), scopeLookupList);
    }

    @Nullable
    private static ProtoRootNode getProtoRoot(TypeReferenceNode typeReferenceNode) {
        PsiElement protoElement = typeReferenceNode;
        while (protoElement != null && !(protoElement instanceof ProtoRootNode)) {
            protoElement = protoElement.getParent();
        }
        return (ProtoRootNode) protoElement;
    }

    @Nullable
    private static DataTypeContainer getScopeElement(TypeReferenceNode typeReferenceNode) {
        PsiElement element = typeReferenceNode;
        while (element != null && !(element instanceof DataTypeContainer)) {
            element = element.getParent();
        }
        return (DataTypeContainer) element;
    }

    /**
     * Type name resolution in the protocol buffer language works like C++: first
     * the innermost scope is searched, then the next-innermost, and so on, with
     * each package considered to be "inner" to its parent package.
     */
    public static Deque<String> createScopeLookupList(DataTypeContainer container) {
        String namespace = container.getNamespace();
        Deque<String> scopeLookupList = new ArrayDeque<>();
        int end = 0;
        while (end >= 0) {
            end = namespace.indexOf('.', end);
            if (end >= 0) {
                end++;
                String scope = namespace.substring(0, end);
                scopeLookupList.addFirst(scope);
            }
        }
        return scopeLookupList;
    }
}
