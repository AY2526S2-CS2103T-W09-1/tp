package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ALIAS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import java.util.stream.Stream;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddAliasCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.game.Game;
import seedu.address.model.person.Alias;
import seedu.address.model.person.Name;

/**
 * Parses input arguments and creates a new AddAliasCommand object.
 */
public class AddAliasCommandParser implements Parser<AddAliasCommand> {

    @Override
    public AddAliasCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_GAME, PREFIX_ALIAS);

        // 1. Game and Alias prefixes are strictly required
        if (!arePrefixesPresent(argMultimap, PREFIX_GAME, PREFIX_ALIAS)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddAliasCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_GAME, PREFIX_ALIAS);

        String preamble = argMultimap.getPreamble();
        boolean hasNamePrefix = argMultimap.getValue(PREFIX_NAME).isPresent();

        // 2. Validate mutually exclusive index/name
        ParserUtil.verifyIndexOrNamePresent(preamble, hasNamePrefix, AddAliasCommand.MESSAGE_USAGE);

        // 3. Extract targets
        Index index = preamble.isEmpty() ? null : ParserUtil.parseIndex(preamble);
        Name name = hasNamePrefix ? ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get()) : null;

        // 4. Parse the game and alias
        Game game = ParserUtil.parseGame(argMultimap.getValue(PREFIX_GAME).get());
        Alias alias = ParserUtil.parseAlias(argMultimap.getValue(PREFIX_ALIAS).get());

        return new AddAliasCommand(index, name, game, alias);
    }

    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
