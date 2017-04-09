package io.protostuff.jetbrains.plugin;

import com.intellij.lang.Commenter;

/**
 * Commenter.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public class ProtoCommenter implements Commenter {

    public static final String LINE_COMMENT_PREFIX = "//";
    public static final String BLOCK_COMMENT_PREFIX = "/*";
    public static final String BLOCK_COMMENT_SUFFIX = "*/";

    public String getLineCommentPrefix() {
        return LINE_COMMENT_PREFIX;
    }

    public String getBlockCommentPrefix() {
        return BLOCK_COMMENT_PREFIX;
    }

    public String getBlockCommentSuffix() {
        return BLOCK_COMMENT_SUFFIX;
    }

    public String getCommentedBlockCommentPrefix() {
        return null;
    }

    public String getCommentedBlockCommentSuffix() {
        return null;
    }
}
