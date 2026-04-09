package seedu.address.model.game;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class GameTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Game(null));
    }

    @Test
    public void constructor_invalidGameName_throwsIllegalArgumentException() {
        String invalidGameName = "";
        assertThrows(IllegalArgumentException.class, () -> new Game(invalidGameName));
    }

    @Test
    public void isValidGameName() {
        // null game name
        assertThrows(NullPointerException.class, () -> Game.isValidGameName(null));

        // invalid game name - blank/spaces
        assertFalse(Game.isValidGameName("")); // empty string
        assertFalse(Game.isValidGameName(" ")); // spaces only

        // invalid game name - contains greedy command prefixes
        assertFalse(Game.isValidGameName("Minecraft al/")); // contains " al/"
        assertFalse(Game.isValidGameName("Valorant g/")); // contains " g/"
        assertFalse(Game.isValidGameName("CSGO n/")); // contains " n/"
        assertFalse(Game.isValidGameName("Mario e/")); // contains " e/"

        // valid game names - standard
        assertTrue(Game.isValidGameName("Minecraft"));
        assertTrue(Game.isValidGameName("a")); // 1 character
        assertTrue(Game.isValidGameName("League of Legends")); // long name
        assertTrue(Game.isValidGameName("CS:GO 2!")); // special characters

        // valid game names - contains prefix text WITHOUT leading spaces (Greedy Parsing safe)
        assertTrue(Game.isValidGameName("Jamal/"));
        assertTrue(Game.isValidGameName("Sign/"));
        assertTrue(Game.isValidGameName("Dog/"));
    }

    @Test
    public void equals() {
        Game game1 = new Game("Minecraft");
        Game game2 = new Game("Minecraft");
        Game game3 = new Game("mineCraft"); // Case difference
        Game game4 = new Game("Valorant");

        // same object -> returns true
        assertTrue(game1.equals(game1));

        // same values -> returns true
        assertTrue(game1.equals(game2));

        // different case, same word -> returns true (Case-insensitive check)
        assertTrue(game1.equals(game3));

        // different word -> returns false
        assertFalse(game1.equals(game4));

        // null -> returns false
        assertFalse(game1.equals(null));

        // different type -> returns false
        assertFalse(game1.equals(5));
    }
}
