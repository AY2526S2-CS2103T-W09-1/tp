package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.DeleteGameCommand;
import seedu.address.model.game.Game;
import seedu.address.model.person.Name;

public class DeleteGameCommandParserTest {

    private DeleteGameCommandParser parser = new DeleteGameCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Name expectedName = new Name("Alice Pauline");
        Game expectedGame = new Game("Minecraft");

        assertParseSuccess(parser, " n/Alice Pauline g/Minecraft",
                new DeleteGameCommand(expectedName, expectedGame));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteGameCommand.MESSAGE_USAGE);

        // missing game prefix
        assertParseFailure(parser, " n/Alice Pauline", expectedMessage);
    }
}