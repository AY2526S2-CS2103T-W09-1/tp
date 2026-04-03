package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for EditContactCommand.
 */
public class EditContactCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_editByName_success() {
        Person personToEdit = model.getFilteredPersonList().get(0);
        Name newName = new Name("Alicia");
        EditContactCommand command = new EditContactCommand(null, personToEdit.getName(), newName, false);

        Person editedPerson = new PersonBuilder(personToEdit).withName("Alicia").build();
        String expectedMessage = String.format(EditContactCommand.MESSAGE_EDIT_PERSON_SUCCESS,
                personToEdit.getName(), newName);
        CommandResult expectedResult = new CommandResult(expectedMessage, false, false, editedPerson);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        assertCommandSuccess(command, model, expectedResult, expectedModel);
    }

    @Test
    public void execute_editByIndex_success() {
        Person personToEdit = model.getFilteredPersonList().get(0);
        Name newName = new Name("Alicia");
        EditContactCommand command = new EditContactCommand(INDEX_FIRST_PERSON, null, newName, false);

        Person editedPerson = new PersonBuilder(personToEdit).withName("Alicia").build();
        String expectedMessage = String.format(EditContactCommand.MESSAGE_EDIT_PERSON_SUCCESS,
                personToEdit.getName(), newName);
        CommandResult expectedResult = new CommandResult(expectedMessage, false, false, editedPerson);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        assertCommandSuccess(command, model, expectedResult, expectedModel);
    }

    @Test
    public void execute_nameNotFound_throwsCommandException() {
        EditContactCommand command = new EditContactCommand(null, new Name("NonExistent"), new Name("Ben"), false);
        assertCommandFailure(command, model, EditContactCommand.MESSAGE_PERSON_NOT_FOUND);
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        EditContactCommand command = new EditContactCommand(outOfBoundIndex, null, new Name("Ben"), false);
        assertCommandFailure(command, model, seedu.address.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_duplicateNameByName_throwsCommandException() {
        Person first = model.getFilteredPersonList().get(0);
        Person second = model.getFilteredPersonList().get(1);
        EditContactCommand command = new EditContactCommand(null, first.getName(), second.getName(), false);
        assertCommandFailure(command, model, EditContactCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_duplicateNameByIndex_throwsCommandException() {
        Person second = model.getFilteredPersonList().get(1);
        EditContactCommand command = new EditContactCommand(INDEX_FIRST_PERSON, null, second.getName(), false);
        assertCommandFailure(command, model, EditContactCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_sameNameEdit_success() throws Exception {
        Person person = model.getFilteredPersonList().get(0);
        EditContactCommand command = new EditContactCommand(null, person.getName(), person.getName(), false);
        command.execute(model);
    }

    @Test
    public void execute_userProfile_success() throws Exception {
        Person userProfile = new Person(new Name("John Doe"), new java.util.HashSet<>(),
                new java.util.HashSet<>(), true);
        AddressBook ab = new AddressBook();
        ab.addPerson(userProfile);
        Model profileModel = new ModelManager(ab, new UserPrefs());

        Name newName = new Name("Johnny");
        EditContactCommand command = new EditContactCommand(null, null, newName, true);
        CommandResult result = command.execute(profileModel);

        assertEquals(String.format(EditContactCommand.MESSAGE_EDIT_PERSON_SUCCESS, "John Doe", newName),
                result.getFeedbackToUser());
    }

    @Test
    public void execute_noUserProfile_throwsCommandException() {
        Model emptyModel = new ModelManager(new AddressBook(), new UserPrefs());
        EditContactCommand command = new EditContactCommand(null, null, new Name("Johnny"), true);
        assertCommandFailure(command, emptyModel, "No user profile found.");
    }

    @Test
    public void undo_editContact_restoresOriginalName() throws Exception {
        Person personToEdit = model.getFilteredPersonList().get(0);
        Name originalName = personToEdit.getName();
        Name newName = new Name("Alicia");

        EditContactCommand command = new EditContactCommand(null, originalName, newName, false);
        command.execute(model);

        assertEquals(newName, model.getFilteredPersonList().get(0).getName());

        command.undo(model);

        assertEquals(originalName, model.getFilteredPersonList().get(0).getName());
    }

    @Test
    public void equals() {
        Name alice = new Name("Alice");
        Name bob = new Name("Bob");
        Name charlie = new Name("Charlie");

        EditContactCommand editByName = new EditContactCommand(null, alice, bob, false);
        EditContactCommand editByIndex = new EditContactCommand(INDEX_FIRST_PERSON, null, bob, false);

        // same object -> true
        assertTrue(editByName.equals(editByName));

        // same values (by name) -> true
        assertTrue(editByName.equals(new EditContactCommand(null, alice, bob, false)));

        // same values (by index) -> true
        assertTrue(editByIndex.equals(new EditContactCommand(INDEX_FIRST_PERSON, null, bob, false)));

        // null -> false
        assertFalse(editByName.equals(null));

        // different type -> false
        assertFalse(editByName.equals(new ClearCommand()));

        // different target (index vs name) -> false
        assertFalse(editByName.equals(editByIndex));

        // different new name -> false
        assertFalse(editByName.equals(new EditContactCommand(null, alice, charlie, false)));

        // different target name -> false
        assertFalse(editByName.equals(new EditContactCommand(null, bob, charlie, false)));
    }

    @Test
    public void toStringMethod() {
        Name targetName = new Name("Alice");
        Name newName = new Name("Bob");
        EditContactCommand command = new EditContactCommand(null, targetName, newName, false);
        String expected = EditContactCommand.class.getCanonicalName()
                + "{targetIndex=" + null + ", targetName=" + targetName + ", newName=" + newName + "}";
        assertEquals(expected, command.toString());
    }
}
