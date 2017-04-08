package io.protostuff.jetbrains.plugin.annotator;

import static io.protostuff.jetbrains.plugin.ProtostuffBundle.message;

import com.google.common.annotations.VisibleForTesting;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.lang.ASTNode;
import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiErrorElement;
import com.intellij.psi.PsiRecursiveElementVisitor;
import io.protostuff.compiler.model.Field;
import io.protostuff.jetbrains.plugin.psi.AntlrParserRuleNode;
import io.protostuff.jetbrains.plugin.psi.EnumConstantNode;
import io.protostuff.jetbrains.plugin.psi.EnumNode;
import io.protostuff.jetbrains.plugin.psi.MessageField;
import io.protostuff.jetbrains.plugin.psi.MessageNode;
import io.protostuff.jetbrains.plugin.psi.RangeNode;
import io.protostuff.jetbrains.plugin.psi.RpcMethodNode;
import io.protostuff.jetbrains.plugin.psi.ServiceNode;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public class ProtoErrorsAnnotator implements Annotator {

    private final int MIN_TAG = 1;
    private final int MAX_TAG = Field.MAX_TAG_VALUE;
    private final int SYS_RESERVED_START = 19000;
    private final int SYS_RESERVED_END = 19999;

    private AnnotationHolder holder;

    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        boolean check = hasErrors(element);
        if (!check) {
            this.holder = holder;
            if (element instanceof MessageNode) {
                MessageNode message = (MessageNode) element;
                Collection<MessageField> fields = message.getFields();
                checkInvalidFieldTags(fields);
                checkDuplicateFieldTags(fields);
                checkDuplicateFieldNames(fields);
                checkReservedFieldTags(message, fields);
                checkReservedFieldNames(message, fields);
            } else if (element instanceof EnumNode) {
                EnumNode anEnum = (EnumNode) element;
                List<EnumConstantNode> constants = anEnum.getConstants();
                checkDuplicateEnumConstantNames(constants);
                checkDuplicateEnumConstantValues(anEnum, constants);
            } else if (element instanceof ServiceNode) {
                ServiceNode service = (ServiceNode) element;
                List<RpcMethodNode> rpcMethods = service.getRpcMethods();
                checkDuplicateServiceMethodNames(rpcMethods);
            }
            this.holder = null;
        }
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
            final boolean[] hasErrors = {false};
            new PsiRecursiveElementVisitor() {
                @Override
                public void visitErrorElement(PsiErrorElement element) {
                    hasErrors[0] = true;
                }
            };
            return hasErrors[0];
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
