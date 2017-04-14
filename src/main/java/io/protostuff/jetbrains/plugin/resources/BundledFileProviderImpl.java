package io.protostuff.jetbrains.plugin.resources;

import com.google.common.collect.Maps;
import com.intellij.lang.Language;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.StreamUtil;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import io.protostuff.jetbrains.plugin.ProtoLanguage;
import io.protostuff.jetbrains.plugin.reference.OptionReference;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Loader for files bundled together with plugin.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public class BundledFileProviderImpl implements BundledFileProvider, ProjectComponent {

    private static final Logger LOGGER = Logger.getInstance(BundledFileProviderImpl.class);

    private final Project project;

    private ConcurrentMap<ResourceId, Optional<PsiFile>> cache = Maps.newConcurrentMap();

    public BundledFileProviderImpl(Project project) {
        this.project = project;
    }

    @Override
    public Optional<PsiFile> findFile(String resource, Language language, String displayName) {
        return cache.computeIfAbsent(new ResourceId(resource, language, displayName),
                this::getResourceImpl);
    }

    @NotNull
    @Override
    public PsiFile getFile(String resource, Language language, String displayName) {
        Optional<PsiFile> descriptor = findFile(BundledFileProvider.DESCRIPTOR_PROTO_RESOURCE, ProtoLanguage.INSTANCE, BundledFileProvider.DESCRIPTOR_PROTO_NAME);
        if (!descriptor.isPresent()) {
            throw new IllegalStateException("Could not load bundled resource: " + BundledFileProvider.DESCRIPTOR_PROTO_RESOURCE);
        }
        return descriptor.get();
    }

    private Optional<PsiFile> getResourceImpl(ResourceId resourceId) {
        String resource = resourceId.resource;
        String displayName = resourceId.displayName;
        String content = readClasspathResource(resource);
        if (content == null) {
            return Optional.empty();
        }
        PsiFile vf = createVirtualFile(displayName, content);
        return Optional.of(vf);
    }

    @NotNull
    private PsiFile createVirtualFile(String resource, String content) {
        PsiFileFactory fileFactory = PsiFileFactory.getInstance(project);
        PsiFile psiFile = fileFactory.createFileFromText(resource, ProtoLanguage.INSTANCE, content);
        try {
            psiFile.getVirtualFile().setWritable(false);
        } catch (IOException e) {
            throw new RuntimeException("Could not mark " + resource + " as read-only.");
        }
        return psiFile;
    }

    @Nullable
    private String readClasspathResource(String name) {
        try {
            String classpath = System.getProperty("java.class.path");
            LOGGER.debug("Reading " + name + " from classpath=" + classpath);
            ClassLoader classLoader = OptionReference.class.getClassLoader();
            if (classLoader == null) {
                throw new IllegalStateException("Can not obtain classloader instance");
            }
            InputStream resource = classLoader.getResourceAsStream(name);
            if (resource == null) {
                LOGGER.debug("Could not find " + name);
                return null;
            }
            return StreamUtil.readText(resource, StandardCharsets.UTF_8);
        } catch (Exception e) {
            LOGGER.error("Could not read " + name, e);
        }
        return null;
    }

    @Override
    public void projectOpened() {

    }

    @Override
    public void projectClosed() {

    }

    @Override
    public void initComponent() {

    }

    @Override
    public void disposeComponent() {

    }

    @NotNull
    @Override
    public String getComponentName() {
        return getClass().getSimpleName();
    }

    static class ResourceId {
        final String resource;
        final Language language;
        final String displayName;

        public ResourceId(String resource, Language language, String displayName) {
            this.resource = resource;
            this.language = language;
            this.displayName = displayName;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            ResourceId that = (ResourceId) o;
            return Objects.equals(resource, that.resource)
                    && Objects.equals(language, that.language)
                    && Objects.equals(displayName, that.displayName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(resource, language, displayName);
        }
    }

}
