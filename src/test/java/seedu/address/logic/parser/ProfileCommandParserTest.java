package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ViewProfileCommand;

public class ProfileCommandParserTest {

    private final ProfileCommandParser parser = new ProfileCommandParser();

    @Test
    public void parse_viewAction_returnsViewProfileCommand() {
        assertParseSuccess(parser, " view", new ViewProfileCommand());
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
