package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.model.game.Game;
import seedu.address.testutil.PersonBuilder;

public class AliasMatchesPredicateTest {

    private Person buildPersonWithAlias(String name, String gameName, String aliasValue) {
        Set<Alias> aliases = new HashSet<>();
        aliases.add(new Alias(aliasValue));
        Set<Game> games = new HashSet<>();
        games.add(new Game(gameName, aliases));
        return new Person(new Name(name), new HashSet<>(), games);
    }

    @Test
    public void equals() {
        AliasMatchesPredicate firstPredicate = new AliasMatchesPredicate("BenJumpin");
        AliasMatchesPredicate secondPredicate = new AliasMatchesPredicate("AlicePlays");

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same value -> returns true
        assertTrue(firstPredicate.equals(new AliasMatchesPredicate("BenJumpin")));

        // case-insensitive same value -> returns true
        assertTrue(firstPredicate.equals(new AliasMatchesPredicate("benjumpin")));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different keyword -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_personHasMatchingAlias_returnsTrue() {
        AliasMatchesPredicate predicate = new AliasMatchesPredicate("BenJumpin");
        assertTrue(predicate.test(buildPersonWithAlias("Ben", "Valorant", "BenJumpin")));

        // case-insensitive match
        predicate = new AliasMatchesPredicate("benjumpin");
        assertTrue(predicate.test(buildPersonWithAlias("Ben", "Valorant", "BenJumpin")));
    }

    @Test
    public void test_personDoesNotHaveMatchingAlias_returnsFalse() {
        // no games
        AliasMatchesPredicate predicate = new AliasMatchesPredicate("BenJumpin");
        assertFalse(predicate.test(new PersonBuilder().withName("Alice").build()));

        // game exists but no matching alias
        assertFalse(predicate.test(buildPersonWithAlias("Alice", "Valorant", "AlicePlays")));
    }

    @Test
    public void toStringMethod() {
        AliasMatchesPredicate predicate = new AliasMatchesPredicate("BenJumpin");
        String expected = AliasMatchesPredicate.class.getCanonicalName() + "{keyword=BenJumpin}";
        assertEquals(expected, predicate.toString());
    }
}
