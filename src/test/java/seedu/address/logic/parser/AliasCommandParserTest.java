package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddAliasCommand;
import seedu.address.logic.commands.DeleteAliasCommand;
import seedu.address.model.game.Game;
import seedu.address.model.person.Alias;
import seedu.address.model.person.Name;

public class AliasCommandParserTest {

    private final AliasCommandParser parser = new AliasCommandParser();

    // Tests for add alias command
    @Test
    public void parse_addAliasValidInput_success() {
        AddAliasCommand expected = new AddAliasCommand(new Name("Benjamin"), new Game("Valorant"), new Alias("Benjumpin"));
        assertParseSuccess(parser, "add n/Benjamin al/Benjumpin", expected);
    }

    @Test
    public void parse_addAliasMissingNamePrefix_failure() {
        assertParseFailure(parser, "add Benjamin al/Benjumpin",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddAliasCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_addAliasMissingAliasPrefix_failure() {
        assertParseFailure(parser, "add n/Benjamin Benjumpin",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddAliasCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_addAliasMissingBothPrefixes_failure() {
        assertParseFailure(parser, "add Benjamin Benjumpin",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddAliasCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_addAliasNonEmptyPreamble_failure() {
        assertParseFailure(parser, "add extrawords n/Benjamin al/Benjumpin",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddAliasCommand.MESSAGE_USAGE));
    }

    // Tests for delete alias command
    @Test
    public void parse_deleteAliasValidInput_success() {
        DeleteAliasCommand expected = new DeleteAliasCommand(new Name("Benjamin"), new Game("Valorant"), new Alias("Benjumpin"));
        assertParseSuccess(parser, "delete n/Benjamin al/Benjumpin", expected);
    }

    @Test
    public void parse_deleteAliasMissingNamePrefix_failure() {
        assertParseFailure(parser, "delete Benjamin al/Benjumpin",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteAliasCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_deleteAliasMissingAliasPrefix_failure() {
        assertParseFailure(parser, "delete n/Benjamin Benjumpin",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteAliasCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_deleteAliasNonEmptyPreamble_failure() {
        assertParseFailure(parser, "delete extrawords n/Benjamin al/Benjumpin",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteAliasCommand.MESSAGE_USAGE));
    }
    // Tests for unknown actions and invalid formats

    @Test
    public void parse_unknownAction_failure() {
        assertParseFailure(parser, "edit n/Benjamin al/Benjumpin", MESSAGE_UNKNOWN_COMMAND);
    }

    @Test
    public void parse_emptyArgs_failure() {
        assertParseFailure(parser, "",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AliasCommandParser.MESSAGE_USAGE));
    }
}
