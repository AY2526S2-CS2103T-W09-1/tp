package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

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
    public void execute_validName_success() {
        Person personToEdit = model.getFilteredPersonList().get(0); // Alice Pauline
        Name newName = new Name("Alicia");
        EditContactCommand command = new EditContactCommand(personToEdit.getName(), newName);

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
        EditContactCommand command = new EditContactCommand(new Name("NonExistent"), new Name("Ben"));
        assertCommandFailure(command, model, EditContactCommand.MESSAGE_PERSON_NOT_FOUND);
    }

    @Test
    public void execute_duplicateName_throwsCommandException() {
        Person first = model.getFilteredPersonList().get(0); // Alice Pauline
        Person second = model.getFilteredPersonList().get(1); // Benson Meier
        EditContactCommand command = new EditContactCommand(first.getName(), second.getName());
        assertCommandFailure(command, model, EditContactCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_sameNameEdit_success() throws Exception {
        Person person = model.getFilteredPersonList().get(0); // Alice Pauline
        EditContactCommand command = new EditContactCommand(person.getName(), person.getName());
        // isSamePerson returns true → duplicate check skipped → succeeds
        command.execute(model);
    }


    @Test
    public void equals() {
        Name alice = new Name("Alice");
        Name bob = new Name("Bob");
        Name charlie = new Name("Charlie");

        EditContactCommand editAliceToBob = new EditContactCommand(alice, bob);

        // same object -> true
        assertTrue(editAliceToBob.equals(editAliceToBob));

        // same values -> true
        assertTrue(editAliceToBob.equals(new EditContactCommand(alice, bob)));

        // null -> false
        assertFalse(editAliceToBob.equals(null));

        // different type -> false
        assertFalse(editAliceToBob.equals(new ClearCommand()));

        // different new name -> false
        assertFalse(editAliceToBob.equals(new EditContactCommand(alice, charlie)));

        // different target name -> false
        assertFalse(editAliceToBob.equals(new EditContactCommand(bob, charlie)));
    }

    @Test
    public void toStringMethod() {
        Name targetName = new Name("Alice");
        Name newName = new Name("Bob");
        EditContactCommand command = new EditContactCommand(targetName, newName);
        String expected = EditContactCommand.class.getCanonicalName()
                + "{targetName=" + targetName + ", newName=" + newName + "}";
        assertEquals(expected, command.toString());
    }
}
