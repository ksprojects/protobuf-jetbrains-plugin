package io.protostuff.jetbrains.plugin.psi;

import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import io.protostuff.compiler.parser.ProtoParser;
import io.protostuff.jetbrains.plugin.Icons;
import io.protostuff.jetbrains.plugin.ProtoParserDefinition;
import io.protostuff.jetbrains.plugin.view.structure.ProtoItemPresentation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public class MessageNode extends UserType implements AntlrParserRuleNode, UserTypeContainer {


    public static final int RULE_INDEX = ProtoParser.RULE_messageBlock;

    @Nullable
    private Boolean syntaxErrors;

    public MessageNode(@NotNull ASTNode node) {
        super(node, ProtoParserDefinition.rule(ProtoParser.RULE_messageName));
    }

    /**
     * Returns message name.
     */
    @Override
    public String getName() {
        return super.getName();
    }


    @Override
    public String getNamespace() {
        return getQualifiedName() + ".";
    }

    @Override
    public Collection<UserType> getChildrenTypes() {
        return Arrays.asList(findChildrenByClass(UserType.class));
    }

    public Collection<MessageField> getFields() {
        List<MessageField> result = new ArrayList<>();
        // normal fields and maps
        MessageField[] fields = findChildrenByClass(MessageField.class);
        result.addAll(Arrays.asList(fields));
        OneOfNode[] oneOfs = findChildrenByClass(OneOfNode.class);
        for (OneOfNode oneOf : oneOfs) {
            Collection<MessageField> oneOfFields = oneOf.getFields();
            result.addAll(oneOfFields);
        }
        return result;
    }

    @Override
    public ItemPresentation getPresentation() {
        String fullName = getFullName();
        return new ProtoItemPresentation(fullName, Icons.MESSAGE);
    }

    @NotNull
    public List<RangeNode> getReservedFieldRanges() {
        return Stream.of(findChildrenByClass(ReservedFieldRangesNode.class))
                .flatMap(rangesNode -> Arrays.stream(rangesNode.getRanges()))
                .collect(Collectors.toList());
    }

    @NotNull
    public Set<String> getReservedFieldNames() {
        return Stream.of(findChildrenByClass(ReservedFieldNamesNode.class))
                .flatMap(namesNode -> namesNode.getNames().stream())
                .collect(Collectors.toSet());
    }

    @Override
    public int getRuleIndex() {
        return RULE_INDEX;
    }

    @Override
    public boolean hasSyntaxErrors() {
        if (syntaxErrors == null) {
            syntaxErrors = Util.checkForSyntaxErrors(this);
        }
        return syntaxErrors;
    }
}