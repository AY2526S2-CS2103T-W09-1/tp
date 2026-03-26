package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code DeleteContactCommand}.
 */
public class DeleteContactCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validNameUnfilteredList_returnsConfirmation() throws Exception {
        Person personToDelete = ALICE;
        DeleteContactCommand deleteCommand = new DeleteContactCommand(personToDelete.getName());

        CommandResult result = deleteCommand.execute(model);

        assertTrue(result.isAwaitingConfirmation());
        assertEquals(personToDelete, result.getPendingPerson());

        String expectedMessage = String.format(DeleteContactCommand.MESSAGE_DELETE_CONFIRMATION,
                Messages.format(personToDelete), personToDelete.getName());
        assertEquals(expectedMessage, result.getFeedbackToUser());

        // model must NOT be modified until confirmation
        assertTrue(model.getFilteredPersonList().contains(personToDelete));
    }

    @Test
    public void execute_nameNotFound_throwsCommandException() {
        Name nonExistentName = new Name("NonExistent Person");
        DeleteContactCommand deleteCommand = new DeleteContactCommand(nonExistentName);

        assertCommandFailure(deleteCommand, model, DeleteContactCommand.MESSAGE_PERSON_NOT_FOUND);
    }

    @Test
    public void execute_validNameFilteredList_returnsConfirmation() throws Exception {
        showPersonAtIndex(model, INDEX_FIRST_PERSON); // shows only ALICE

        Person personToDelete = ALICE;
        DeleteContactCommand deleteCommand = new DeleteContactCommand(personToDelete.getName());

        CommandResult result = deleteCommand.execute(model);

        assertTrue(result.isAwaitingConfirmation());
        assertEquals(personToDelete, result.getPendingPerson());

        // model must NOT be modified until confirmation
        assertTrue(model.getFilteredPersonList().contains(personToDelete));
    }

    @Test
    public void execute_nameNotFoundInFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON); // shows only ALICE

        // BENSON exists in address book but not in filtered list
        DeleteContactCommand deleteCommand = new DeleteContactCommand(BENSON.getName());

        assertCommandFailure(deleteCommand, model, DeleteContactCommand.MESSAGE_PERSON_NOT_FOUND);
    }

    @Test
    public void equals() {
        DeleteContactCommand deleteAliceCommand = new DeleteContactCommand(ALICE.getName());
        DeleteContactCommand deleteBensonCommand = new DeleteContactCommand(BENSON.getName());

        // same object -> returns true
        assertTrue(deleteAliceCommand.equals(deleteAliceCommand));

        // same values -> returns true
        DeleteContactCommand deleteAliceCommandCopy = new DeleteContactCommand(ALICE.getName());
        assertTrue(deleteAliceCommand.equals(deleteAliceCommandCopy));

        // different types -> returns false
        assertFalse(deleteAliceCommand.equals(1));

        // null -> returns false
        assertFalse(deleteAliceCommand.equals(null));

        // different person -> returns false
        assertFalse(deleteAliceCommand.equals(deleteBensonCommand));
    }

    @Test
    public void undo_deleteContact_personRestored() {
        Person personToDelete = ALICE;
        DeleteContactCommand deleteCommand = new DeleteContactCommand(personToDelete.getName());
        deleteCommand.setDeletedPerson(personToDelete);
        model.deletePerson(personToDelete);

        assertFalse(model.getFilteredPersonList().contains(personToDelete));

        deleteCommand.undo(model);

        assertTrue(model.getFilteredPersonList().contains(personToDelete));
    }

    @Test
    public void toStringMethod() {
        DeleteContactCommand deleteCommand = new DeleteContactCommand(ALICE.getName());
        String expected = DeleteContactCommand.class.getCanonicalName() + "{targetName=" + ALICE.getName() + "}";
        assertEquals(expected, deleteCommand.toString());
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoPerson(Model model) {
        model.updateFilteredPersonList(p -> false);
        assertTrue(model.getFilteredPersonList().isEmpty());
    }
}
