package io.protostuff.jetbrains.plugin.psi;

import static io.protostuff.jetbrains.plugin.ProtoParserDefinition.R_FIELD_MODIFIER;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.tree.IElementType;
import io.protostuff.jetbrains.plugin.psi.stubs.DataTypeStub;
import io.protostuff.jetbrains.plugin.psi.stubs.GroupStub;
import io.protostuff.jetbrains.plugin.psi.stubs.MessageStub;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * Group node.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public class GroupNode extends MessageNode implements KeywordsContainer {

  public GroupNode(@NotNull ASTNode node) {
    super(node);
  }

  public GroupNode(DataTypeStub stub, IStubElementType type) {
    super(stub, type);
  }

  public GroupNode(DataTypeStub stub, IElementType type, ASTNode node) {
    super(stub, type, node);
  }

  @Override
  public Collection<PsiElement> keywords() {
    ASTNode node = getNode();
    List<PsiElement> result = new ArrayList<>();
    result.addAll(Util.findKeywords(getNode()));
    ASTNode fieldModifier = node.findChildByType(R_FIELD_MODIFIER);
    if (fieldModifier != null) {
      result.addAll(Util.findKeywords(fieldModifier));
    }
    return result;
  }
}
