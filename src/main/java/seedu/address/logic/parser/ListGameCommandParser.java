package seedu.address.logic.parser;

import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.ListGameCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Name;

/**
 * Parses input arguments and creates a new ListGameCommand object.
 */
public class ListGameCommandParser implements Parser<ListGameCommand> {

    @Override
    public ListGameCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME);

        String preamble = argMultimap.getPreamble();
        boolean hasNamePrefix = argMultimap.getValue(PREFIX_NAME).isPresent();

        // 1. Validate mutually exclusive index/name using our helper
        ParserUtil.verifyIndexOrNamePresent(preamble, hasNamePrefix, ListGameCommand.MESSAGE_USAGE);

        // 2. Extract whichever target is present
        Index index = preamble.isEmpty() ? null : ParserUtil.parseIndex(preamble);
        Name name = hasNamePrefix ? ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get()) : null;

        return new ListGameCommand(index, name);
    }
}
