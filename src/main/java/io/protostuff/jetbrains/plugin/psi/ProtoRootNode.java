package io.protostuff.jetbrains.plugin.psi;

import static io.protostuff.jetbrains.plugin.psi.ProtoRootNode.ResolveMode.RESOLVE_ALL;
import static io.protostuff.jetbrains.plugin.psi.ProtoRootNode.ResolveMode.RESOLVE_FIRST;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.antlr.jetbrains.adapter.psi.AntlrPsiNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Proto root node.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public class ProtoRootNode extends AntlrPsiNode implements KeywordsContainer, DataTypeContainer {

    public ProtoRootNode(@NotNull ASTNode node) {
        super(node);
    }

    /**
     * Returns a package name for given proto file.
     * If package is not set, returns an empty string.
     */
    @NotNull
    public String getPackageName() {
        PackageStatement packageStatement = getPackageStatement();
        if (packageStatement != null) {
            return packageStatement.getPackageName();
        }
        return "";
    }


    /**
     * Resolve data type using given scope lookup list.
     */
    public DataType resolve(String typeName, Deque<String> scopeLookupList) {
        Optional<DataType> result = resolveFirst(proto -> proto.resolveLocal(typeName, scopeLookupList));
        return result.orElse(null);
    }

    private DataType resolveLocal(String typeName, Deque<String> scopeLookupList) {
        DataType result = null;
        // A leading '.' (for example, .foo.bar.Baz) means to start from the outermost scope
        if (typeName.startsWith(".")) {
            result = resolveByQualifiedName(typeName);
        } else {
            for (String scope : scopeLookupList) {
                String fullTypeName = scope + typeName;
                DataType type = resolveByQualifiedName(fullTypeName);
                if (type != null) {
                    result = type;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Get imports declared in this proto file.
     */
    @NotNull
    public List<ImportNode> getImports() {
        return Arrays.asList(findChildrenByClass(ImportNode.class));
    }

    /**
     * Get public imports declared in this proto file.
     */
    @NotNull
    public List<ImportNode> getPublicImports() {
        return Arrays.stream(findChildrenByClass(ImportNode.class))
                .filter(ImportNode::isPublic)
                .collect(Collectors.toList());
    }


    private DataType resolveByQualifiedName(String qualifiedName) {
        String prefix = getCurrentProtoPrefix();
        if (qualifiedName.startsWith(prefix)) {
            return resolveRecursive(this, qualifiedName);
        }
        return null;
    }

    @NotNull
    private String getCurrentProtoPrefix() {
        String packageName = getPackageName();
        if ("".equals(packageName)) {
            return ".";
        }
        return "." + getPackageName() + ".";
    }

    private DataType resolveRecursive(DataTypeContainer container, String targetName) {
        Collection<DataType> childrenTypes = container.getDeclaredDataTypes();
        for (DataType type : childrenTypes) {
            String qualifiedName = type.getQualifiedName();
            if (Objects.equals(targetName, qualifiedName)) {
                return type;
            }
            if (type instanceof DataTypeContainer && targetName.startsWith(qualifiedName + ".")) {
                return resolveRecursive((DataTypeContainer) type, targetName);
            }
        }
        return null;
    }

    @Nullable
    public PackageStatement getPackageStatement() {
        return findChildByClass(PackageStatement.class);
    }

    @Nullable
    public SyntaxStatement getSyntaxStatement() {
        return findChildByClass(SyntaxStatement.class);
    }

    /**
     * Returns syntax value for this file, or default to {@link Syntax#DEFAULT}.
     */
    public Syntax getSyntax() {
        SyntaxStatement statement = getSyntaxStatement();
        if (statement != null) {
            String syntaxName = statement.getSyntaxName();
            return Syntax.forName(syntaxName).orElse(Syntax.DEFAULT);
        }
        return Syntax.DEFAULT;
    }

    @Override
    public String getNamespace() {
        String packageName = getPackageName();
        if (packageName.isEmpty()) {
            return ".";
        }
        return "." + packageName + ".";
    }

    @Override
    public Collection<DataType> getDeclaredDataTypes() {
        return Arrays.asList(findChildrenByClass(DataType.class));
    }

    @Override
    public Collection<ExtendNode> getDeclaredExtensions() {
        return Arrays.asList(findChildrenByClass(ExtendNode.class));
    }

    public Collection<ProtoType> getDeclaredTypes() {
        return Arrays.asList(findChildrenByClass(ProtoType.class));
    }

    /**
     * Returns all extension for given target message.
     */
    public Collection<ExtendNode> getExtenstions(MessageNode target) {
        return getExtensions().stream()
                .filter(node -> {
                    TypeReferenceNode extendTarget = node.getTarget();
                    if (extendTarget == null) {
                        return false;
                    }
                    PsiReference extendTargetReference = extendTarget.getReference();
                    return extendTargetReference.isReferenceTo(target);
                })
                .collect(Collectors.toList());
    }

    /**
     * Returns all extensions visible inside of this proto file.
     */
    public Collection<ExtendNode> getExtensions() {
        return resolveAll(ProtoRootNode::getLocalExtensions);
    }

    enum ResolveMode {
        RESOLVE_FIRST,
        RESOLVE_ALL
    }

    @NotNull
    private <T extends PsiElement> Optional<T> resolveFirst(Function<ProtoRootNode, T> extractor) {
        Collection<T> elements = resolveElementsImpl(RESOLVE_FIRST, proto -> {
            T result = extractor.apply(proto);
            if (result == null) {
                return Collections.emptyList();
            }
            return Collections.singletonList(result);
        });
        if (elements.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(elements.iterator().next());
    }

    @NotNull
    private <T extends PsiElement> Collection<T> resolveAll(Function<ProtoRootNode, Collection<T>> extractor) {
        return resolveElementsImpl(RESOLVE_ALL, extractor);
    }

    @NotNull
    private <T extends PsiElement> Collection<T> resolveElementsImpl(ResolveMode mode, Function<ProtoRootNode, Collection<T>> extractor) {
        List<T> result = new ArrayList<>();
        result.addAll(extractor.apply(this));
        if (stopLookup(mode, result)) {
            return result;
        }
        Queue<ImportNode> queue = new ArrayDeque<>();
        queue.addAll(getImports());
        Set<ProtoRootNode> processedProtos = new HashSet<>();
        while (!queue.isEmpty()) {
            ImportNode importNode = queue.poll();
            ProtoRootNode targetProto = importNode.getTargetProto();
            if (processedProtos.contains(targetProto)) {
                // do not enter into endless loop
                // if proto files refer to each other
                continue;
            }
            processedProtos.add(targetProto);
            if (targetProto != null) {
                result.addAll(extractor.apply(targetProto));
                queue.addAll(targetProto.getPublicImports());
                if (stopLookup(mode, result)) {
                    break;
                }
            }
        }
        return result;
    }

    private <T extends PsiElement> boolean stopLookup(ResolveMode mode, List<T> result) {
        return mode == ResolveMode.RESOLVE_FIRST && !result.isEmpty();
    }

    /**
     * Returns local extensions declared in this proto file.
     */
    public Collection<ExtendNode> getLocalExtensions() {
        List<ExtendNode> result = new ArrayList<>();
        Deque<DataTypeContainer> queue = new ArrayDeque<>();
        queue.push(this);
        while (!queue.isEmpty()) {
            DataTypeContainer container = queue.poll();
            Collection<ExtendNode> extensions = container.getDeclaredExtensions();
            result.addAll(extensions);
            for (DataType dataType : container.getDeclaredDataTypes()) {
                if (dataType instanceof DataTypeContainer) {
                    DataTypeContainer childContainer = (DataTypeContainer) dataType;
                    queue.push(childContainer);
                }
            }
        }
        return result;
    }
}