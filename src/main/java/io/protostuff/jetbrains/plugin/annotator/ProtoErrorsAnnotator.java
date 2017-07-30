package io.protostuff.jetbrains.plugin.annotator;

import static io.protostuff.jetbrains.plugin.ProtostuffBundle.message;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableSet;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.lang.ASTNode;
import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import io.protostuff.compiler.model.Field;
import io.protostuff.compiler.model.ProtobufConstants;
import io.protostuff.jetbrains.plugin.psi.AntlrParserRuleNode;
import io.protostuff.jetbrains.plugin.psi.EnumConstantNode;
import io.protostuff.jetbrains.plugin.psi.EnumNode;
import io.protostuff.jetbrains.plugin.psi.ExtendNode;
import io.protostuff.jetbrains.plugin.psi.FieldLabel;
import io.protostuff.jetbrains.plugin.psi.FieldNode;
import io.protostuff.jetbrains.plugin.psi.FieldReferenceNode;
import io.protostuff.jetbrains.plugin.psi.FileReferenceNode;
import io.protostuff.jetbrains.plugin.psi.GroupNode;
import io.protostuff.jetbrains.plugin.psi.MessageField;
import io.protostuff.jetbrains.plugin.psi.MessageNode;
import io.protostuff.jetbrains.plugin.psi.OneOfNode;
import io.protostuff.jetbrains.plugin.psi.OptionNode;
import io.protostuff.jetbrains.plugin.psi.ProtoPsiFileRoot;
import io.protostuff.jetbrains.plugin.psi.ProtoRootNode;
import io.protostuff.jetbrains.plugin.psi.RangeNode;
import io.protostuff.jetbrains.plugin.psi.RpcMethodNode;
import io.protostuff.jetbrains.plugin.psi.ServiceNode;
import io.protostuff.jetbrains.plugin.psi.Syntax;
import io.protostuff.jetbrains.plugin.psi.TypeReferenceNode;
import io.protostuff.jetbrains.plugin.reference.FieldReferenceProviderImpl;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Errors annotator.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public class ProtoErrorsAnnotator implements Annotator {

    private static final int MIN_TAG = 1;
    private static final int MAX_TAG = Field.MAX_TAG_VALUE;
    private static final int SYS_RESERVED_START = 19000;
    private static final int SYS_RESERVED_END = 19999;

    private static final Set<String> VALID_PROTO3_EXTENDEES = ImmutableSet.of(
            ProtobufConstants.MSG_ENUM_OPTIONS,
            ProtobufConstants.MSG_ENUM_VALUE_OPTIONS,
            ProtobufConstants.MSG_FIELD_OPTIONS,
            ProtobufConstants.MSG_FILE_OPTIONS,
            ProtobufConstants.MSG_MESSAGE_OPTIONS,
            ProtobufConstants.MSG_METHOD_OPTIONS,
            ProtobufConstants.MSG_ONEOF_OPTIONS,
            ProtobufConstants.MSG_SERVICE_OPTIONS
    );
    private AnnotationHolder holder;

    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        boolean check = hasErrors(element);
        if (!check) {
            this.holder = holder;
            ProtoRootNode root = getProtoRoot(element);
            if (root != null) {
                Syntax syntax = root.getSyntax();
                if (element instanceof MessageNode) {
                    MessageNode message = (MessageNode) element;
                    Collection<MessageField> fields = message.getFields();
                    checkInvalidFieldTags(fields);
                    checkDuplicateFieldTags(fields);
                    checkDuplicateFieldNames(fields);
                    checkReservedFieldTags(message, fields);
                    checkReservedFieldNames(message, fields);
                }
                if (element instanceof GroupNode) {
                    checkGroupNodeDeprecated((GroupNode) element, syntax);
                }
                if (element instanceof ExtendNode) {
                    checkExtendNodeDeprecated((ExtendNode) element, syntax);
                }
                if (element instanceof FieldNode) {
                    FieldNode field = (FieldNode) element;
                    checkFieldLabel(field, syntax);
                }
                if (element instanceof OptionNode) {
                    checkDefaultValue((OptionNode) element, syntax);
                }
                if (element instanceof EnumNode) {
                    EnumNode anEnum = (EnumNode) element;
                    List<EnumConstantNode> constants = anEnum.getConstants();
                    checkDuplicateEnumConstantNames(constants);
                    checkDuplicateEnumConstantValues(anEnum, constants);
                    checkFirstEnumConstantValueIsZero(anEnum, constants, syntax);
                }
                if (element instanceof ServiceNode) {
                    ServiceNode service = (ServiceNode) element;
                    List<RpcMethodNode> rpcMethods = service.getRpcMethods();
                    checkDuplicateServiceMethodNames(rpcMethods);
                }
                if (element instanceof TypeReferenceNode
                        || element instanceof FieldReferenceNode) {
                    checkReference(element);
                }
                if (element instanceof FileReferenceNode) {
                    FileReferenceNode fileReferenceNode = (FileReferenceNode) element;
                    checkFileReference(fileReferenceNode);
                }
            }
            this.holder = null;
        }
    }

    private void checkFileReference(FileReferenceNode element) {
        ProtoPsiFileRoot file = element.getTarget();
        if (file == null) {
            String message = message("error.file.not.found");
            markError(element.getNode(), null, message);
        }
    }

    private void checkReference(PsiElement element) {
        PsiReference ref = element.getReference();
        if (ref == null || ref.isSoft()) {
            return;
        }
        if (ref.resolve() == null) {
            String message = message("error.unresolved.reference");
            markError(element.getNode(), null, message);
        }
    }

    private void checkFirstEnumConstantValueIsZero(EnumNode anEnum, List<EnumConstantNode> constants, Syntax syntax) {
        if (syntax != Syntax.PROTO3) {
            return;
        }
        if (constants.isEmpty()) {
            return;
        }
        EnumConstantNode first = constants.get(0);
        if (first.getConstantValue() != 0) {
            String message = message("error.first.enum.value.should.be.zero");
            markError(first.getConstantValueNode(), null, message);
        }
    }

    private void checkExtendNodeDeprecated(ExtendNode element, Syntax syntax) {
        PsiElement extendee = null;
        TypeReferenceNode target = element.getTarget();
        if (target != null) {
            PsiReference reference = target.getReference();
            if (reference != null) {
                extendee = reference.resolve();
            }
        }
        if (!(extendee instanceof MessageNode)) {
            String message = message("error.extensions.not.supported");
            markError(element.getNode(), element.getTargetNode(), message);
            return;
        }
        if (syntax == Syntax.PROTO3) {
            MessageNode message = (MessageNode) extendee;
            String qualifiedName = message.getQualifiedName();
            if (!VALID_PROTO3_EXTENDEES.contains(qualifiedName)) {
                String text = message("error.extensions.not.supported");
                markError(element.getNode(), element.getTargetNode(), text);
            }
        }
    }

    private void checkGroupNodeDeprecated(GroupNode element, Syntax syntax) {
        if (syntax != Syntax.PROTO3) {
            return;
        }
        String message = message("error.groups.not.supported");
        markError(element.getNode(), null, message);
    }

    private void checkDefaultValue(@NotNull OptionNode option, Syntax syntax) {
        if (syntax != Syntax.PROTO3) {
            return;
        }
        PsiElement optionEntry = option.getParent();
        if (optionEntry != null) {
            PsiElement field = optionEntry.getParent();
            if (field instanceof MessageField) {
                String optionName = option.getOptionNameText();
                if (Objects.equals(optionName, FieldReferenceProviderImpl.DEFAULT)) {
                    String message = message("error.default.value.not.supported");
                    markError(option.getNode(), null, message);
                }
            }
        }
    }


    private void checkFieldLabel(FieldNode field, Syntax syntax) {
        if (field.getParent() instanceof OneOfNode) {
            checkOneofFieldLabel(field);
        } else {
            switch (syntax) {
                case PROTO2:
                    checkFieldLabelProto2(field);
                    break;
                case PROTO3:
                    checkFieldLabelProto3(field);
                    break;
                default:
                    throw new IllegalStateException(String.valueOf(syntax));
            }
        }
    }

    private void checkOneofFieldLabel(FieldNode field) {
        ASTNode fieldLabelNode = field.getFieldLabelNode();
        if (fieldLabelNode != null) {
            String message = message("error.illegal.oneof.field.label");
            markError(field.getFieldLabelNode(), null, message);
        }
    }

    private void checkFieldLabelProto2(FieldNode field) {
        ASTNode fieldLabelNode = field.getFieldLabelNode();
        if (fieldLabelNode == null) {
            String message = message("error.missing.field.label");
            markError(field.getNode(), null, message);
        }
    }

    private void checkFieldLabelProto3(FieldNode field) {
        Optional<FieldLabel> fieldLabel = field.getFieldLabel();
        fieldLabel.ifPresent(label -> {
            if (label == FieldLabel.OPTIONAL
                    || label == FieldLabel.REQUIRED) {
                String message = message("error.illegal.field.label", label.getName());
                markError(field.getFieldLabelNode(), null, message);
            }
        });
    }

    private ProtoRootNode getProtoRoot(PsiElement element) {
        PsiElement tmp = element;
        while (tmp != null && !(tmp instanceof ProtoRootNode)) {
            tmp = tmp.getParent();
        }
        return (ProtoRootNode) tmp;
    }

    private void checkDuplicateServiceMethodNames(List<RpcMethodNode> rpcMethods) {
        Map<String, RpcMethodNode> methodByName = new HashMap<>();
        for (RpcMethodNode methods : rpcMethods) {
            String name = methods.getMethodName();
            if (methodByName.containsKey(name)) {
                String message = message("error.duplicate.method.name", name);
                markError(methods.getNode(), methodByName.get(name).getMethodNameNode(), message);
                markError(methods.getNode(), methods.getMethodNameNode(), message);
            }
            methodByName.put(name, methods);
        }
    }

    private void checkDuplicateEnumConstantValues(EnumNode anEnum, List<EnumConstantNode> constants) {
        if (anEnum.allowAlias()) {
            return;
        }
        Map<Integer, EnumConstantNode> fieldByTag = new HashMap<>();
        for (EnumConstantNode constant : constants) {
            int tag = constant.getConstantValue();
            if (fieldByTag.containsKey(tag)) {
                String message = message("error.duplicate.constant.value", tag);
                markError(constant.getNode(), fieldByTag.get(tag).getConstantValueNode(), message);
                markError(constant.getNode(), constant.getConstantValueNode(), message);
            }
            fieldByTag.put(tag, constant);
        }
    }

    private void checkDuplicateEnumConstantNames(List<EnumConstantNode> constants) {
        Map<String, EnumConstantNode> fieldByName = new HashMap<>();
        for (EnumConstantNode constant : constants) {
            String name = constant.getConstantName();
            if (fieldByName.containsKey(name)) {
                String message = message("error.duplicate.constant.name", name);
                markError(constant.getNode(), fieldByName.get(name).getConstantNameNode(), message);
                markError(constant.getNode(), constant.getConstantNameNode(), message);
            }
            fieldByName.put(name, constant);
        }
    }

    private boolean hasErrors(PsiElement element) {
        if (element instanceof AntlrParserRuleNode) {
            AntlrParserRuleNode node = (AntlrParserRuleNode) element;
            return node.hasSyntaxErrors();
        } else {
            return false;
        }
    }

    private void checkReservedFieldTags(MessageNode message, Collection<MessageField> fields) {
        List<RangeNode> ranges = message.getReservedFieldRanges();
        for (MessageField field : fields) {
            int tag = field.getTag();
            for (RangeNode range : ranges) {
                if (range.contains(tag)) {
                    markError(field.getNode(), field.getTagNode(), message("error.reserved.tag.value", tag));
                }
            }
        }
    }

    private void checkReservedFieldNames(MessageNode message, Collection<MessageField> fields) {
        Set<String> names = message.getReservedFieldNames();
        for (MessageField field : fields) {
            String name = field.getFieldName();
            if (names.contains(name)) {
                markError(field.getNode(), field.getFieldNameNode(), message("error.reserved.field.name", name));
            }
        }
    }

    private void checkInvalidFieldTags(Collection<MessageField> fields) {
        for (MessageField field : fields) {
            int tag = field.getTag();
            if (!isValidTagValue(tag)) {
                markError(field.getNode(), field.getTagNode(), message("error.invalid.tag.not.in.range",
                        tag, MIN_TAG, SYS_RESERVED_START, SYS_RESERVED_END, MAX_TAG));
            }
        }
    }

    private void markError(ASTNode parent, @Nullable ASTNode node, String message) {
        Annotation annotation;
        if (node == null) {
            annotation = holder.createErrorAnnotation(parent, message);
        } else {
            annotation = holder.createErrorAnnotation(node, message);
        }
        annotation.setHighlightType(ProblemHighlightType.GENERIC_ERROR);
    }

    @VisibleForTesting
    boolean isValidTagValue(int tag) {
        return tag >= MIN_TAG && tag <= MAX_TAG
                && !(tag >= SYS_RESERVED_START && tag <= SYS_RESERVED_END);
    }

    private void checkDuplicateFieldTags(Collection<MessageField> fields) {
        Map<Integer, MessageField> fieldByTag = new HashMap<>();
        for (MessageField field : fields) {
            int tag = field.getTag();
            if (fieldByTag.containsKey(tag)) {
                String message = message("error.duplicate.field.tag", tag);
                markError(field.getNode(), fieldByTag.get(tag).getTagNode(), message);
                markError(field.getNode(), field.getTagNode(), message);
            }
            fieldByTag.put(tag, field);
        }
    }

    private void checkDuplicateFieldNames(Collection<MessageField> fields) {
        Map<String, MessageField> fieldByName = new HashMap<>();
        for (MessageField field : fields) {
            String name = field.getFieldName();
            if (fieldByName.containsKey(name)) {
                String message = message("error.duplicate.field.name", name);
                markError(field.getNode(), fieldByName.get(name).getFieldNameNode(), message);
                markError(field.getNode(), field.getFieldNameNode(), message);
            }
            fieldByName.put(name, field);
        }
    }
}
