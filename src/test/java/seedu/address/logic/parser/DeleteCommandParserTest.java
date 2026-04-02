package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.DeleteContactCommand;
import seedu.address.model.person.Name;

/**
 * As we are only doing white-box testing, our test cases do not cover path variations
 * outside of the DeleteContactCommand code.
 */
public class DeleteCommandParserTest {

    private DeleteContactCommandParser parser = new DeleteContactCommandParser();

    @Test
    public void parse_validArgsByName_returnsDeleteContactCommand() {
        assertParseSuccess(parser, " n/Alice Pauline",
                new DeleteContactCommand(null, new Name("Alice Pauline"), false));
    }

    @Test
    public void parse_validArgsByIndex_returnsDeleteContactCommand() {
        assertParseSuccess(parser, " 1", new DeleteContactCommand(INDEX_FIRST_PERSON, null, false));
    }

    @Test
    public void parse_userProfile_returnsDeleteContactCommand() {
        assertParseSuccess(parser, " 0", new DeleteContactCommand(null, null, true));
    }

    @Test
    public void parse_bothIndexAndName_throwsParseException() {
        assertParseFailure(parser, " 1 n/Alice Pauline",
                "Please provide either an index OR a name, not both.");
    }

    @Test
    public void parse_userProfileAndName_throwsParseException() {
        assertParseFailure(parser, " 0 n/Alice",
                "Please provide either an index OR a name, not both.");
    }

    @Test
    public void parse_emptyArgs_throwsParseException() {
        assertParseFailure(parser, "",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteContactCommand.MESSAGE_USAGE));
    }
}
