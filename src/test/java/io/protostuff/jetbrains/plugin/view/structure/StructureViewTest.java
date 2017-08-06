package io.protostuff.jetbrains.plugin.view.structure;

import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import io.protostuff.jetbrains.plugin.psi.EnumConstantNode;
import io.protostuff.jetbrains.plugin.psi.EnumNode;
import io.protostuff.jetbrains.plugin.psi.FieldNode;
import io.protostuff.jetbrains.plugin.psi.MessageNode;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;

/**
 * Tests for structure view.
 *
 * @author Kostiantyn Shchepanovskyi
 */
@SuppressWarnings("unchecked")
public class StructureViewTest extends LightCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "src/test/resources";
    }

    public void testStructureView() throws Exception {
        myFixture.configureByFile("structure/StructureViewTest.proto");
        myFixture.testStructureView(structureViewComponent -> {
            structureViewComponent.setActionActive("SHOW_FIELDS", true);
            AbstractTreeNode rootElement = (AbstractTreeNode) structureViewComponent.getTreeStructure().getRootElement();
            List<AbstractTreeNode<?>> children = new ArrayList<>(rootElement.getChildren());

            AbstractTreeNode<MessageNode> msg = (AbstractTreeNode<MessageNode>) children.get(0);
            ItemPresentation msgPresentation = msg.getPresentation();
            Assert.assertEquals("Message", msgPresentation.getPresentableText());

            List<AbstractTreeNode> msgChildren = new ArrayList<>(msg.getChildren());
            Assert.assertEquals(1, msgChildren.size());

            AbstractTreeNode<FieldNode> field = (AbstractTreeNode<FieldNode>) msgChildren.get(0);
            PresentationData fieldPresentation = field.getPresentation();
            Assert.assertEquals("field", fieldPresentation.getPresentableText());

            AbstractTreeNode<EnumNode> anEnum = (AbstractTreeNode<EnumNode>) children.get(1);
            ItemPresentation enumPresentation = anEnum.getPresentation();
            Assert.assertEquals("Enum", enumPresentation.getPresentableText());

            List<AbstractTreeNode> enumChildren = new ArrayList<>(anEnum.getChildren());
            Assert.assertEquals(1, enumChildren.size());

            AbstractTreeNode<EnumConstantNode> enumConst = (AbstractTreeNode<EnumConstantNode>) enumChildren.get(0);
            PresentationData constPresentation = enumConst.getPresentation();
            Assert.assertEquals("VAL0", constPresentation.getPresentableText());

        });
    }

}
