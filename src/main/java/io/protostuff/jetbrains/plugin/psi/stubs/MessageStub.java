package io.protostuff.jetbrains.plugin.psi.stubs;

import static io.protostuff.compiler.parser.ProtoParser.RULE_messageBlock;

import com.intellij.psi.stubs.IndexSink;
import com.intellij.psi.stubs.StubElement;
import io.protostuff.jetbrains.plugin.psi.MessageNode;
import io.protostuff.jetbrains.plugin.psi.indices.DataTypeIndex;
import org.jetbrains.annotations.NotNull;

public class MessageStub extends DataTypeStub<MessageNode> {

    public static final Type TYPE = new Type();

    MessageStub(StubElement parent, String fullName, String name) {
        super(parent, TYPE, fullName, name);
    }

    private static class Type extends DataTypeStub.Type<MessageNode> {

        Type() {
            super(RULE_messageBlock, "MESSAGE");
        }

        @NotNull
        @Override
        public String getExternalId() {
            return "MESSAGE";
        }

        @Override
        public void indexStub(@NotNull DataTypeStub<MessageNode> stub, @NotNull IndexSink sink) {
            sink.occurrence(DataTypeIndex.KEY, stub.getFullName());
        }

        @Override
        public MessageNode createPsi(@NotNull DataTypeStub<MessageNode> stub) {
            assert stub instanceof MessageStub;
            return new MessageNode((MessageStub) stub, this);
        }

        @NotNull
        @Override
        protected MessageStub createStub(StubElement parent, String fullName, String name) {
            return new MessageStub(parent, fullName, name);
        }
    }
}
