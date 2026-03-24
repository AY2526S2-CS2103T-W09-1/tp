package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;

import org.junit.jupiter.api.Test;

public class ContactCommandParserTest {

    private final ContactCommandParser parser = new ContactCommandParser();

    @Test
    public void parse_unknownAction_throwsParseException() {
        assertParseFailure(parser, " unknown", MESSAGE_UNKNOWN_COMMAND);
    }

    @Test
    public void parse_editAction_throwsParseException() {
        assertParseFailure(parser, " edit", MESSAGE_UNKNOWN_COMMAND);
    }

    @Test
    public void parse_emptyArgs_throwsParseException() {
        assertParseFailure(parser, "",
                String.format(seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                        ContactCommandParser.MESSAGE_USAGE));
    }
}
