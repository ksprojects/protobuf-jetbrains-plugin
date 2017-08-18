package io.protostuff.jetbrains.plugin.psi.stubs;

import com.intellij.psi.stubs.IndexSink;
import com.intellij.psi.stubs.StubElement;
import io.protostuff.jetbrains.plugin.psi.EnumNode;
import io.protostuff.jetbrains.plugin.psi.indices.DataTypeIndex;
import org.jetbrains.annotations.NotNull;

public class EnumStub extends DataTypeStub<EnumNode> {

    public static final Type TYPE = new Type();

    EnumStub(StubElement parent, String fullName, String name) {
        super(parent, TYPE, fullName, name);
    }

    private static class Type extends DataTypeStub.Type<EnumNode> {

        Type() {
            super("ENUM");
        }

        @NotNull
        @Override
        public String getExternalId() {
            return "ENUM";
        }

        @Override
        public void indexStub(@NotNull DataTypeStub<EnumNode> stub, @NotNull IndexSink sink) {
            sink.occurrence(DataTypeIndex.KEY, stub.getFullName());
        }

        @Override
        public EnumNode createPsi(@NotNull DataTypeStub<EnumNode> stub) {
            assert stub instanceof EnumStub;
            return new EnumNode((EnumStub) stub, this);
        }

        @NotNull
        @Override
        protected EnumStub createStub(StubElement parent, String fullName, String name) {
            return new EnumStub(parent, fullName, name);
        }
    }
}
