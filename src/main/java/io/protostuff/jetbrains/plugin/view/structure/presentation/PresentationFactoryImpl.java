package io.protostuff.jetbrains.plugin.view.structure.presentation;

import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import io.protostuff.jetbrains.plugin.psi.*;
import io.protostuff.jetbrains.plugin.view.structure.PresentationFactory;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public class PresentationFactoryImpl implements PresentationFactory {


    @Override
    public ItemPresentation createPresentation(PsiElement element) {
        if (element instanceof MessageNode) {
            return new MessagePresentation((MessageNode) element);
        }
        if (element instanceof EnumNode) {
            return new EnumPresentation((EnumNode) element);
        }
        if (element instanceof ServiceNode) {
            return new ServicePresentation((ServiceNode) element);
        }
        if (element instanceof EnumConstantNode) {
            return new EnumConstantPresentation((EnumConstantNode) element);
        }
        if (element instanceof FieldNode) {
            return new MessageFieldPresentation((FieldNode) element);
        }
        if (element instanceof RpcMethodNode) {
            return new RpcMethodPresentation((RpcMethodNode) element);
        }
        return new GenericItemPresentation(element);
    }

    @Override
    public boolean hasPresentation(PsiElement element) {
        return element instanceof MessageNode
                || element instanceof EnumNode
                || element instanceof ServiceNode
                || element instanceof EnumConstantNode
                || element instanceof FieldNode
                || element instanceof RpcMethodNode;
    }

}
