package io.protostuff.jetbrains.plugin.psi.stubs;

import com.intellij.lang.Language;
import com.intellij.psi.PsiFile;
import com.intellij.psi.StubBuilder;
import com.intellij.psi.stubs.DefaultStubBuilder;
import com.intellij.psi.stubs.PsiFileStubImpl;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.intellij.psi.tree.IStubFileElementType;
import io.protostuff.jetbrains.plugin.ProtoLanguage;
import io.protostuff.jetbrains.plugin.psi.ProtoPsiFileRoot;
import java.io.IOException;
import org.jetbrains.annotations.NotNull;

public class FileStub extends PsiFileStubImpl<ProtoPsiFileRoot> {

    public static final Type TYPE = new Type("PROTO_FILE", ProtoLanguage.INSTANCE);

    public FileStub(ProtoPsiFileRoot file) {
        super(file);
    }

    @Override
    public IStubFileElementType getType() {
        return TYPE;
    }

    static class Type extends IStubFileElementType<FileStub> {

        public static final int VERSION = 1;

        public Type(String debugName, Language language) {
            super(debugName, language);
        }

        @Override
        public StubBuilder getBuilder() {
            return new DefaultStubBuilder() {
                @Override
                protected StubElement createStubForFile(@NotNull PsiFile file) {
                    return new FileStub((ProtoPsiFileRoot) file);
                }
            };
        }

        @Override
        public int getStubVersion() {
            return VERSION;
        }

        @Override
        public void serialize(@NotNull FileStub stub, @NotNull StubOutputStream dataStream)
                throws IOException {
        }

        @NotNull
        @Override
        public FileStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub)
                throws IOException {
            return new FileStub(null);
        }

        @NotNull
        @Override
        public String getExternalId() {
            return "PROTO_FILE";
        }
    }
}