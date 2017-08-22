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

    DataTypeStub(StubElement parent, IStubElementType<DataTypeStub<T>, T> type,
            String fullName, String name) {
        super(parent, type, name);
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }

    public static abstract class Type<T extends DataType> extends
            RuleIStubElementTypeImpl<DataTypeStub<T>, T> {

        Type(int ruleIndex, @NotNull @NonNls final String debugName) {
            super(ruleIndex, debugName, ProtoLanguage.INSTANCE);
        }

        @NotNull
        protected abstract DataTypeStub<T> createStub(StubElement parent, String fullName,
                String name);

        @NotNull
        @Override
        public DataTypeStub<T> createStub(@NotNull T psi, StubElement parentStub) {
            return createStub(parentStub, psi.getFullName(), psi.getName());
        }

        @Override
        public void serialize(@NotNull DataTypeStub<T> stub, @NotNull StubOutputStream dataStream)
                throws IOException {
            dataStream.writeName(stub.getFullName());
            dataStream.writeName(stub.getName());
        }

        @NotNull
        @Override
        public DataTypeStub<T> deserialize(
                @NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
            return createStub(parentStub, dataStream.readName().getString(),
                    dataStream.readName().getString());
        }
    }
}
