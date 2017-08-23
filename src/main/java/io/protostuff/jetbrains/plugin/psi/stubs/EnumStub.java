package io.protostuff.jetbrains.plugin.psi.stubs;

import static io.protostuff.compiler.parser.ProtoParser.RULE_enumBlock;

import com.intellij.psi.stubs.IndexSink;
import com.intellij.psi.stubs.StubElement;
import io.protostuff.jetbrains.plugin.psi.EnumNode;
import io.protostuff.jetbrains.plugin.psi.indices.DataTypeFullNameIndex;
import io.protostuff.jetbrains.plugin.psi.indices.DataTypeNameIndex;
import org.jetbrains.annotations.NotNull;

/**
 * Stub for [EnumNode].
 */
public class EnumStub extends DataTypeStub<EnumNode> {

    public static final Type TYPE = new Type();

    EnumStub(StubElement parent, String fullName, String name) {
        super(parent, TYPE, fullName, name);
    }

    private static class Type extends DataTypeStub.Type<EnumStub, EnumNode> {

        Type() {
            super(RULE_enumBlock,"enumBlock");
        }

        @NotNull
        @Override
        public String getExternalId() {
            return "enumBlock";
        }

        @Override
        public void indexStub(@NotNull EnumStub stub, @NotNull IndexSink sink) {
            sink.occurrence(DataTypeFullNameIndex.KEY, stub.getFullName());
            sink.occurrence(DataTypeNameIndex.KEY, stub.getName());
        }

        @Override
        public EnumNode createPsi(@NotNull EnumStub stub) {
            return new EnumNode(stub, this);
        }

        @NotNull
        @Override
        protected EnumStub createStub(StubElement parent, String fullName, String name) {
            return new EnumStub(parent, fullName, name);
        }
    }
}
