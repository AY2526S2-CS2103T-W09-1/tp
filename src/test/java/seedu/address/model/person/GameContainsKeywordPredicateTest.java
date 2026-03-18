package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class GameContainsKeywordPredicateTest {

    @Test
    public void equals() {
        GameContainsKeywordPredicate firstPredicate = new GameContainsKeywordPredicate("Valorant");
        GameContainsKeywordPredicate secondPredicate = new GameContainsKeywordPredicate("Minecraft");

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same value -> returns true
        assertTrue(firstPredicate.equals(new GameContainsKeywordPredicate("Valorant")));

        // case-insensitive same value -> returns true
        assertTrue(firstPredicate.equals(new GameContainsKeywordPredicate("valorant")));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different keyword -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_personHasMatchingGame_returnsTrue() {
        GameContainsKeywordPredicate predicate = new GameContainsKeywordPredicate("Valorant");
        assertTrue(predicate.test(new PersonBuilder().withName("Alice").withGames("Valorant").build()));

        // case-insensitive match
        predicate = new GameContainsKeywordPredicate("valorant");
        assertTrue(predicate.test(new PersonBuilder().withName("Alice").withGames("Valorant").build()));

        // person with multiple games
        predicate = new GameContainsKeywordPredicate("Minecraft");
        assertTrue(predicate.test(new PersonBuilder().withName("Bob").withGames("Valorant", "Minecraft").build()));
    }

    @Test
    public void test_personDoesNotHaveMatchingGame_returnsFalse() {
        // no games
        GameContainsKeywordPredicate predicate = new GameContainsKeywordPredicate("Valorant");
        assertFalse(predicate.test(new PersonBuilder().withName("Alice").build()));

        // different game
        assertFalse(predicate.test(new PersonBuilder().withName("Alice").withGames("Minecraft").build()));
    }

    @Test
    public void toStringMethod() {
        GameContainsKeywordPredicate predicate = new GameContainsKeywordPredicate("Valorant");
        String expected = GameContainsKeywordPredicate.class.getCanonicalName() + "{keyword=Valorant}";
        assertEquals(expected, predicate.toString());
    }
}
