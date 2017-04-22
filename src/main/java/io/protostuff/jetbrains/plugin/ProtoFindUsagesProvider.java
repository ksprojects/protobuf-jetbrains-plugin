package io.protostuff.jetbrains.plugin;

import com.intellij.lang.cacheBuilder.DefaultWordsScanner;
import com.intellij.lang.cacheBuilder.WordsScanner;
import com.intellij.lang.findUsages.FindUsagesProvider;
import com.intellij.psi.PsiElement;
import io.protostuff.jetbrains.plugin.psi.DataType;
import io.protostuff.jetbrains.plugin.psi.DataTypeContainer;
import io.protostuff.jetbrains.plugin.psi.EnumNode;
import io.protostuff.jetbrains.plugin.psi.MessageField;
import io.protostuff.jetbrains.plugin.psi.MessageNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Find usages provider.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public class ProtoFindUsagesProvider implements FindUsagesProvider {

    @Nullable
    @Override
    public WordsScanner getWordsScanner() {

        return new DefaultWordsScanner(new ProtoLexerAdapter(),
                ProtoParserDefinition.IDENTIFIER_TOKEN_SET,
                ProtoParserDefinition.COMMENT_TOKEN_SET,
                ProtoParserDefinition.LITERAL_TOKEN_SET);
    }

    @Override
    public boolean canFindUsagesFor(@NotNull PsiElement psiElement) {
        return psiElement instanceof DataType
                || psiElement instanceof MessageField;
    }

    @Nullable
    @Override
    public String getHelpId(@NotNull PsiElement psiElement) {
        return null;
    }

    @NotNull
    @Override
    public String getType(@NotNull PsiElement element) {
        if (element instanceof MessageNode) {
            return "message";
        }
        if (element instanceof EnumNode) {
            return "enum";
        }
        if (element instanceof MessageField) {
            return "field";
        }
        return "";
    }

    @NotNull
    @Override
    public String getDescriptiveName(@NotNull PsiElement element) {
        if (element instanceof DataType) {
            DataType type = (DataType) element;
            return type.getFullName();
        }
        if (element instanceof MessageField) {
            MessageField field = (MessageField) element;
            return field.getFieldName();
        }
        return "";
    }

    @NotNull
    @Override
    public String getNodeText(@NotNull PsiElement element, boolean useFullName) {
        if (element instanceof DataType) {
            DataType type = (DataType) element;
            if (useFullName) {
                return type.getFullName();
            }
            //noinspection ConstantConditions
            return type.getName();
        }
        if (element instanceof MessageField) {
            MessageField field = (MessageField) element;
            if (useFullName) {
                String namespace = getNamespace(field);
                return namespace + field.getFieldName();
            }
            return field.getFieldName();
        }
        return "";
    }

    private String getNamespace(MessageField field) {
        PsiElement tmp = field;
        while (tmp != null) {
            tmp = tmp.getParent();
            if (tmp instanceof DataTypeContainer) {
                DataTypeContainer container = (DataTypeContainer) tmp;
                return container.getNamespace();
            }
        }
        return "";
    }


}
