package io.protostuff.jetbrains.plugin.view.structure;

import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import io.protostuff.jetbrains.plugin.psi.ProtoRootStatementNode;
import org.jetbrains.annotations.NotNull;

public class ProtoStructureViewRootElement extends ProtoStructureViewElement {
	public ProtoStructureViewRootElement(PsiFile element) {
		super(element);
	}

	@NotNull
	@Override
	public ItemPresentation getPresentation() {
		return new ProtoRootPresentation((PsiFile)element);
	}

}
