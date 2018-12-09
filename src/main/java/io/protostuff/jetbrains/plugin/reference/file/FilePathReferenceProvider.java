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
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFileSystemItem;
import com.intellij.psi.PsiManager;
import com.intellij.util.containers.ContainerUtil;
import io.protostuff.jetbrains.plugin.psi.ProtoPsiFileRoot;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * File path references provider.
 *
 * @author Kostiantyn Shchepanovskyi
 */
@SuppressWarnings("checkstyle:JavadocMethod")
public class FilePathReferenceProvider {

    private final boolean myEndingSlashNotAllowed;
    private List<SourceRootsProvider> sourceRootsProviders = new ArrayList<>();

    public FilePathReferenceProvider() {
        this(true);
    }

    public FilePathReferenceProvider(boolean endingSlashNotAllowed) {
        myEndingSlashNotAllowed = endingSlashNotAllowed;
        sourceRootsProviders.add(new AllSourceRootsProvider());
        sourceRootsProviders.add(new WebCoreResourcePathRootsProvider());
        sourceRootsProviders.add(new CustomIncludePathRootsProvider());
        sourceRootsProviders.add(new LibrariesAndSdkClassesRootsProvider());
        sourceRootsProviders.add(new ProtoFileRelativePathRootsProvider());
    }

    @NotNull
    public Collection<PsiFileSystemItem> getRoots(@Nullable final Module module, ProtoPsiFileRoot psiFileRoot) {
        if (module == null) {
            return Collections.emptyList();
        }

        Set<PsiFileSystemItem> result = ContainerUtil.newLinkedHashSet();
        PsiManager psiManager = PsiManager.getInstance(module.getProject());

        for (SourceRootsProvider sourceRootsProvider : sourceRootsProviders) {
            VirtualFile[] sourceRoots = sourceRootsProvider.getSourceRoots(module, psiFileRoot);
            for (VirtualFile root : sourceRoots) {
                if (root != null) {
                    final PsiDirectory directory = psiManager.findDirectory(root);
                    if (directory != null) {
                        result.add(directory);
                    }
                }
            }
        }
        return result;
    }

    interface SourceRootsProvider {
        VirtualFile[] getSourceRoots(Module module, ProtoPsiFileRoot psiFileRoot);
    }

}
