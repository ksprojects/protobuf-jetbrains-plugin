package io.protostuff.jetbrains.plugin.psi.stubs;

import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.NamedStubBase;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import io.protostuff.jetbrains.plugin.ProtoLanguage;
import io.protostuff.jetbrains.plugin.psi.DataType;
import java.io.IOException;
import org.antlr.jetbrains.adapter.lexer.RuleIStubElementTypeImpl;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * Base class for [DataType] stubs.
 */
public abstract class DataTypeStub<T extends DataType> extends NamedStubBase<T> {

    private final String fullName;

    DataTypeStub(StubElement parent, IStubElementType<?, T> type, String fullName, String name) {
        super(parent, type, name);
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }

    public abstract static class Type<S extends DataTypeStub<T>, T extends DataType> extends
            RuleIStubElementTypeImpl<S, T> {

        Type(int ruleIndex, @NotNull @NonNls final String debugName) {
            super(ruleIndex, debugName, ProtoLanguage.INSTANCE);
        }

        @NotNull
        protected abstract S createStub(StubElement parent, String fullName, String name);

        @NotNull
        @Override
        public S createStub(@NotNull T psi, StubElement parentStub) {
            return createStub(parentStub, psi.getFullName(), psi.getName());
        }

        @Override
        public void serialize(@NotNull S stub, @NotNull StubOutputStream dataStream)
                throws IOException {
            dataStream.writeName(stub.getFullName());
            dataStream.writeName(stub.getName());
        }

        @NotNull
        @Override
        public S deserialize(@NotNull StubInputStream dataStream, StubElement parentStub)
                throws IOException {
            return createStub(parentStub, dataStream.readName().getString(),
                    dataStream.readName().getString());
        }
    }
}
