package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.HashSet;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;

public class ViewProfileCommandTest {

    private Model typicalModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_profileExists_success() throws Exception {
        Person userProfile = new Person(new Name("John Doe"), new HashSet<>(), new HashSet<>(), true);
        AddressBook ab = new AddressBook();
        ab.addPerson(userProfile);
        Model model = new ModelManager(ab, new UserPrefs());

        CommandResult result = new ViewProfileCommand().execute(model);

        assertEquals(ViewProfileCommand.MESSAGE_SUCCESS_SELF, result.getFeedbackToUser());
        assertEquals(userProfile, result.getViewedPerson());
    }

    @Test
    public void execute_noProfile_throwsCommandException() {
        Model model = new ModelManager(new AddressBook(), new UserPrefs());

        assertThrows(CommandException.class,
                ViewProfileCommand.MESSAGE_NO_PROFILE, () -> new ViewProfileCommand().execute(model));
    }
    @Test
    public void execute_viewByIndex_success() {
        Person personToView = typicalModel.getFilteredPersonList().get(0);
        ViewProfileCommand viewCommand = new ViewProfileCommand(INDEX_FIRST_PERSON, null);

        String expectedMessage = String.format(ViewProfileCommand.MESSAGE_SUCCESS_CONTACT, personToView.getName().fullName);
        CommandResult expectedCommandResult = new CommandResult(expectedMessage, false, false, personToView);
        Model expectedModel = new ModelManager(typicalModel.getAddressBook(), new UserPrefs());

        assertCommandSuccess(viewCommand, typicalModel, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_contactNotFoundByName_failure() {
        Name notInModelName = new Name("Unknown Person Name");
        ViewProfileCommand viewCommand = new ViewProfileCommand(null, notInModelName);

        assertCommandFailure(viewCommand, typicalModel, ViewProfileCommand.MESSAGE_PERSON_NOT_FOUND);
    }

    @Test
    public void execute_invalidIndex_failure() {
        Index outOfBoundIndex = Index.fromOneBased(typicalModel.getFilteredPersonList().size() + 1);
        ViewProfileCommand viewCommand = new ViewProfileCommand(outOfBoundIndex, null);

        assertCommandFailure(viewCommand, typicalModel, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        Name nameA = new Name("Alice");
        Name nameB = new Name("Bob");

        ViewProfileCommand viewSelf = new ViewProfileCommand();
        ViewProfileCommand viewByIndexFirst = new ViewProfileCommand(INDEX_FIRST_PERSON, null);
        ViewProfileCommand viewByIndexSecond = new ViewProfileCommand(INDEX_SECOND_PERSON, null);
        ViewProfileCommand viewByNameA = new ViewProfileCommand(null, nameA);

        // same object -> returns true
        org.junit.jupiter.api.Assertions.assertTrue(viewByIndexFirst.equals(viewByIndexFirst));

        // same values -> returns true
        ViewProfileCommand viewByIndexFirstCopy = new ViewProfileCommand(INDEX_FIRST_PERSON, null);
        org.junit.jupiter.api.Assertions.assertTrue(viewByIndexFirst.equals(viewByIndexFirstCopy));

        // different types -> returns false
        org.junit.jupiter.api.Assertions.assertFalse(viewByIndexFirst.equals(1));

        // null -> returns false
        org.junit.jupiter.api.Assertions.assertFalse(viewByIndexFirst.equals(null));

        // different targets -> returns false
        org.junit.jupiter.api.Assertions.assertFalse(viewSelf.equals(viewByIndexFirst));
        org.junit.jupiter.api.Assertions.assertFalse(viewByIndexFirst.equals(viewByNameA));
        org.junit.jupiter.api.Assertions.assertFalse(viewByIndexFirst.equals(viewByIndexSecond));
    }
}
