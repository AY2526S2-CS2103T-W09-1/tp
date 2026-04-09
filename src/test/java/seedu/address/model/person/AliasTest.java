package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class AliasTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Alias(null));
    }

    @Test
    public void constructor_invalidAlias_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Alias("   "));
    }

    @Test
    public void isValidAlias_variousInputs_correctValidation() {
        // null alias
        assertThrows(NullPointerException.class, () -> Alias.isValidAlias(null));

        // invalid alias - blank/spaces
        assertFalse(Alias.isValidAlias("")); // empty string
        assertFalse(Alias.isValidAlias("   ")); // spaces only
        assertFalse(Alias.isValidAlias("a".repeat(Alias.MAX_LENGTH + 1))); // too long

        // invalid alias - contains greedy command prefixes
        assertFalse(Alias.isValidAlias("Benjumpin al/")); // contains " al/"
        assertFalse(Alias.isValidAlias("Striker g/")); // contains " g/"
        assertFalse(Alias.isValidAlias("Sniper n/")); // contains " n/"
        assertFalse(Alias.isValidAlias("Pro e/")); // contains " e/"
        assertFalse(Alias.isValidAlias("Noob na/")); // contains " na/"

        // valid alias - standard
        assertTrue(Alias.isValidAlias("fr1edDucky"));
        assertTrue(Alias.isValidAlias("da biggus brain"));
        assertTrue(Alias.isValidAlias("a".repeat(Alias.MAX_LENGTH)));
        assertTrue(Alias.isValidAlias("  trimmed alias  "));

        // valid alias - contains prefix text WITHOUT leading spaces (Greedy Parsing safe)
        assertTrue(Alias.isValidAlias("Jamal/"));
        assertTrue(Alias.isValidAlias("Sign/"));
        assertTrue(Alias.isValidAlias("Dog/"));
    }

    @Test
    public void constructor_trimsSurroundingWhitespace_success() {
        Alias alias = new Alias("  supportMain  ");
        assertTrue(alias.equals(new Alias("supportMain")));
        assertTrue(alias.toString().equals("supportMain"));
    }

    @Test
    public void equals_variousInputs_correctEquality() {
        Alias alias = new Alias("Valid Alias");

        // same values -> returns true
        assertTrue(alias.equals(new Alias("Valid Alias")));

        // same object -> returns true
        assertTrue(alias.equals(alias));

        // null -> returns false
        assertFalse(alias.equals(null));

        // different types -> returns false
        assertFalse(alias.equals(5.0f));

        // different values -> returns false
        assertFalse(alias.equals(new Alias("Other Alias")));

        // different case -> returns false (equals is case-sensitive)
        assertFalse(alias.equals(new Alias("valid alias")));
    }

    @Test
    public void hashCode_caseSensitive_differentCaseAliasesHaveDifferentHashCodes() {
        Alias alias1 = new Alias("CaseSensitive");
        Alias alias2 = new Alias("casesensitive");
        assertFalse(alias1.equals(alias2));
        assertFalse(alias1.hashCode() == alias2.hashCode());
    }

    @Test
    public void hashCode_sameValue_equalAliasesHaveSameHashCode() {
        Alias alias1 = new Alias("SameAlias");
        Alias alias2 = new Alias("SameAlias");
        assertTrue(alias1.equals(alias2));
        assertTrue(alias1.hashCode() == alias2.hashCode());
    }
}
