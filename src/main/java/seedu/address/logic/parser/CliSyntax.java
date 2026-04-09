package seedu.address.logic.parser;

/**
 * Contains Command Line Interface (CLI) syntax definitions common to multiple commands
 */
public class CliSyntax {

    /* Prefix definitions */
    public static final Prefix PREFIX_NAME = new Prefix("n/");
    public static final Prefix PREFIX_GAME = new Prefix("g/");
    public static final Prefix PREFIX_ALIAS = new Prefix("al/");
    public static final Prefix PREFIX_NEW_ALIAS = new Prefix("na/");
    public static final Prefix PREFIX_NEW_NAME = new Prefix("e/");

    /* Array containing all prefix definitions for easy iteration */
    public static final Prefix[] ALL_PREFIXES = new Prefix[] {
        PREFIX_NAME,
        PREFIX_GAME,
        PREFIX_ALIAS,
        PREFIX_NEW_ALIAS,
        PREFIX_NEW_NAME
    };

    /**
     * Checks if the specified string contains any defined command prefixes preceded by a space.
     * This method is typically used for validation to prevent greedy parsing. It returns
     * {@code false} if a reserved prefix is found, indicating the string is invalid or
     * contains illegal prefix sequences.
     *
     * @param test The string to be tested.
     * @return {@code false} if the string contains a space followed by any prefix defined
     *     in {@code ALL_PREFIXES}; {@code true} if no such prefixes are found.
     */
    public static boolean hasPrefix(String test) {
        for (Prefix prefix : ALL_PREFIXES) {
            if (test.contains(" " + prefix.getPrefix())) {
                return true;
            }
        }
        return false;
    }
}
