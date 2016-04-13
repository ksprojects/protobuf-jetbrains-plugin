package io.protostuff.jetbrains.plugin.view.structure;

import com.intellij.ide.util.treeView.smartTree.ActionPresentation;
import com.intellij.ide.util.treeView.smartTree.Sorter;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

final class KindSorter implements Sorter {

    static final Sorter INSTANCE = new KindSorter();

    private static final String ID = "KIND";

    private final Comparator COMPARATOR = new Comparator() {
        @Override
        public int compare(final Object o1, final Object o2) {
            return getWeight(o1) - getWeight(o2);
        }

        private int getWeight(final Object value) {
            if (value instanceof ServiceTreeElement) {
                return 0;
            }
            if (value instanceof MessageFieldTreeElement) {
                return 10;
            }
            if (value instanceof EnumConstantTreeElement) {
                return 10;
            }
            if (value instanceof ServiceMethodTreeElement) {
                return 10;
            }
            if (value instanceof MessageTreeElement) {
                return 20;
            }
            if (value instanceof EnumTreeElement) {
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
