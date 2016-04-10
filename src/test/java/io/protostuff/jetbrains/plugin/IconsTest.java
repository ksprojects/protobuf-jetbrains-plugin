package io.protostuff.jetbrains.plugin;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public class IconsTest {

    @Test
    @Ignore
    public void iconExists() throws Exception {
        Assert.assertEquals(16, Icons.PROTO.getIconHeight());
    }
}