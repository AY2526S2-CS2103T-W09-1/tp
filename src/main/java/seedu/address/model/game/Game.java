package seedu.address.model.game;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Game associated with a Contact in the address book.
 * Guarantees: immutable; name is valid as declared in {@link #isValidGameName(String)}
 */
public class Game {

    public static final String MESSAGE_CONSTRAINTS = "Game names must be 1 to 200 characters long and cannot be empty.";

    // Accepts any string that is between 1 and 200 characters long (ignoring leading/trailing spaces)
    public static final String VALIDATION_REGEX = "^.{1,200}$";

    public final String gameName;

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
     * Returns true if a given string is a valid game name.
     */
    public static boolean isValidGameName(String test) {
        return test.trim().matches(VALIDATION_REGEX);
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