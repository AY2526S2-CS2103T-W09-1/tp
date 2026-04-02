package seedu.address.logic.parser;

import static seedu.address.logic.commands.CommandTestUtil.VALID_ALIAS_JETT;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ALIAS_STEVE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_GAME_VALORANT;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.EditAliasCommand;
import seedu.address.model.game.Game;
import seedu.address.model.person.Alias;
import seedu.address.model.person.Name;

public class EditAliasCommandParserTest {

    private final EditAliasCommandParser parser = new EditAliasCommandParser();

    @Test
    public void parse_byIndex_success() {
        EditAliasCommand expected = new EditAliasCommand(
                INDEX_FIRST_PERSON, null,
                new Game(VALID_GAME_VALORANT),
                new Alias(VALID_ALIAS_STEVE), new Alias(VALID_ALIAS_JETT),
                false);
        assertParseSuccess(parser,
                " 1 g/" + VALID_GAME_VALORANT + " al/" + VALID_ALIAS_STEVE + " na/" + VALID_ALIAS_JETT,
                expected);
    }

    @Test
    public void parse_byName_success() {
        EditAliasCommand expected = new EditAliasCommand(
                null, new Name(VALID_NAME_AMY),
                new Game(VALID_GAME_VALORANT),
                new Alias(VALID_ALIAS_STEVE), new Alias(VALID_ALIAS_JETT),
                false);
        assertParseSuccess(parser,
                " n/" + VALID_NAME_AMY + " g/" + VALID_GAME_VALORANT
                        + " al/" + VALID_ALIAS_STEVE + " na/" + VALID_ALIAS_JETT,
                expected);
    }

    @Test
    public void parse_userProfile_success() {
        EditAliasCommand expected = new EditAliasCommand(
                null, null,
                new Game(VALID_GAME_VALORANT),
                new Alias(VALID_ALIAS_STEVE), new Alias(VALID_ALIAS_JETT),
                true);
        assertParseSuccess(parser,
                " 0 g/" + VALID_GAME_VALORANT + " al/" + VALID_ALIAS_STEVE + " na/" + VALID_ALIAS_JETT,
                expected);
    }

    @Test
    public void parse_missingGame_failure() {
        assertParseFailure(parser,
                " 1 al/" + VALID_ALIAS_STEVE + " na/" + VALID_ALIAS_JETT,
                String.format(seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                        EditAliasCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_missingOldAlias_failure() {
        assertParseFailure(parser,
                " 1 g/" + VALID_GAME_VALORANT + " na/" + VALID_ALIAS_JETT,
                String.format(seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                        EditAliasCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_missingNewAlias_failure() {
        assertParseFailure(parser,
                " 1 g/" + VALID_GAME_VALORANT + " al/" + VALID_ALIAS_STEVE,
                String.format(seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                        EditAliasCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_missingIdentifier_failure() {
        assertParseFailure(parser,
                " g/" + VALID_GAME_VALORANT + " al/" + VALID_ALIAS_STEVE + " na/" + VALID_ALIAS_JETT,
                String.format(seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                        EditAliasCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_bothIndexAndName_failure() {
        assertParseFailure(parser,
                " 1 n/" + VALID_NAME_AMY + " g/" + VALID_GAME_VALORANT
                        + " al/" + VALID_ALIAS_STEVE + " na/" + VALID_ALIAS_JETT,
                "Please provide either an index OR a name, not both.");
    }

    @Test
    public void parse_userProfileWithName_failure() {
        assertParseFailure(parser,
                " 0 n/" + VALID_NAME_AMY + " g/" + VALID_GAME_VALORANT
                        + " al/" + VALID_ALIAS_STEVE + " na/" + VALID_ALIAS_JETT,
                "Please provide either an index OR a name, not both.");
    }
}
