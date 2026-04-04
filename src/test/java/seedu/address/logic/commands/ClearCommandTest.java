package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
    public void execute_returnsConfirmationPrompt() {
        Model model = new ModelManager();
        CommandResult result = new ClearCommand().execute(model);
        assertTrue(result.isAwaitingClearConfirmation());
        assertEquals(ClearCommand.MESSAGE_CONFIRMATION, result.getFeedbackToUser());
    }

    @Test
    public void executeConfirmed_emptyAddressBook_success() {
        Model model = new ModelManager();
        ClearCommand clearCommand = new ClearCommand();
        clearCommand.execute(model);
        CommandResult result = clearCommand.executeConfirmed(model);
        assertEquals(ClearCommand.MESSAGE_SUCCESS, result.getFeedbackToUser());
    }

    @Test
    public void executeConfirmed_nonEmptyAddressBook_success() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Person userProfile = model.getFilteredPersonList().get(0);
        AddressBook ab = new AddressBook();
        ab.addPerson(userProfile);
        Model expectedModel = new ModelManager(ab, new UserPrefs());

        ClearCommand clearCommand = new ClearCommand();
        clearCommand.execute(model);
        clearCommand.executeConfirmed(model);

        assertEquals(expectedModel, model);
    }

    @Test
    public void executeConfirmed_addressBookWithUserProfile_preservesUserProfile() {
        Person userProfile = new Person(new Name("John Doe"), new HashSet<>(), false);
        AddressBook ab = new AddressBook();
        ab.addPerson(userProfile);
        Model model = new ModelManager(ab, new UserPrefs());

        ClearCommand clearCommand = new ClearCommand();
        clearCommand.execute(model);
        clearCommand.executeConfirmed(model);

        assertTrue(model.getFilteredPersonList().get(0).isUserProfile());
    }

    @Test
    public void undo_clearCommand_addressBookRestored() throws Exception {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        AddressBook originalAddressBook = new AddressBook(model.getAddressBook());

        ClearCommand clearCommand = new ClearCommand();
        clearCommand.execute(model);
        clearCommand.executeConfirmed(model);
        clearCommand.undo(model);

        assertEquals(originalAddressBook, model.getAddressBook());
    }
}
