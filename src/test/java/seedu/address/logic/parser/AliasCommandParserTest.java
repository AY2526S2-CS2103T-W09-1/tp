package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddAliasCommand;
import seedu.address.logic.commands.DeleteAliasCommand;
import seedu.address.model.game.Game;
import seedu.address.model.person.Alias;
import seedu.address.model.person.Name;

public class AliasCommandParserTest {

    private final AliasCommandParser parser = new AliasCommandParser();

    // Tests for ADD alias command

    @Test
    public void parse_addAliasValidInputByName_success() {
        AddAliasCommand expected = new AddAliasCommand(null, new Name("Benjamin"),
                new Game("Valorant"), new Alias("Benjumpin"));
        assertParseSuccess(parser, "add n/Benjamin g/Valorant al/Benjumpin", expected);
    }

    @Test
    public void parse_addAliasValidInputByIndex_success() {
        AddAliasCommand expected = new AddAliasCommand(INDEX_FIRST_PERSON, null,
                new Game("Valorant"), new Alias("Benjumpin"));
        assertParseSuccess(parser, "add 1 g/Valorant al/Benjumpin", expected);
    }

    @Test
    public void parse_addAliasMutuallyExclusive_failure() {
        // Triggers the mutually exclusive error by providing both an index and a name
        assertParseFailure(parser, "add 1 n/Benjamin g/Valorant al/Benjumpin",
                "Please provide either an index OR a name, not both.");
    }

    @Test
    public void parse_addAliasMissingBothIndexAndName_failure() {
        assertParseFailure(parser, "add g/Valorant al/Benjumpin",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddAliasCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_addAliasMissingGameOrAliasPrefix_failure() {
        assertParseFailure(parser, "add n/Benjamin Benjumpin",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddAliasCommand.MESSAGE_USAGE));
    }

    // Tests for DELETE alias command

    @Test
    public void parse_deleteAliasValidInputByName_success() {
        DeleteAliasCommand expected = new DeleteAliasCommand(null, new Name("Benjamin"),
                new Game("Valorant"), new Alias("Benjumpin"));
        assertParseSuccess(parser, "delete n/Benjamin g/Valorant al/Benjumpin", expected);
    }

    @Test
    public void parse_deleteAliasValidInputByIndex_success() {
        DeleteAliasCommand expected = new DeleteAliasCommand(INDEX_FIRST_PERSON, null,
                new Game("Valorant"), new Alias("Benjumpin"));
        assertParseSuccess(parser, "delete 1 g/Valorant al/Benjumpin", expected);
    }

    @Test
    public void parse_deleteAliasMutuallyExclusive_failure() {
        // Triggers the mutually exclusive error by providing both an index and a name
        assertParseFailure(parser, "delete 1 n/Benjamin g/Valorant al/Benjumpin",
                "Please provide either an index OR a name, not both.");
    }

    @Test
    public void parse_deleteAliasMissingBothIndexAndName_failure() {
        assertParseFailure(parser, "delete g/Valorant al/Benjumpin",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteAliasCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_deleteAliasMissingGameOrAliasPrefix_failure() {
        assertParseFailure(parser, "delete n/Benjamin Benjumpin",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteAliasCommand.MESSAGE_USAGE));
    }

    // Tests for unknown actions and formats

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
