package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.EditContactCommand;
import seedu.address.model.person.Name;

public class EditContactCommandParserTest {

    private final EditContactCommandParser parser = new EditContactCommandParser();

    @Test
    public void parse_byName_success() {
        EditContactCommand expected = new EditContactCommand(
                null, new Name(VALID_NAME_AMY), new Name(VALID_NAME_BOB), false);
        assertParseSuccess(parser, " n/" + VALID_NAME_AMY + " e/" + VALID_NAME_BOB, expected);
    }

    @Test
    public void parse_byIndex_success() {
        EditContactCommand expected = new EditContactCommand(INDEX_FIRST_PERSON, null, new Name(VALID_NAME_BOB), false);
        assertParseSuccess(parser, " 1 e/" + VALID_NAME_BOB, expected);
    }

    @Test
    public void parse_userProfile_success() {
        EditContactCommand expected = new EditContactCommand(null, null, new Name(VALID_NAME_BOB), true);
        assertParseSuccess(parser, " 0 e/" + VALID_NAME_BOB, expected);
    }

    @Test
    public void parse_missingNewName_failure() {
        assertParseFailure(parser, " n/" + VALID_NAME_AMY,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditContactCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_missingBothIdentifierAndNewName_failure() {
        assertParseFailure(parser, "",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditContactCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_bothIndexAndName_failure() {
        assertParseFailure(parser, " 1 n/" + VALID_NAME_AMY + " e/" + VALID_NAME_BOB,
                "Please provide either an index OR a name, not both.");
    }

    @Test
    public void parse_missingIdentifier_failure() {
        assertParseFailure(parser, " e/" + VALID_NAME_BOB,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditContactCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidName_failure() {
        assertParseFailure(parser, " n/James& e/" + VALID_NAME_BOB, Name.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_invalidNewName_failure() {
        assertParseFailure(parser, " n/" + VALID_NAME_AMY + " e/James&", Name.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_invalidIndex_failure() {
        assertParseFailure(parser, " 0 n/" + VALID_NAME_AMY + " e/" + VALID_NAME_BOB,
                "Please provide either an index OR a name, not both.");
    }
}
