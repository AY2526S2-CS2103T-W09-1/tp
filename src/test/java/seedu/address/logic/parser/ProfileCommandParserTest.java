package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ViewProfileCommand;

public class ProfileCommandParserTest {

    private final ProfileCommandParser parser = new ProfileCommandParser();

    @Test
    public void parse_viewAction_returnsViewProfileCommand() {
        assertParseSuccess(parser, " view", new ViewProfileCommand(null, null));
    }

    @Test
    public void parse_viewActionWithArgs_returnsViewProfileCommand() {
        // Simulates: profile view 1
        assertParseSuccess(parser, " view 1", new ViewProfileCommand(INDEX_FIRST_PERSON, null));
    }

    @Test
    public void parse_unknownAction_throwsParseException() {
        assertParseFailure(parser, " unknown", MESSAGE_UNKNOWN_COMMAND);
    }

    @Test
    public void parse_emptyArgs_throwsParseException() {
        assertParseFailure(parser, "",
                String.format(seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                        ProfileCommandParser.MESSAGE_USAGE));
    }
}
