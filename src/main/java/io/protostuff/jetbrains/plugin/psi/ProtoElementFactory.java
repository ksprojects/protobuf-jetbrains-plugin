package io.protostuff.jetbrains.plugin.psi;

import com.google.common.base.Preconditions;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.util.PsiTreeUtil;
import io.protostuff.jetbrains.plugin.ProtoLanguage;
import org.jetbrains.annotations.NotNull;

/**
 * PSI element factory.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public class ProtoElementFactory {

    private final Project project;
    private final PsiFileFactory factory;

    public ProtoElementFactory(Project project) {
        this.project = project;
        factory = PsiFileFactory.getInstance(project);
    }

    /**
     * Create new name node instance.
     */
    public GenericNameNode createGenericNameNode(String name) {
        String text = String.format("message %s {}", name);
        return createProtoAndExtract(text, GenericNameNode.class);
    }

    /**
     * Create new field reference node instance.
     */
    public FieldReferenceNode createFieldReferenceNode(String reference) {
        String text = String.format("option %s = true;", reference);
        return createProtoAndExtract(text, FieldReferenceNode.class);
    }

    /**
     * Create new file reference node instance.
     */
    public FileReferenceNode createFileReferenceNode(String reference) {
        String text = String.format("import %s;", reference);
        return createProtoAndExtract(text, FileReferenceNode.class);
    }

    /**
     * Create new type reference node instance.
     */
    public TypeReferenceNode crateTypeReferenceNode(String reference) {
        String text = String.format("extend %s {}", reference);
        return createProtoAndExtract(text, TypeReferenceNode.class);
    }

    @NotNull
    private <T extends PsiElement> T createProtoAndExtract(String text, Class<T> type) {
        ProtoPsiFileRoot protoFile = createProtoFromText(text);
        T result = PsiTreeUtil.findChildOfType(protoFile, type);
        Preconditions.checkNotNull(result, "Could not find %s in %s", type, protoFile.getText());
        return result;
    }

    @NotNull
    private ProtoPsiFileRoot createProtoFromText(String text) {
        PsiFile file = factory.createFileFromText("tmp.proto", ProtoLanguage.INSTANCE, text);
        Preconditions.checkState(file instanceof ProtoPsiFileRoot, "Expected result of type %s but got %s",
                ProtoPsiFileRoot.class, file.getClass());
        return (ProtoPsiFileRoot) file;
    }
}
