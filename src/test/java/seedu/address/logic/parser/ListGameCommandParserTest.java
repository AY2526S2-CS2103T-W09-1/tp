package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.ListGameCommand;
import seedu.address.model.person.Name;

public class ListGameCommandParserTest {

    private final ListGameCommandParser parser = new ListGameCommandParser();

    private static final String PROFILE_MUTUALLY_EXCLUSIVE_ERROR =
            "Please do not provide a name prefix (n/) when targeting your own profile with 'me'.";

    @Test
    public void parse_validArgsByName_returnsListGameCommand() {
        // Simulates: game list n/Alice Pauline
        assertParseSuccess(parser,
                " n/Alice Pauline",
                new ListGameCommand(null, new Name("Alice Pauline"), false));
    }

    @Test
    public void parse_validArgsByIndex_returnsListGameCommand() {
        // Simulates: game list 1
        assertParseSuccess(parser, " 1", new ListGameCommand(INDEX_FIRST_PERSON, null, false));
    }

    @Test
    public void parse_validUserProfile_returnsListGameCommand() {
        // Simulates: game list me (user profile)
        assertParseSuccess(parser, " me", new ListGameCommand(null, null, true));

        // Ensure whitespace and case-insensitivity don't break it
        assertParseSuccess(parser, "   ME   ", new ListGameCommand(null, null, true));
    }

    @Test
    public void parse_bothIndexAndName_throwsParseException() {
        // Simulates the mutually exclusive error: game list 1 n/Alice Pauline
        assertParseFailure(parser, " 1 n/Alice Pauline",
                "Please provide either an index OR a name, not both.");
    }

    @Test
    public void parse_profileWithNamePrefix_throwsParseException() {
        // "me" and name prefix together is invalid
        assertParseFailure(parser, " me n/Alice Pauline",
                PROFILE_MUTUALLY_EXCLUSIVE_ERROR);
    }

    @Test
    public void parse_duplicatePrefixes_throwsParseException() {
        // Simulates providing multiple names: game list n/Alice n/Bob
        assertParseFailure(parser, " n/Alice n/Bob",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        // Missing both index and name completely (empty string)
        assertParseFailure(parser, "",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListGameCommand.MESSAGE_USAGE));
        assertParseFailure(parser, " ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListGameCommand.MESSAGE_USAGE));
    }
}
