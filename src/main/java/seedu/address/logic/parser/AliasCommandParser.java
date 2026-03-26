package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.logic.commands.Command;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses alias-related commands by dispatching on the action word (add, delete, etc.).
 */
public class AliasCommandParser implements Parser<Command> {

    public static final String COMMAND_WORD = "alias";
    public static final String MESSAGE_USAGE = """
              alias: Manages aliases.
              alias add n/NAME g/GAME al/ALIAS
              alias delete n/NAME g/GAME al/ALIAS
              alias edit n/NAME g/GAME al/OLD_ALIAS na/NEW_ALIAS""";

    private static final Pattern ACTION_FORMAT = Pattern.compile("(?<action>\\S+)(?<arguments>.*)");

    @Override
    public Command parse(String args) throws ParseException {
        final Matcher matcher = ACTION_FORMAT.matcher(args.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, MESSAGE_USAGE));
        }

        final String action = matcher.group("action");
        final String arguments = matcher.group("arguments");

        return switch (action) {
        case "add" -> new AddAliasCommandParser().parse(arguments);
        case "delete" -> new DeleteAliasCommandParser().parse(arguments);
        case "edit" -> new EditAliasCommandParser().parse(arguments);
        default -> throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        };
    }
}
