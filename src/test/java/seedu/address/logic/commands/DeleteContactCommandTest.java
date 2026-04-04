package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
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
        DeleteContactCommand deleteCommand = new DeleteContactCommand(null, personToDelete.getName(), false);

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
    public void execute_validIndexUnfilteredList_returnsConfirmation() throws Exception {
        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteContactCommand deleteCommand = new DeleteContactCommand(INDEX_FIRST_PERSON, null, false);

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
    public void execute_invalidIndex_throwsCommandException() {
        Index outOfBoundIndex = Index.fromZeroBased(model.getFilteredPersonList().size() + 1);
        DeleteContactCommand deleteCommand = new DeleteContactCommand(outOfBoundIndex, null, false);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_nameNotFound_throwsCommandException() {
        Name nonExistentName = new Name("NonExistent Person");
        DeleteContactCommand deleteCommand = new DeleteContactCommand(null, nonExistentName, false);

        assertCommandFailure(deleteCommand, model, DeleteContactCommand.MESSAGE_PERSON_NOT_FOUND);
    }

    @Test
    public void execute_nameLowercase_returnsConfirmation() throws Exception {
        // ALICE has name "Alice Pauline"; search with all-lowercase should still find her
        Name lowercaseName = new Name("alice pauline");
        DeleteContactCommand deleteCommand = new DeleteContactCommand(null, lowercaseName, false);

        CommandResult result = deleteCommand.execute(model);

        assertTrue(result.isAwaitingConfirmation());
        assertEquals(ALICE, result.getPendingPerson());
    }

    @Test
    public void execute_nameUppercase_returnsConfirmation() throws Exception {
        // ALICE has name "Alice Pauline"; search with all-uppercase should still find her
        Name uppercaseName = new Name("ALICE PAULINE");
        DeleteContactCommand deleteCommand = new DeleteContactCommand(null, uppercaseName, false);

        CommandResult result = deleteCommand.execute(model);

        assertTrue(result.isAwaitingConfirmation());
        assertEquals(ALICE, result.getPendingPerson());
    }

    @Test
    public void execute_nameMixedCase_returnsConfirmation() throws Exception {
        // ALICE has name "Alice Pauline"; search with mixed casing should still find her
        Name mixedCaseName = new Name("aLiCe pAuLiNe");
        DeleteContactCommand deleteCommand = new DeleteContactCommand(null, mixedCaseName, false);

        CommandResult result = deleteCommand.execute(model);

        assertTrue(result.isAwaitingConfirmation());
        assertEquals(ALICE, result.getPendingPerson());
    }

    @Test
    public void execute_validNameFilteredList_returnsConfirmation() throws Exception {
        showPersonAtIndex(model, INDEX_FIRST_PERSON); // shows only ALICE

        Person personToDelete = ALICE;
        DeleteContactCommand deleteCommand = new DeleteContactCommand(null, personToDelete.getName(), false);

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
        DeleteContactCommand deleteCommand = new DeleteContactCommand(null, CARL.getName(), false);

        assertCommandFailure(deleteCommand, model, DeleteContactCommand.MESSAGE_PERSON_NOT_FOUND);
    }

    @Test
    public void execute_userProfile_returnsConfirmation() throws Exception {
        Person userProfile = new Person(new Name("Janelle"), new java.util.HashSet<>(), true);
        AddressBook ab = new AddressBook();
        ab.addPerson(userProfile);
        Model profileModel = new ModelManager(ab, new UserPrefs());

        DeleteContactCommand deleteCommand = new DeleteContactCommand(null, null, true);
        CommandResult result = deleteCommand.execute(profileModel);

        assertTrue(result.isAwaitingConfirmation());
        assertEquals(userProfile, result.getPendingPerson());
    }

    @Test
    public void execute_noUserProfile_throwsCommandException() {
        Model emptyModel = new ModelManager(new AddressBook(), new UserPrefs());
        DeleteContactCommand deleteCommand = new DeleteContactCommand(null, null, true);
        emptyModel.deletePerson(emptyModel.getFilteredPersonList().get(0));

        assertCommandFailure(deleteCommand, emptyModel, "No user profile found.");
    }

    @Test
    public void execute_nullIndexAndName_throwsCommandException() {
        DeleteContactCommand deleteCommand = new DeleteContactCommand(null, null, false);
        assertCommandFailure(deleteCommand, model, DeleteContactCommand.MESSAGE_PERSON_NOT_FOUND);
    }

    @Test
    public void equals() {
        DeleteContactCommand deleteAliceByName =
                new DeleteContactCommand(null, ALICE.getName(), false);
        DeleteContactCommand deleteBensonByName =
                new DeleteContactCommand(null, BENSON.getName(), false);
        DeleteContactCommand deleteByIndex =
                new DeleteContactCommand(INDEX_FIRST_PERSON, null, false);

        // same object -> returns true
        assertTrue(deleteAliceByName.equals(deleteAliceByName));

        // same values -> returns true
        DeleteContactCommand deleteAliceCopy = new DeleteContactCommand(null, ALICE.getName(), false);
        assertTrue(deleteAliceByName.equals(deleteAliceCopy));

        // different types -> returns false
        assertFalse(deleteAliceByName.equals(1));

        // null -> returns false
        assertFalse(deleteAliceByName.equals(null));

        // different person -> returns false
        assertFalse(deleteAliceByName.equals(deleteBensonByName));

        // different identifier type -> returns false
        assertFalse(deleteAliceByName.equals(deleteByIndex));

        // same index -> returns true
        DeleteContactCommand deleteByIndexCopy = new DeleteContactCommand(INDEX_FIRST_PERSON, null, false);
        assertTrue(deleteByIndex.equals(deleteByIndexCopy));

        // different index -> returns false
        DeleteContactCommand deleteByIndex2 = new DeleteContactCommand(INDEX_SECOND_PERSON, null, false);
        assertFalse(deleteByIndex.equals(deleteByIndex2));
    }

    @Test
    public void undo_deleteContact_personRestored() {
        Person personToDelete = ALICE;
        DeleteContactCommand deleteCommand = new DeleteContactCommand(null, personToDelete.getName(), false);
        deleteCommand.setDeletedPerson(personToDelete);
        model.deletePerson(personToDelete);

        assertFalse(model.getFilteredPersonList().contains(personToDelete));

        deleteCommand.undo(model);

        assertTrue(model.getFilteredPersonList().contains(personToDelete));
    }

    @Test
    public void toStringMethod() {
        DeleteContactCommand deleteByName = new DeleteContactCommand(null, ALICE.getName(), false);
        String expectedByName = DeleteContactCommand.class.getCanonicalName()
                + "{targetIndex=null, targetName=" + ALICE.getName() + "}";
        assertEquals(expectedByName, deleteByName.toString());

        DeleteContactCommand deleteByIndex = new DeleteContactCommand(INDEX_FIRST_PERSON, null, false);
        String expectedByIndex = DeleteContactCommand.class.getCanonicalName()
                + "{targetIndex=" + INDEX_FIRST_PERSON + ", targetName=null}";
        assertEquals(expectedByIndex, deleteByIndex.toString());
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoPerson(Model model) {
        model.updateFilteredPersonList(p -> false);
        assertTrue(model.getFilteredPersonList().isEmpty());
    }
}
