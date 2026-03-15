package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ALIAS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import java.util.stream.Stream;

import seedu.address.logic.commands.AddAliasCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Alias;
import seedu.address.model.person.Name;
import seedu.address.model.game.Game;

/**
 * Parses input arguments and creates a new AddAliasCommand object.
 */
public class AddAliasCommandParser implements Parser<AddAliasCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddAliasCommand
     * and returns an AddAliasCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format.
     */
    public AddAliasCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_GAME, PREFIX_ALIAS);

        if (!arePrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_GAME, PREFIX_ALIAS)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AddAliasCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_GAME, PREFIX_ALIAS);
        Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());
        Game game = ParserUtil.parseGame(argMultimap.getValue(PREFIX_GAME).get());
        Alias alias = ParserUtil.parseAlias(argMultimap.getValue(PREFIX_ALIAS).get());

        return new AddAliasCommand(name, game, alias);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
