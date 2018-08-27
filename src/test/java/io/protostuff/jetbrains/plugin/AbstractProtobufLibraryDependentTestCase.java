package io.protostuff.jetbrains.plugin;

import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;

/**
 * Parent for tests that depend on global protobuf library installed for a module.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public abstract class AbstractProtobufLibraryDependentTestCase extends LightCodeInsightFixtureTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        ProtostuffPluginController controller = myFixture.getProject().getComponent(ProtostuffPluginController.class);
        controller.addLibrary(myModule);
    }
}
