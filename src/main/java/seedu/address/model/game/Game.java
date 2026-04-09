package seedu.address.model.game;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;
import static seedu.address.logic.parser.CliSyntax.hasPrefix;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import seedu.address.model.person.Alias;

/**
 * Represents a Game associated with a Contact in the address book.
 * Guarantees: immutable; name is valid as declared in {@link #isValidGameName(String)}
 */
public class Game {

    public static final String MESSAGE_CONSTRAINTS = "Game names must be 1 to 200 characters long and cannot be empty."
            + "Additionally, names cannot contain spaces followed by command prefixes (e.g., ' al/', ' g/', ' n/').";

    // Accepts any string that is between 1 and 200 characters long (ignoring leading/trailing spaces)
    public static final String VALIDATION_REGEX = "^.{1,200}$";

    public final String gameName;
    private final Set<Alias> aliases = new HashSet<>();

    /**
     * Constructs a {@code Game}.
     *
     * @param gameName A valid game name.
     */
    public Game(String gameName) {
        requireNonNull(gameName);
        String trimmedName = gameName.trim();
        checkArgument(isValidGameName(trimmedName), MESSAGE_CONSTRAINTS);
        this.gameName = trimmedName;
    }

    /**
     * Constructs a {@code Game}.
     *
     * @param gameName A valid game name.
     * @param aliases list of aliases that belong to gameName.
     */
    public Game(String gameName, Set<Alias> aliases) {
        requireNonNull(gameName);
        requireNonNull(aliases);
        String trimmedName = gameName.trim();
        checkArgument(isValidGameName(trimmedName), MESSAGE_CONSTRAINTS);
        this.gameName = trimmedName;
        this.aliases.addAll(aliases);
    }

    /**
     * Returns true if a given string is a valid game name.
     */
    public static boolean isValidGameName(String test) {
        return test.trim().matches(VALIDATION_REGEX) && !hasPrefix(test);
    }

    public Set<Alias> getAliases() {
        return Collections.unmodifiableSet(aliases);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Game)) {
            return false;
        }

        Game otherGame = (Game) other;
        // Case-insensitive comparison as per your specifications
        return gameName.equalsIgnoreCase(otherGame.gameName);
    }

    @Override
    public int hashCode() {
        // Lowercase the hashcode so "Minecraft" and "minecraft" produce the same hash
        return gameName.toLowerCase().hashCode();
    }

    /**
     * Format state as text for viewing.
     */
    public String toString() {
        return '[' + gameName + ']';
    }
}
