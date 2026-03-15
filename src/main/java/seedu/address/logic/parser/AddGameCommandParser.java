package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import java.util.stream.Stream;

import seedu.address.logic.commands.AddGameCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.game.Game;
import seedu.address.model.person.Name;

/**
 * Parses input arguments and creates a new AddGameCommand object.
 */
public class AddGameCommandParser implements Parser<AddGameCommand> {

    @Override
    public AddGameCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_GAME);

        if (!arePrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_GAME)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddGameCommand.MESSAGE_USAGE));
        }

        Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());

        // We will parse the game string directly here to keep it simple,
        // similar to how tags are parsed.
        String gameString = argMultimap.getValue(PREFIX_GAME).get();
        if (!Game.isValidGameName(gameString)) {
            throw new ParseException(Game.MESSAGE_CONSTRAINTS);
        }
        Game game = new Game(gameString);

        return new AddGameCommand(name, game);
    }

    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
