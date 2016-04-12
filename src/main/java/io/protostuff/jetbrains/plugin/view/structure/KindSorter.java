package io.protostuff.jetbrains.plugin.view.structure;

import com.intellij.ide.util.treeView.smartTree.ActionPresentation;
import com.intellij.ide.util.treeView.smartTree.Sorter;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

public class KindSorter implements Sorter {

    public static final Sorter INSTANCE = new KindSorter();
    @NonNls
    public static final String ID = "KIND";

    private final Comparator COMPARATOR = new Comparator() {
        @Override
        public int compare(final Object o1, final Object o2) {
            return getWeight(o1) - getWeight(o2);
        }

        private int getWeight(final Object value) {
            if (value instanceof ProtoServiceTreeElement) {
                return 0;
            }
            if (value instanceof ProtoMessageFieldTreeElement) {
                return 10;
            }
            if (value instanceof ProtoEnumConstantTreeElement) {
                return 10;
            }
            if (value instanceof ProtoServiceMethodTreeElement) {
                return 10;
            }
            if (value instanceof ProtoMessageTreeElement) {
                return 20;
            }
            if (value instanceof ProtoEnumTreeElement) {
                return 20;
            }
            return 0;
        }
    };

    @Override
    @NotNull
    public Comparator getComparator() {
        return COMPARATOR;
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    @NotNull
    public ActionPresentation getPresentation() {
        throw new IllegalStateException();
    }

    @Override
    @NotNull
    public String getName() {
        return ID;
    }
}
