package io.protostuff.jetbrains.plugin.reference;

import static io.protostuff.compiler.model.ProtobufConstants.MSG_ENUM_OPTIONS;
import static io.protostuff.compiler.model.ProtobufConstants.MSG_ENUM_VALUE_OPTIONS;
import static io.protostuff.compiler.model.ProtobufConstants.MSG_FIELD_OPTIONS;
import static io.protostuff.compiler.model.ProtobufConstants.MSG_FILE_OPTIONS;
import static io.protostuff.compiler.model.ProtobufConstants.MSG_MESSAGE_OPTIONS;
import static io.protostuff.compiler.model.ProtobufConstants.MSG_METHOD_OPTIONS;
import static io.protostuff.compiler.model.ProtobufConstants.MSG_SERVICE_OPTIONS;

import com.google.common.collect.ImmutableMap;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import io.protostuff.jetbrains.plugin.ProtoLanguage;
import io.protostuff.jetbrains.plugin.psi.DataTypeContainer;
import io.protostuff.jetbrains.plugin.psi.EnumConstantNode;
import io.protostuff.jetbrains.plugin.psi.EnumNode;
import io.protostuff.jetbrains.plugin.psi.ExtendNode;
import io.protostuff.jetbrains.plugin.psi.FieldNode;
import io.protostuff.jetbrains.plugin.psi.MessageField;
import io.protostuff.jetbrains.plugin.psi.MessageNode;
import io.protostuff.jetbrains.plugin.psi.ProtoPsiFileRoot;
import io.protostuff.jetbrains.plugin.psi.ProtoRootNode;
import io.protostuff.jetbrains.plugin.psi.RpcMethodNode;
import io.protostuff.jetbrains.plugin.psi.ServiceNode;
import io.protostuff.jetbrains.plugin.resources.BundledFileProvider;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Field reference provider.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public class FieldReferenceProviderImpl implements FieldReferenceProvider {

    private static final Logger LOGGER = Logger.getInstance(FieldReferenceProviderImpl.class);
    // "default" field option (a special case)
    private static final String DEFAULT = "default";
    private static final Map<Class<? extends PsiElement>, String> TARGET_MAPPING
            = ImmutableMap.<Class<? extends PsiElement>, String>builder()
            .put(FieldNode.class, MSG_FIELD_OPTIONS)
            .put(MessageNode.class, MSG_MESSAGE_OPTIONS)
            .put(EnumConstantNode.class, MSG_ENUM_VALUE_OPTIONS)
            .put(EnumNode.class, MSG_ENUM_OPTIONS)
            .put(RpcMethodNode.class, MSG_METHOD_OPTIONS)
            .put(ServiceNode.class, MSG_SERVICE_OPTIONS)
            .put(ProtoRootNode.class, MSG_FILE_OPTIONS)
            .build();

    public FieldReferenceProviderImpl(Project project) {
        this.project = project;
    }

    @Override
    public PsiReference[] getReferencesByElement(PsiElement element, String text) {
        PsiElement target = resolve(element, text);
        return new PsiReference[]{
                new OptionReference(element, new TextRange(0, text.length()), target)
        };
    }

    private final Project project;

    @Nullable
    private String getTarget(PsiElement element) {
        while (element != null) {
            String result = TARGET_MAPPING.get(element.getClass());
            if (result != null) {
                return result;
            }
            element = element.getParent();
        }
        return null;
    }

    /**
     * Resolve target PsiElement for given key.
     */
    @Nullable
    public PsiElement resolve(PsiElement element, String key) {
        if (key == null || key.isEmpty()) {
            return null;
        }
        String targetType = getTarget(element);
        MessageNode message = resolveType(element, targetType);
        if (message == null) {
            LOGGER.error("Could not resolve " + targetType);
            return null;
        }
        if (key.startsWith("(")) {
            return resolveCustomOptionReference(element, message, key);
        } else {
            return resolveStandardOptionReference(element, message, key);
        }
    }

    private ProtoPsiFileRoot getProtoRoot(PsiElement element) {
        PsiElement parent = element.getParent();
        while (!(parent instanceof ProtoPsiFileRoot)) {
            parent = parent.getParent();
        }
        return (ProtoPsiFileRoot) parent;
    }

    private PsiElement resolveStandardOptionReference(PsiElement sourceElement, MessageNode target, String key) {
        if (MSG_FIELD_OPTIONS.equals(target.getQualifiedName())
                && DEFAULT.equals(key)) {
            return resolveDefaultOptionReference(sourceElement);
        }
        for (MessageField field : target.getFields()) {
            if (Objects.equals(key, field.getFieldName())) {
                return field;
            }
        }
        return null;
    }

    /**
     * "default" field option is a special case: it is not defined
     * in the {@code google/protobuf/descriptor.proto} and it cannot
     * be treated like other options, as its type depends on a field's
     * type.
     * <p>
     * In order to implement value validation, we have to return the
     * field where this option was applied.
     */
    private PsiElement resolveDefaultOptionReference(PsiElement element) {
        while (element != null) {
            if (element instanceof FieldNode) {
                return element;
            }
            element = element.getParent();
        }
        return null;
    }

    @Nullable
    private PsiElement resolveCustomOptionReference(PsiElement element, MessageNode target, String key) {
        ProtoPsiFileRoot protoRoot = getProtoRoot(element);
        DataTypeContainer container = getContainer(element);
        Deque<String> scopeLookupList = TypeReference.createScopeLookupList(container);
        // case 1: (.package.field)
        // case 2: (.package.field).field
        // case 3: (.package.field).(.package.field)
        Collection<ExtendNode> extensions = protoRoot.getExtenstions(target);
        Map<String, FieldNode> extensionFields = new HashMap<>();
        for (ExtendNode extension : extensions) {
            for (FieldNode field : extension.getExtensionFields().values()) {
                extensionFields.put(extension.getNamespace() + field.getFieldName(), field);
            }
        }
        String ref = key.substring(1, key.length() - 1);
        if (ref.startsWith(".")) {
            return extensionFields.get(ref);
        } else {
            for (String scope : scopeLookupList) {
                FieldNode field = extensionFields.get(scope + ref);
                if (field != null) {
                    return field;
                }
            }
        }
        return null;
    }

    @NotNull
    private DataTypeContainer getContainer(PsiElement element) {
        PsiElement parent = element.getParent();
        while (!(parent instanceof DataTypeContainer)) {
            parent = parent.getParent();
        }
        return (DataTypeContainer) parent;
    }

    private MessageNode resolveType(PsiElement element, String qualifiedName) {
        MessageNode message = resolveTypeFromCurrentFile(element, qualifiedName);
//        if (message == null) {
//            ProtoPsiFileRoot descriptorProto = (ProtoPsiFileRoot) getBundledDescriptorProto();
//            return (MessageNode) descriptorProto.findType(qualifiedName.substring(1));
//        }
        return message;
    }

    private PsiFile getBundledDescriptorProto() {
        return loadInMemoryDescriptorProto();
    }

    @NotNull
    private PsiFile loadInMemoryDescriptorProto() {
        BundledFileProvider bundledFileProvider = project.getComponent(BundledFileProvider.class);
        return bundledFileProvider.getFile(BundledFileProvider.DESCRIPTOR_PROTO_RESOURCE,
                ProtoLanguage.INSTANCE, BundledFileProvider.DESCRIPTOR_PROTO_NAME);
    }

    @Nullable
    private MessageNode resolveTypeFromCurrentFile(PsiElement element, String qualifiedName) {
        PsiElement protoElement = element;
        while (protoElement != null && !(protoElement instanceof ProtoRootNode)) {
            protoElement = protoElement.getParent();
        }
        if (protoElement == null) {
            return null;
        }
        ProtoRootNode proto = (ProtoRootNode) protoElement;
        return (MessageNode) proto.resolve(qualifiedName, new ArrayDeque<>());
    }

}
