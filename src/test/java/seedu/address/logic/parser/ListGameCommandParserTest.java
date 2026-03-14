package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ListGameCommand;
import seedu.address.model.person.Name;

public class ListGameCommandParserTest {

    private ListGameCommandParser parser = new ListGameCommandParser();

    @Test
    public void parse_validArgs_returnsListGameCommand() {
        assertParseSuccess(parser, " n/Alice Pauline", new ListGameCommand(new Name("Alice Pauline")));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, " Alice Pauline",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListGameCommand.MESSAGE_USAGE));
    }
}