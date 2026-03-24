package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.DeleteGameCommand;
import seedu.address.model.game.Game;
import seedu.address.model.person.Name;

public class DeleteGameCommandParserTest {

    private DeleteGameCommandParser parser = new DeleteGameCommandParser();
    private final Game validGame = new Game("Minecraft");
    private final Name validName = new Name("Zi Xuan");

    @Test
    public void parse_validArgsByIndex_returnsDeleteGameCommand() {
        // Simulates: game delete 1 g/Minecraft
        assertParseSuccess(parser, " 1 g/Minecraft",
                new DeleteGameCommand(INDEX_FIRST_PERSON, null, validGame, false));
    }

    @Test
    public void parse_validArgsByName_returnsDeleteGameCommand() {
        // Simulates: game delete n/Zi Xuan g/Minecraft
        assertParseSuccess(parser, " n/Zi Xuan g/Minecraft",
                new DeleteGameCommand(null, validName, validGame, false));
    }

    @Test
    public void parse_bothIndexAndName_throwsParseException() {
        // Simulates the mutually exclusive error: game delete 1 n/Zi Xuan g/Minecraft
        assertParseFailure(parser, " 1 n/Zi Xuan g/Minecraft",
                "Please provide either an index OR a name, not both.");
    }

    @Test
    public void parse_missingGame_throwsParseException() {
        // Missing the game prefix completely
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteGameCommand.MESSAGE_USAGE);

        assertParseFailure(parser, " 1", expectedMessage);
        assertParseFailure(parser, " n/Zi Xuan", expectedMessage);
    }

    @Test
    public void parse_indexZero_returnsUserProfileCommand() {
        // Simulates: game delete 0 g/Minecraft (user profile)
        assertParseSuccess(parser, " 0 g/Minecraft",
                new DeleteGameCommand(null, null, validGame, true));
    }

    @Test
    public void parse_indexZeroWithName_throwsParseException() {
        // Index 0 and name prefix together is invalid
        assertParseFailure(parser, " 0 n/Zi Xuan g/Minecraft",
                "Please provide either an index OR a name, not both.");
    }
}
