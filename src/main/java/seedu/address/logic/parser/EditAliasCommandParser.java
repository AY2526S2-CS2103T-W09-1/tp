package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ALIAS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NEW_ALIAS;

import java.util.stream.Stream;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditAliasCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.game.Game;
import seedu.address.model.person.Alias;
import seedu.address.model.person.Name;

/**
 * Parses input arguments and creates a new EditAliasCommand object.
 */
public class EditAliasCommandParser implements Parser<EditAliasCommand> {

    @Override
    public EditAliasCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_GAME, PREFIX_ALIAS, PREFIX_NEW_ALIAS);

        if (!arePrefixesPresent(argMultimap, PREFIX_GAME, PREFIX_ALIAS, PREFIX_NEW_ALIAS)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditAliasCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_GAME, PREFIX_ALIAS, PREFIX_NEW_ALIAS);

        String preamble = argMultimap.getPreamble().trim();
        boolean useUserProfile = preamble.equals("0");
        boolean hasNamePrefix = argMultimap.getValue(PREFIX_NAME).isPresent();

        if (!useUserProfile) {
            ParserUtil.verifyIndexOrNamePresent(preamble, hasNamePrefix, EditAliasCommand.MESSAGE_USAGE);
        } else if (hasNamePrefix) {
            throw new ParseException("Please provide either an index OR a name, not both.");
        }

        Index index = (!useUserProfile && !preamble.isEmpty()) ? ParserUtil.parseIndex(preamble) : null;
        Name name = hasNamePrefix ? ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get()) : null;

        Game game = ParserUtil.parseGame(argMultimap.getValue(PREFIX_GAME).get());
        Alias oldAlias = ParserUtil.parseAlias(argMultimap.getValue(PREFIX_ALIAS).get());
        Alias newAlias = ParserUtil.parseAlias(argMultimap.getValue(PREFIX_NEW_ALIAS).get());

        return new EditAliasCommand(index, name, game, oldAlias, newAlias, useUserProfile);
    }

    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
