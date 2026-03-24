package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.logic.commands.Command;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses profile-related commands by dispatching on the action word (view, etc.).
 */
public class ProfileCommandParser implements Parser<Command> {

    public static final String COMMAND_WORD = "profile";
    public static final String MESSAGE_USAGE = """
            profile: Manages the user profile.
            profile view""";

    private static final Pattern ACTION_FORMAT = Pattern.compile("(?<action>\\S+)(?<arguments>.*)");

    @Override
    public Command parse(String args) throws ParseException {
        final Matcher matcher = ACTION_FORMAT.matcher(args.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, MESSAGE_USAGE));
        }

        final String action = matcher.group("action");
        final String arguments = matcher.group("arguments"); // pass in name/index

        return switch (action) {
        case "view" -> new ViewProfileCommandParser().parse(arguments);
        default -> throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        };
    }
}
