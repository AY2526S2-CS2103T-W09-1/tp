package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class CommandResultTest {
    @Test
    public void equals() {
        CommandResult commandResult = new CommandResult("feedback");

        // same values -> returns true
        assertTrue(commandResult.equals(new CommandResult("feedback")));
        assertTrue(commandResult.equals(new CommandResult("feedback", false, false)));
        assertTrue(commandResult.equals(new CommandResult("feedback", false, false, null)));

        // same object -> returns true
        assertTrue(commandResult.equals(commandResult));

        // null -> returns false
        assertFalse(commandResult.equals(null));

        // different types -> returns false
        assertFalse(commandResult.equals(0.5f));

        // different feedbackToUser value -> returns false
        assertFalse(commandResult.equals(new CommandResult("different")));

        // different showHelp value -> returns false
        assertFalse(commandResult.equals(new CommandResult("feedback", true, false)));

        // different exit value -> returns false
        assertFalse(commandResult.equals(new CommandResult("feedback", false, true)));

        // awaiting confirmation vs not -> returns false
        Person person = new PersonBuilder().withName("Alice Pauline").build();
        assertFalse(commandResult.equals(new CommandResult("feedback", person)));

        // different personToView value -> returns false
        assertFalse(commandResult.equals(new CommandResult("feedback", false, false, person)));

        // different pendingPerson value -> returns false
        Person alice = new PersonBuilder().withName("Alice").build();
        Person bob = new PersonBuilder().withName("Bob").build();
        CommandResult commandResultAlice = new CommandResult("feedback", alice);
        CommandResult commandResultBob = new CommandResult("feedback", bob);
        assertFalse(commandResultAlice.equals(commandResultBob));
    }

    @Test
    public void hashcode() {
        CommandResult commandResult = new CommandResult("feedback");

        // same values -> returns same hashcode
        assertEquals(commandResult.hashCode(), new CommandResult("feedback").hashCode());

        // different feedbackToUser value -> returns different hashcode
        assertNotEquals(commandResult.hashCode(), new CommandResult("different").hashCode());

        // different showHelp value -> returns different hashcode
        assertNotEquals(commandResult.hashCode(), new CommandResult("feedback", true, false).hashCode());

        // different exit value -> returns different hashcode
        assertNotEquals(commandResult.hashCode(), new CommandResult("feedback", false, true).hashCode());

        // different personToView value -> returns different hashcode
        Person person = new PersonBuilder().withName("Alice Pauline").build();
        assertNotEquals(commandResult.hashCode(), new CommandResult("feedback", false, false, person).hashCode());
    }

    @Test
    public void isAwaitingConfirmation_defaultConstructor_returnsFalse() {
        CommandResult commandResult = new CommandResult("feedback");
        assertFalse(commandResult.isAwaitingConfirmation());
        assertNull(commandResult.getPendingPerson());
        assertNull(commandResult.getViewedPerson());
    }

    @Test
    public void isAwaitingConfirmation_confirmationConstructor_returnsTrue() {
        Person person = new PersonBuilder().withName("Alice Pauline").build();
        CommandResult commandResult = new CommandResult("confirm?", person);

        assertTrue(commandResult.isAwaitingConfirmation());
        assertEquals(person, commandResult.getPendingPerson());
        assertFalse(commandResult.isShowHelp());
        assertFalse(commandResult.isExit());
        assertNull(commandResult.getViewedPerson());
    }

    @Test
    public void toStringMethod() {
        CommandResult commandResult = new CommandResult("feedback");
        String expected = CommandResult.class.getCanonicalName() + "{feedbackToUser="
                + commandResult.getFeedbackToUser() + ", showHelp=" + commandResult.isShowHelp()
                + ", exit=" + commandResult.isExit() + ", awaitingConfirmation="
                + commandResult.isAwaitingConfirmation() + ", pendingPerson="
                + commandResult.getPendingPerson() + ", personToView="
                + commandResult.getViewedPerson() + "}";
        assertEquals(expected, commandResult.toString());
    }
}
