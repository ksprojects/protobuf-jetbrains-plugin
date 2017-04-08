package io.protostuff.jetbrains.plugin.resources;

import com.intellij.lang.Language;
import com.intellij.psi.PsiFile;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public interface BundledFileProvider {

    // File descriptor.proto from io.protostuff.compiler:protostuff-parser.
    // Originally copied from
    // https://github.com/google/protobuf/blob/master/src/google/protobuf/descriptor.proto
    String DESCRIPTOR_PROTO_RESOURCE = "google/protobuf/__descriptor.proto";
    String DESCRIPTOR_PROTO_NAME = "google/protobuf/descriptor.proto";

    /**
     * Returns a read-only {@code VirtualFile} for a given resource, bundled with plugin.
     */
    Optional<PsiFile> findFile(String resource, Language language, String displayName);

    /**
     * Returns a read-only {@code VirtualFile} for a given resource, bundled with plugin.
     * Throws an exception if file cannot be found.
     */
    @NotNull
    PsiFile getFile(String resource, Language language, String displayName);
}
