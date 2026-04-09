package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;
import static seedu.address.logic.parser.CliSyntax.hasPrefix;

/**
 * Represents one alias used by a person in Harmony.
 * Guarantees: immutable; is valid as declared in {@link #isValidAlias(String)}
 */
public class Alias {

    public static final int MAX_LENGTH = 200;
    public static final String MESSAGE_CONSTRAINTS =
            "Aliases can take any values, but must not be blank and must be at most "
                    + MAX_LENGTH + " characters long"
                    + "Additionally, aliases cannot contain spaces followed by command prefixes (e.g., ' al/', ' g/').";

    public final String value;

    /**
     * Constructs an {@code Alias}.
     *
     * @param alias A valid alias.
     */
    public Alias(String alias) {
        requireNonNull(alias);
        String trimmedAlias = alias.trim();
        checkArgument(isValidAlias(trimmedAlias), MESSAGE_CONSTRAINTS);
        value = trimmedAlias;
    }

    /**
     * Returns true if a given string is a valid alias.
     */
    public static boolean isValidAlias(String test) {
        requireNonNull(test);
        String trimmedTest = test.trim();

        return !trimmedTest.isEmpty() && trimmedTest.length() <= MAX_LENGTH && !hasPrefix(test);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Alias)) {
            return false;
        }

        Alias otherAlias = (Alias) other;
        return value.equals(otherAlias.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
