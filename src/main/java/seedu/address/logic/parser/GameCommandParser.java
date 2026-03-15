package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses user input for game-related commands.
 */
public class GameCommandParser implements Parser<Command> {

    public static final String COMMAND_WORD = "game";
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");

    @Override
    public Command parse(String userInput) throws ParseException {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());

        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");

        switch (commandWord) {
        case seedu.address.logic.commands.AddGameCommand.COMMAND_WORD:
            return new AddGameCommandParser().parse(arguments);

        case seedu.address.logic.commands.DeleteGameCommand.COMMAND_WORD:
            return new DeleteGameCommandParser().parse(arguments);

        case seedu.address.logic.commands.ListGameCommand.COMMAND_WORD:
            return new ListGameCommandParser().parse(arguments);

        default:
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }
    }
}
