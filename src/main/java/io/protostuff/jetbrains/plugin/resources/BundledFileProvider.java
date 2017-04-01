package io.protostuff.jetbrains.plugin.resources;

import com.intellij.lang.Language;
import com.intellij.psi.PsiFile;

import java.util.Optional;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public interface BundledFileProvider {

    /**
     * Returns a read-only {@code VirtualFile} for a given resource, bundled with plugin.
     */
    Optional<PsiFile> getFile(String resource, Language language, String displayName);
}
