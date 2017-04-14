package io.protostuff.jetbrains.plugin.psi;

import java.util.Optional;

/**
 * Proto syntax level.
 *
 * @author Kostiantyn Shchepanovskyi
 */
public enum Syntax {
    PROTO2("proto2"),
    PROTO3("proto3");

    public static Syntax DEFAULT = PROTO2;

    private final String name;

    Syntax(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * Get syntax by proto syntax name.
     */
    public static Optional<Syntax> forName(String name) {
        for (Syntax syntax : Syntax.values()) {
            if (syntax.getName().equals(name)) {
                return Optional.of(syntax);
            }
        }
        return Optional.empty();
    }
}
