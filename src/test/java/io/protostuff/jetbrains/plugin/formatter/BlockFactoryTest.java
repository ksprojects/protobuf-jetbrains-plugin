package io.protostuff.jetbrains.plugin.formatter;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.intellij.psi.tree.IElementType;
import io.protostuff.compiler.parser.ProtoParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public class BlockFactoryTest {

    @Test
    public void allParserRulesAreRegistered() throws Exception {
        Map<IElementType, BlockFactory.Factory> registry = BlockFactory.registry;

        Set<String> allRules = ImmutableSet.copyOf(ProtoParser.ruleNames);

        Set<String> registeredRules = new HashSet<String>();
        for (IElementType type : registry.keySet()) {
            registeredRules.add(type.toString());
        }

        Sets.SetView<String> diff = Sets.difference(allRules, registeredRules);
        if (!diff.isEmpty()) {
            Assert.fail("Following rules are not registered: " + diff);
        }

    }
}