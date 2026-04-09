package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.logic.commands.Command;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses contact-related commands by dispatching on the action word (add, delete, edit, view, etc.).
 */
public class ContactCommandParser implements Parser<Command> {

    public static final String COMMAND_WORD = "contact";
    public static final String MESSAGE_USAGE = """
              contact: Manages contacts.
              contact add n/NAME
              contact view me  (or shorthand: view me)
              contact delete n/NAME""";

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
        case "add" -> new AddContactCommandParser().parse(arguments);
        case "delete" -> new DeleteContactCommandParser().parse(arguments);
        case "edit" -> new EditContactCommandParser().parse(arguments);
        case "view" -> new ViewContactCommandParser().parse(arguments);
        default -> throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        };
    }
}
