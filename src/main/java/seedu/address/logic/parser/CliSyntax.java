package seedu.address.logic.parser;

/**
 * Contains Command Line Interface (CLI) syntax definitions common to multiple commands
 */
public class CliSyntax {

    /* Prefix definitions */
    public static final Prefix PREFIX_NAME = new Prefix("n/");
    public static final Prefix PREFIX_TAG = new Prefix("t/");
    public static final Prefix PREFIX_GAME = new Prefix("g/");
    public static final Prefix PREFIX_ALIAS = new Prefix("al/");
    public static final Prefix PREFIX_NEW_ALIAS = new Prefix("na/");
    public static final Prefix PREFIX_NEW_NAME = new Prefix("e/");
}
