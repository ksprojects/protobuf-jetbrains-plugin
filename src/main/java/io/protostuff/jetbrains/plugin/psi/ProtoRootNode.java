package io.protostuff.jetbrains.plugin.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiReference;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
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
        return resolve(typeName, scopeLookupList, true);
    }

    /**
     * Resolve data type using given scope lookup list.
     */
    public DataType resolve(String typeName, Deque<String> scopeLookupList, boolean resolveInImports) {
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
        if (result != null) {
            return result;
        }
        if (!resolveInImports) {
            return null;
        }
        List<ImportNode> importNodes = getImports();
        for (ImportNode importNode : importNodes) {
            ProtoRootNode targetProto = importNode.getTargetProto();
            if (targetProto != null) {
                boolean isPublic = importNode.isPublic();
                result = targetProto.resolve(typeName, scopeLookupList, isPublic);
                if (result != null) {
                    return result;
                }

            }
        }
        return null;
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
    private PackageStatement getPackageStatement() {
        return findChildByClass(PackageStatement.class);
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
        List<ExtendNode> result = new ArrayList<>();
        result.addAll(getLocalExtensions());
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
                result.addAll(targetProto.getLocalExtensions());
                queue.addAll(targetProto.getPublicImports());
            }
        }
        return result;
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