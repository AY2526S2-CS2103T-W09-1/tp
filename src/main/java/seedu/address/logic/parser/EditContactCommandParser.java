package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NEW_NAME;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditContactCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Name;

/**
 * Parses input arguments and creates a new EditContactCommand object.
 */
public class EditContactCommandParser implements Parser<EditContactCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditContactCommand
     * and returns an EditContactCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditContactCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_NEW_NAME);

        if (!argMultimap.getValue(PREFIX_NEW_NAME).isPresent()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditContactCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_NEW_NAME);

        String preamble = argMultimap.getPreamble().trim();
        boolean useUserProfile = preamble.equals("0");
        boolean hasNamePrefix = argMultimap.getValue(PREFIX_NAME).isPresent();

        if (!useUserProfile) {
            ParserUtil.verifyIndexOrNamePresent(preamble, hasNamePrefix, EditContactCommand.MESSAGE_USAGE);
        } else if (hasNamePrefix) {
            throw new ParseException("Please provide either an index OR a name, not both.");
        }

        Name newName = ParserUtil.parseName(argMultimap.getValue(PREFIX_NEW_NAME).get());

        if (useUserProfile) {
            return new EditContactCommand(null, null, newName, true);
        }

        if (hasNamePrefix) {
            Name targetName = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());
            return new EditContactCommand(null, targetName, newName, false);
        }

        Index index = ParserUtil.parseIndex(preamble);
        return new EditContactCommand(index, null, newName, false);
    }
}
