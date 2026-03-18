package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ListGameCommand;
import seedu.address.model.person.Name;

public class ListGameCommandParserTest {

    private ListGameCommandParser parser = new ListGameCommandParser();

    @Test
    public void parse_validArgsByName_returnsListGameCommand() {
        // Simulates: game list n/Alice Pauline
        assertParseSuccess(parser,
                " n/Alice Pauline",
                new ListGameCommand(null, new Name("Alice Pauline")));
    }

    @Test
    public void parse_validArgsByIndex_returnsListGameCommand() {
        // Simulates: game list 1
        assertParseSuccess(parser, " 1", new ListGameCommand(INDEX_FIRST_PERSON, null));
    }

    @Test
    public void parse_bothIndexAndName_throwsParseException() {
        // Simulates the mutually exclusive error: game list 1 n/Alice Pauline
        assertParseFailure(parser, " 1 n/Alice Pauline",
                "Please provide either an index OR a name, not both.");
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        // Missing both index and name completely (empty string)
        assertParseFailure(parser, " ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListGameCommand.MESSAGE_USAGE));
    }
}
