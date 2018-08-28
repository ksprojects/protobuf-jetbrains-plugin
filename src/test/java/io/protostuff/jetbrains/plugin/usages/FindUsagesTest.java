package io.protostuff.jetbrains.plugin.usages;

import com.intellij.psi.PsiElement;
import com.intellij.usageView.UsageInfo;
import io.protostuff.jetbrains.plugin.AbstractProtobufLibraryDependentTestCase;
import io.protostuff.jetbrains.plugin.psi.FieldNode;
import io.protostuff.jetbrains.plugin.psi.MessageNode;
import java.util.Collection;

/**
 * Tests for "find usages" feature.
 *
 * @author Kostiantyn Shchepanovskyi
 */
@SuppressWarnings("ConstantConditions")
public class FindUsagesTest extends AbstractProtobufLibraryDependentTestCase {

    @Override
    protected String getTestDataPath() {
        return "src/test/resources";
    }

    public void testFindMessageTypeUsages() {
        Collection<UsageInfo> usageInfos = myFixture.testFindUsages("usages/FieldTypeReference.proto");
        assertEquals(2, usageInfos.size());
        for (UsageInfo usageInfo : usageInfos) {
            PsiElement element = usageInfo.getReference().resolve();
            assertNotNull(element);
            MessageNode message = (MessageNode) element;
            assertEquals("usages.MessageA", message.getFullName());
        }
    }

    public void testCustomOptionReferenceUsages() {
        Collection<UsageInfo> usageInfos = myFixture.testFindUsages(
                "usages/CustomOptionReference.proto",
                "usages/Options.proto"
        );
        assertEquals(1, usageInfos.size());
        for (UsageInfo usageInfo : usageInfos) {
            PsiElement element = usageInfo.getReference().resolve();
            assertNotNull(element);
            FieldNode field = (FieldNode) element;
            assertEquals("stringOption", field.getFieldName());
        }
    }

    public void testStandardOptionReferenceUsages() {
        Collection<UsageInfo> usageInfos = myFixture.testFindUsages(
                "usages/StandardOptionReference.proto"
        );
        assertEquals(1, usageInfos.size());
        for (UsageInfo usageInfo : usageInfos) {
            PsiElement element = usageInfo.getReference().resolve();
            assertNotNull(element);
            FieldNode field = (FieldNode) element;
            assertEquals("jstype", field.getFieldName());
        }
    }

}
