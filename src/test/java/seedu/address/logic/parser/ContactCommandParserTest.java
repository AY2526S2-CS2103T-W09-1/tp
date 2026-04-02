package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddContactCommand;
import seedu.address.logic.commands.DeleteContactCommand;
import seedu.address.logic.commands.EditContactCommand;
import seedu.address.model.person.Name;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.TypicalIndexes;

public class ContactCommandParserTest {

    private final ContactCommandParser parser = new ContactCommandParser();

    @Test
    public void parseAdd_validArgs_success() {
        AddContactCommand expected = new AddContactCommand(new PersonBuilder().withName("Alice").build());
        assertParseSuccess(parser, " add n/Alice", expected);
    }

    @Test
    public void parseDelete_validArgsByName_success() {
        DeleteContactCommand expected = new DeleteContactCommand(null, new Name("Alice"), false);
        assertParseSuccess(parser, " delete n/Alice", expected);
    }

    @Test
    public void parseDelete_validArgsByIndex_success() {
        DeleteContactCommand expected = new DeleteContactCommand(TypicalIndexes.INDEX_FIRST_PERSON, null, false);
        assertParseSuccess(parser, " delete 1", expected);
    }

    @Test
    public void parse_unknownAction_throwsParseException() {
        assertParseFailure(parser, " unknown", MESSAGE_UNKNOWN_COMMAND);
    }

    @Test
    public void parseEdit_validArgs_success() {
        EditContactCommand expectedInput = new EditContactCommand(null, new Name("Janelle"), new Name("Jan"), false);
        assertParseSuccess(parser, " edit n/Janelle e/Jan", expectedInput);
    }

    @Test
    public void parseEdit_missingArgs_failure() {
        assertParseFailure(parser, " edit",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditContactCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_emptyArgs_throwsParseException() {
        assertParseFailure(parser, "",
                String.format(seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                        ContactCommandParser.MESSAGE_USAGE));
    }
}
