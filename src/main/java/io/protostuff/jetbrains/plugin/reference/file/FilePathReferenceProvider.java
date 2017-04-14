/*
 * Copyright 2000-2014 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.protostuff.jetbrains.plugin.reference.file;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.util.Condition;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiCompiledElement;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFileSystemItem;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceProvider;
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReference;
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReferenceSet;
import com.intellij.util.ProcessingContext;
import com.intellij.util.containers.ContainerUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Copied from IntelliJ IDEA source code.
 *
 * @author cdr
 */
@SuppressWarnings("checkstyle:JavadocMethod")
public class FilePathReferenceProvider extends PsiReferenceProvider {

    private final boolean myEndingSlashNotAllowed;

    interface SourceRootsProvider {
        VirtualFile[] getSourceRoots(Module module);
    }

    private List<SourceRootsProvider> sourceRootsProviders = new ArrayList<>();

    public FilePathReferenceProvider() {
        this(true);
    }

    public FilePathReferenceProvider(boolean endingSlashNotAllowed) {
        myEndingSlashNotAllowed = endingSlashNotAllowed;
        sourceRootsProviders.add(new AllSourceRootsProvider());
        sourceRootsProviders.add(new CustomIncludePathRootsProvider());
        sourceRootsProviders.add(new LibrariesAndSdkClassesRootsProvider());
    }

    @NotNull
    public Collection<PsiFileSystemItem> getRoots(@Nullable final Module module) {
        if (module == null) {
            return Collections.emptyList();
        }

        Set<PsiFileSystemItem> result = ContainerUtil.newLinkedHashSet();
        PsiManager psiManager = PsiManager.getInstance(module.getProject());

        for (SourceRootsProvider sourceRootsProvider : sourceRootsProviders) {
            VirtualFile[] sourceRoots = sourceRootsProvider.getSourceRoots(module);
            for (VirtualFile root : sourceRoots) {
                final PsiDirectory directory = psiManager.findDirectory(root);
                if (directory != null) {
                    result.add(directory);
                }
            }
        }
        return result;
    }

    @NotNull
    public PsiReference[] getReferencesByElement(@NotNull PsiElement element, String text, int offset, final boolean soft) {
        return getReferencesByElement(element, text, offset, soft, Module.EMPTY_ARRAY);
    }

    @NotNull
    public PsiReference[] getReferencesByElement(@NotNull PsiElement element,
                                                 String text,
                                                 int offset,
                                                 final boolean soft,
                                                 @NotNull final Module... forModules) {
        return new FileReferenceSet(text, element, offset, this, true, myEndingSlashNotAllowed) {


            @Override
            protected boolean isSoft() {
                return soft;
            }

            @Override
            public boolean isAbsolutePathReference() {
                return true;
            }

            @Override
            public boolean couldBeConvertedTo(boolean relative) {
                return !relative;
            }

            @Override
            public boolean absoluteUrlNeedsStartSlash() {
                final String s = getPathString();
                return s != null && !s.isEmpty() && s.charAt(0) == '/';
            }

            @Override
            @NotNull
            public Collection<PsiFileSystemItem> computeDefaultContexts() {
                if (forModules.length > 0) {
                    Set<PsiFileSystemItem> rootsForModules = ContainerUtil.newLinkedHashSet();
                    for (Module forModule : forModules) {
                        rootsForModules.addAll(getRoots(forModule));
                    }
                    return rootsForModules;
                }

                return getRoots(ModuleUtilCore.findModuleForPsiElement(getElement()));
            }

            @Override
            public FileReference createFileReference(final TextRange range, final int index, final String text) {
                return FilePathReferenceProvider.this.createFileReference(this, range, index, text);
            }

            @Override
            protected Condition<PsiFileSystemItem> getReferenceCompletionFilter() {
                return new Condition<PsiFileSystemItem>() {
                    @Override
                    public boolean value(final PsiFileSystemItem element) {
                        return isPsiElementAccepted(element);
                    }
                };
            }
        }.getAllReferences();
    }

    @Override
    @NotNull
    public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull final ProcessingContext context) {
        String text = null;
        if (text == null) {
            return PsiReference.EMPTY_ARRAY;
        }
        return getReferencesByElement(element, text, 1, true);
    }

    @Override
    public boolean acceptsTarget(@NotNull PsiElement target) {
        return target instanceof PsiFileSystemItem;
    }

    protected boolean isPsiElementAccepted(PsiElement element) {
        return !(element instanceof PsiCompiledElement);
    }

    protected FileReference createFileReference(FileReferenceSet referenceSet, final TextRange range, final int index, final String text) {
        return new FileReference(referenceSet, range, index, text);
    }

}
