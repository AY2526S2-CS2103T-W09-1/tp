package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.HashSet;

import org.junit.jupiter.api.Test;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;

public class ClearCommandTest {

    @Test
    public void execute_emptyAddressBook_success() {
        Model model = new ModelManager();
        Model expectedModel = new ModelManager();

        assertCommandSuccess(new ClearCommand(), model, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_nonEmptyAddressBook_success() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel.setAddressBook(new AddressBook());

        assertCommandSuccess(new ClearCommand(), model, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_addressBookWithUserProfile_preservesUserProfile() {
        Person userProfile = new Person(new Name("John Doe"), new HashSet<>(), new HashSet<>(), true);
        AddressBook ab = new AddressBook();
        ab.addPerson(userProfile);
        Model model = new ModelManager(ab, new UserPrefs());

        Model expectedModel = new ModelManager(new AddressBook(), new UserPrefs());
        expectedModel.addPerson(userProfile);

        assertCommandSuccess(new ClearCommand(), model, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void undo_clearCommand_addressBookRestored() throws Exception {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        AddressBook originalAddressBook = new AddressBook(model.getAddressBook());

        ClearCommand clearCommand = new ClearCommand();
        clearCommand.execute(model);
        clearCommand.undo(model);

        assertEquals(originalAddressBook, model.getAddressBook());
    }

}
