package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.game.Game;
import seedu.address.model.person.Alias;

public class JsonAdaptedGameTest {

    @Test
    public void toModelType_validGame_returnsGame() throws Exception {
        Game game = new Game("Minecraft");
        JsonAdaptedGame adapted = new JsonAdaptedGame(game);
        assertEquals(game, adapted.toModelType());
    }

    @Test
    public void toModelType_validGameWithAliases_returnsGame() throws Exception {
        Set<Alias> aliases = new HashSet<>();
        aliases.add(new Alias("CraftyMiner"));
        Game game = new Game("Minecraft", aliases);
        JsonAdaptedGame adapted = new JsonAdaptedGame(game);
        assertEquals(game, adapted.toModelType());
    }

    @Test
    public void toModelType_nullGameName_throwsIllegalValueException() {
        List<JsonAdaptedAlias> emptyAliases = new ArrayList<>();
        JsonAdaptedGame adapted = new JsonAdaptedGame(null, emptyAliases);
        assertThrows(IllegalValueException.class, adapted::toModelType);
    }

    @Test
    public void toModelType_invalidAlias_throwsIllegalValueException() {
        List<JsonAdaptedAlias> invalidAliases = new ArrayList<>();
        invalidAliases.add(new JsonAdaptedAlias(""));
        JsonAdaptedGame adapted = new JsonAdaptedGame("Minecraft", invalidAliases);
        assertThrows(IllegalValueException.class, adapted::toModelType);
    }

    @Test
    public void toModelType_nullAliasesList_returnsGame() throws Exception {
        JsonAdaptedGame adapted = new JsonAdaptedGame("Valorant", null);
        assertEquals(new Game("Valorant"), adapted.toModelType());
    }
}
