package io.protostuff.jetbrains.plugin.psi.stubs;

import static io.protostuff.compiler.parser.ProtoParser.RULE_messageBlock;

import com.intellij.psi.stubs.IndexSink;
import com.intellij.psi.stubs.StubElement;
import io.protostuff.jetbrains.plugin.psi.MessageNode;
import io.protostuff.jetbrains.plugin.psi.indices.DataTypeFullNameIndex;
import io.protostuff.jetbrains.plugin.psi.indices.DataTypeNameIndex;
import org.jetbrains.annotations.NotNull;

/**
 * Stub for [MessageNode].
 */
public class MessageStub extends DataTypeStub<MessageNode> {

    public static final Type TYPE = new Type();

    MessageStub(StubElement parent, String fullName, String name) {
        super(parent, TYPE, fullName, name);
    }

    private static class Type extends DataTypeStub.Type<MessageNode> {

        Type() {
            super(RULE_messageBlock, "messageBlock");
        }

        @NotNull
        @Override
        public String getExternalId() {
            return "messageBlock";
        }

        @Override
        public void indexStub(@NotNull DataTypeStub<MessageNode> stub, @NotNull IndexSink sink) {
            sink.occurrence(DataTypeFullNameIndex.KEY, stub.getFullName());
            sink.occurrence(DataTypeNameIndex.KEY, stub.getName());
        }

        @Override
        public MessageNode createPsi(@NotNull DataTypeStub<MessageNode> stub) {
            return new MessageNode((MessageStub) stub, this);
        }

        @NotNull
        @Override
        protected MessageStub createStub(StubElement parent, String fullName, String name) {
            return new MessageStub(parent, fullName, name);
        }
    }
}
