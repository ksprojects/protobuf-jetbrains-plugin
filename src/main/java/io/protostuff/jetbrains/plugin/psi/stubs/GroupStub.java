package io.protostuff.jetbrains.plugin.psi.stubs;

import static io.protostuff.compiler.parser.ProtoParser.RULE_groupBlock;
import static io.protostuff.compiler.parser.ProtoParser.RULE_messageBlock;

import com.intellij.psi.stubs.IndexSink;
import com.intellij.psi.stubs.StubElement;
import io.protostuff.jetbrains.plugin.psi.GroupNode;
import io.protostuff.jetbrains.plugin.psi.MessageNode;
import io.protostuff.jetbrains.plugin.psi.indices.DataTypeFullNameIndex;
import io.protostuff.jetbrains.plugin.psi.indices.DataTypeNameIndex;
import org.jetbrains.annotations.NotNull;

/**
 * Stub for [GroupNode].
 */
public class GroupStub extends DataTypeStub<GroupNode> {

    public static final Type TYPE = new Type();

    GroupStub(StubElement parent, String fullName, String name) {
        super(parent, TYPE, fullName, name);
    }

    private static class Type extends DataTypeStub.Type<GroupStub, GroupNode> {

        Type() {
            super(RULE_groupBlock, "groupBlock");
        }

        @NotNull
        @Override
        public String getExternalId() {
            return "groupBlock";
        }

        @Override
        public void indexStub(@NotNull GroupStub stub, @NotNull IndexSink sink) {
            sink.occurrence(DataTypeFullNameIndex.KEY, stub.getFullName());
            sink.occurrence(DataTypeNameIndex.KEY, stub.getName());
        }

        @Override
        public GroupNode createPsi(@NotNull GroupStub stub) {
            return new GroupNode(stub, this);
        }

        @NotNull
        @Override
        protected GroupStub createStub(StubElement parent, String fullName, String name) {
            return new GroupStub(parent, fullName, name);
        }
    }
}
