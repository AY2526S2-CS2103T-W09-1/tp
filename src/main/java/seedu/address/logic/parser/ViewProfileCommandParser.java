package seedu.address.logic.parser;

import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.ViewProfileCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Name;

/**
 * Parses input arguments and creates a new ViewProfileCommand object
 */
public class ViewProfileCommandParser implements Parser<ViewProfileCommand> {

    @Override
    public ViewProfileCommand parse(String args) throws ParseException {
        // SCENARIO 1: No arguments (e.g., user just typed "profile view")
        if (args.trim().isEmpty()) {
            return new ViewProfileCommand(null, null);
        }

        // SCENARIO 2: Arguments exist (e.g., "profile view 1" or "profile view n/Benjamin")
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_NAME);

        String preamble = argMultimap.getPreamble();
        boolean hasNamePrefix = argMultimap.getValue(PREFIX_NAME).isPresent();

        ParserUtil.verifyIndexOrNamePresent(preamble, hasNamePrefix, ViewProfileCommand.MESSAGE_USAGE);

        Index index = preamble.isEmpty() ? null : ParserUtil.parseIndex(preamble);
        Name name = hasNamePrefix ? ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get()) : null;

        return new ViewProfileCommand(index, name);
    }
}
