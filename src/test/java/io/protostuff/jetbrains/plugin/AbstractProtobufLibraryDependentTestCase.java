package io.protostuff.jetbrains.plugin;

import com.intellij.testFramework.LightProjectDescriptor;
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase;

/**
 * Parent for tests that depend on global protobuf library installed for a module.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public abstract class AbstractProtobufLibraryDependentTestCase extends LightPlatformCodeInsightFixtureTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        ProtostuffPluginController controller = myFixture.getProject().getComponent(ProtostuffPluginController.class);
        controller.addLibrary(myModule);
    }

    /**
     * Dumb fix for test failure:
     * ./gradlew test --debug --tests *Test.testUnresolvedOptionReference_StandardOption
     * --tests FindUsagesTest.testStandardOptionReferenceUsages
     * When FindUsagesTest does not run first - it fails.
     */
    @Override
    protected LightProjectDescriptor getProjectDescriptor() {
        return new LightProjectDescriptor() {

        };
    }
}
