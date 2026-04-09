package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code ViewContactCommand}.
 */
public class ViewContactCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Person personToView = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        ViewContactCommand viewContactCommand = new ViewContactCommand(INDEX_FIRST_PERSON, null, false);

        String expectedMessage = String.format(ViewContactCommand.MESSAGE_SUCCESS_CONTACT,
                personToView.getName().fullName);

        // Viewing by index does not filter the list, so expectedModel remains the same
        CommandResult expectedCommandResult = new CommandResult(expectedMessage, false, false, personToView);

        assertCommandSuccess(viewContactCommand, model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_validNameUnfilteredList_success() {
        Person personToView = ALICE; // Alice is guaranteed to be in the TypicalAddressBook
        ViewContactCommand viewContactCommand = new ViewContactCommand(null, personToView.getName(),
                false);

        String expectedMessage = String.format(ViewContactCommand.MESSAGE_SUCCESS_CONTACT,
                personToView.getName().fullName);
        CommandResult expectedCommandResult = new CommandResult(expectedMessage,
                false, false, personToView);

        assertCommandSuccess(viewContactCommand, model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_validUserProfile_success() {
        Person userProfile = model.getFilteredPersonList().get(0);

        ViewContactCommand viewContactCommand = new ViewContactCommand(null, null, true);

        // 2. Use the correct success message for the "me" command
        String expectedMessage = ViewContactCommand.MESSAGE_SUCCESS_SELF;

        // 3. Since the command does model.updateFilteredPersonList(Person::isUserProfile);
        // we must do the same to our expectedModel so they match at the end!
        expectedModel.updateFilteredPersonList(Person::isUserProfile);

        CommandResult expectedCommandResult = new CommandResult(expectedMessage,
                false, false, userProfile);

        assertCommandSuccess(viewContactCommand, model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromZeroBased(model.getFilteredPersonList().size() + 1);
        ViewContactCommand viewContactCommand = new ViewContactCommand(outOfBoundIndex,
                null, false);

        assertCommandFailure(viewContactCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidNameUnfilteredList_throwsCommandException() {
        Name unknownName = new Name("Unknown Person");
        ViewContactCommand viewContactCommand = new ViewContactCommand(null,
                unknownName, false);

        assertCommandFailure(viewContactCommand, model, ViewContactCommand.MESSAGE_PERSON_NOT_FOUND);
    }

    @Test
    public void equals() {
        ViewContactCommand viewFirstCommand = new ViewContactCommand(INDEX_FIRST_PERSON,
                null, false);
        ViewContactCommand viewSecondCommand = new ViewContactCommand(INDEX_SECOND_PERSON,
                null, false);
        ViewContactCommand viewNameCommand = new ViewContactCommand(null,
                ALICE.getName(), false);
        ViewContactCommand viewProfileCommand = new ViewContactCommand(null,
                null, true);

        // same object -> returns true
        assertTrue(viewFirstCommand.equals(viewFirstCommand));

        // same values -> returns true
        ViewContactCommand viewFirstCommandCopy = new ViewContactCommand(INDEX_FIRST_PERSON,
                null, false);
        assertTrue(viewFirstCommand.equals(viewFirstCommandCopy));

        // different types -> returns false
        assertFalse(viewFirstCommand.equals(1));

        // null -> returns false
        assertFalse(viewFirstCommand.equals(null));

        // different index -> returns false
        assertFalse(viewFirstCommand.equals(viewSecondCommand));

        // different target types (index vs name) -> returns false
        assertFalse(viewFirstCommand.equals(viewNameCommand));

        // different target types (index vs profile) -> returns false
        assertFalse(viewFirstCommand.equals(viewProfileCommand));

        // same name -> returns true
        ViewContactCommand viewNameCommandCopy = new ViewContactCommand(null, ALICE.getName(), false);
        assertTrue(viewNameCommand.equals(viewNameCommandCopy));

        // Ensure profile equals its own copy
        ViewContactCommand viewProfileCommandCopy = new ViewContactCommand(null, null, true);
        assertTrue(viewProfileCommand.equals(viewProfileCommandCopy));
    }

    @Test
    public void hashCode_sameValues_sameHash() {
        ViewContactCommand a = new ViewContactCommand(INDEX_FIRST_PERSON, null, false);
        ViewContactCommand b = new ViewContactCommand(INDEX_FIRST_PERSON, null, false);
        assertEquals(a.hashCode(), b.hashCode());
    }
}
