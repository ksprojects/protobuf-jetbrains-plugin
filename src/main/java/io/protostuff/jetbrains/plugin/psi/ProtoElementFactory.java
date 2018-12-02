package io.protostuff.jetbrains.plugin.psi;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import io.protostuff.jetbrains.plugin.ProtoLanguage;
import java.util.Collection;

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
        PsiFile file = factory.createFileFromText("tmp.proto", ProtoLanguage.INSTANCE, text);
        Preconditions.checkState(file instanceof ProtoPsiFileRoot, "Expected result of type %s but got %s",
                ProtoPsiFileRoot.class, file.getClass());
        ProtoPsiFileRoot protoFile = (ProtoPsiFileRoot) file;
        ProtoRootNode protoRoot = protoFile.getProtoRoot();
        Collection<DataType> types = protoRoot.getDeclaredDataTypes();
        DataType dataType = Iterables.getFirst(types, null);
        Preconditions.checkNotNull(dataType, "Could not find message in %s", protoFile.getText());
        return dataType.getNameIdentifier();
    }
}
