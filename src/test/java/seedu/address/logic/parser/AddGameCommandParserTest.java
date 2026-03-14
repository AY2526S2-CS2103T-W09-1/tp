package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddGameCommand;
import seedu.address.model.game.Game;
import seedu.address.model.person.Name;

public class AddGameCommandParserTest {

    private AddGameCommandParser parser = new AddGameCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Name expectedName = new Name("Alice Pauline");
        Game expectedGame = new Game("Minecraft");

        // valid input
        assertParseSuccess(parser, " n/Alice Pauline g/Minecraft",
                new AddGameCommand(expectedName, expectedGame));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddGameCommand.MESSAGE_USAGE);

        // missing name prefix
        assertParseFailure(parser, " Alice Pauline g/Minecraft", expectedMessage);

        // missing game prefix
        assertParseFailure(parser, " n/Alice Pauline Minecraft", expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid game (empty spaces)
        assertParseFailure(parser, " n/Alice Pauline g/  ", Game.MESSAGE_CONSTRAINTS);
    }
}