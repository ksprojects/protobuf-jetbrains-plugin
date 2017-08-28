package io.protostuff.jetbrains.plugin.indices;

import com.google.common.collect.ImmutableList;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import io.protostuff.jetbrains.plugin.psi.indices.DataTypeFullNameIndex;
import io.protostuff.jetbrains.plugin.psi.indices.DataTypeNameIndex;
import java.util.Collection;

public class DataTypeIndicesTest extends LightCodeInsightFixtureTestCase {

    public void testIndices() throws Exception {
        myFixture.configureByText("test.proto", String.join("\n",
                "package navigation;",
                "message MyProtobufMessage {",
                "enum MyProtobufNestedEnum {}",
                "}"));
        Collection<String> shortNames = DataTypeNameIndex.INSTANCE.getAllKeys(getProject());
        assertContainsElements(shortNames,
                ImmutableList.of("MyProtobufMessage", "MyProtobufNestedEnum"));

        Collection<String> fullNames = DataTypeFullNameIndex.INSTANCE.getAllKeys(getProject());
        assertContainsElements(fullNames, ImmutableList.of("navigation.MyProtobufMessage",
                "navigation.MyProtobufMessage.MyProtobufNestedEnum"));
    }
}
