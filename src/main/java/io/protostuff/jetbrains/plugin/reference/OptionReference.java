package io.protostuff.jetbrains.plugin.reference;

import com.google.common.collect.ImmutableMap;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReferenceBase;
import io.protostuff.jetbrains.plugin.ProtoLanguage;
import io.protostuff.jetbrains.plugin.psi.*;
import io.protostuff.jetbrains.plugin.resources.BundledFileProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayDeque;
import java.util.Map;
import java.util.Objects;

import static io.protostuff.compiler.model.ProtobufConstants.*;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public class OptionReference extends PsiReferenceBase<PsiElement> {

    private static final Logger LOGGER = Logger.getInstance(OptionReference.class);

    // "default" field option (a special case)
    private static final String DEFAULT = "default";

    private String key;

    public OptionReference(PsiElement element, TextRange textRange) {
        super(element, textRange, true);
        key = element.getText();
    }

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


    @Nullable
    private String getTarget() {
        PsiElement element = getElement();
        while (element != null) {
            String result = TARGET_MAPPING.get(element.getClass());
            if (result != null) {
                return result;
            }
            element = element.getParent();
        }
        return null;
    }

    @Nullable
    @Override
    public PsiElement resolve() {
        if (key == null || key.isEmpty()) {
            return null;
        }
        if (key.startsWith("(")) {
            return resolveCustomOptionReference();
        } else {
            return resolveStandardOptionReference();
        }
    }

    private PsiElement resolveStandardOptionReference() {
        String targetType = getTarget();
        if (MSG_FIELD_OPTIONS.equals(targetType)
                && DEFAULT.equals(key)) {
            return resolveDefaultOptionReference();
        }
        MessageNode message = resolveType(targetType);
        if (message == null) {
            LOGGER.error("Could not resolve " + targetType);
            return null;
        }
        for (MessageField field : message.getFields()) {
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
    private PsiElement resolveDefaultOptionReference() {
        PsiElement element = getElement();
        while (element != null) {
            if (element instanceof FieldNode) {
                return element;
            }
            element = element.getParent();
        }
        return null;
    }

    @Nullable
    private PsiElement resolveCustomOptionReference() {
        // custom option
        // TODO: resolve
        return null;
    }

    private MessageNode resolveType(String qualifiedName) {
        MessageNode message = resolveTypeFromCurrentFile(qualifiedName);
        if (message == null) {
            ProtoPsiFileRoot descriptorProto = (ProtoPsiFileRoot) getBundledDescriptorProto();
            return (MessageNode) descriptorProto.findType(qualifiedName.substring(1));
        }
        return message;
    }

    private PsiFile getBundledDescriptorProto() {
        return loadInMemoryDescriptorProto();
    }

    @NotNull
    private PsiFile loadInMemoryDescriptorProto() {
        BundledFileProvider bundledFileProvider = getElement().getProject().getComponent(BundledFileProvider.class);
        return bundledFileProvider.getFile(BundledFileProvider.DESCRIPTOR_PROTO_RESOURCE,
                ProtoLanguage.INSTANCE, BundledFileProvider.DESCRIPTOR_PROTO_NAME);
    }

    @Nullable
    private MessageNode resolveTypeFromCurrentFile(String qualifiedName) {
        PsiElement protoElement = getElement();
        while (protoElement != null && !(protoElement instanceof ProtoRootNode)) {
            protoElement = protoElement.getParent();
        }
        if (protoElement == null) {
            return null;
        }
        ProtoRootNode proto = (ProtoRootNode) protoElement;
        return (MessageNode) proto.resolve(qualifiedName, new ArrayDeque<>());
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        return new Object[0];
    }
}
