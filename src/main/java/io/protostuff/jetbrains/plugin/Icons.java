package io.protostuff.jetbrains.plugin;

import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

/**
 * All icons used in the plugin.
 *
 * http://www.jetbrains.org/intellij/sdk/docs/reference_guide/work_with_icons_and_images.html
 */
public class Icons {

    private static final String BASEDIR = "/io/protostuff/protostuff-jetbrains-plugin/icons/";

    public static final Icon PROTO = IconLoader.getIcon(BASEDIR + "proto.png");
    public static final Icon MESSAGE = IconLoader.getIcon(BASEDIR + "message.png");
    public static final Icon ENUM = IconLoader.getIcon(BASEDIR + "enum.png");
    public static final Icon SERVICE = IconLoader.getIcon(BASEDIR + "service.png");
    public static final Icon FIELD = IconLoader.getIcon(BASEDIR + "field.png");
    public static final Icon CONSTANT = IconLoader.getIcon(BASEDIR + "constant.png");
    public static final Icon RPC = IconLoader.getIcon(BASEDIR + "rpc.png");

    private Icons(){}

}
