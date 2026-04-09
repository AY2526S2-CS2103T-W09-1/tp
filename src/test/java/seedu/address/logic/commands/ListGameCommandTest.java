package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.HashSet;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.game.Game;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;

public class ListGameCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_listGamesByName_success() throws Exception {
        Person firstPerson = model.getFilteredPersonList().get(0);
        Game gameToAdd = new Game("Minecraft");

        // Setup: Add one game
        new AddGameCommand(null, firstPerson.getName(), gameToAdd, false).execute(model);

        ListGameCommand listGameCommand = new ListGameCommand(null, firstPerson.getName(), false);

        // Expected model doesn't change during a list command
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        String expectedMessage = String.format(ListGameCommand.MESSAGE_SUCCESS,
                firstPerson.getName().fullName,
                "Minecraft");

        assertCommandSuccess(listGameCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noGames_success() {
        Person firstPerson = model.getFilteredPersonList().get(0);
        ListGameCommand listGameCommand = new ListGameCommand(null, firstPerson.getName(), false);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        String expectedMessage = String.format(ListGameCommand.MESSAGE_NO_GAMES, firstPerson.getName().fullName);

        assertCommandSuccess(listGameCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_contactNotFoundByName_failure() {
        Name notInModelName = new Name("Unknown Person Name");
        ListGameCommand listGameCommand = new ListGameCommand(null, notInModelName, false);

        assertCommandFailure(listGameCommand, model, ListGameCommand.MESSAGE_CONTACT_NOT_FOUND);
    }

    // NEW TESTS: Index Support and Equals Method

    @Test
    public void execute_listGamesByIndex_success() throws Exception {
        Person firstPerson = model.getFilteredPersonList().get(1);
        Game gameToAdd = new Game("Minecraft");

        // Setup: Add one game
        new AddGameCommand(INDEX_FIRST_PERSON, null, gameToAdd, false).execute(model);

        // Test listing by Index (Name is null)
        ListGameCommand listGameCommand = new ListGameCommand(INDEX_FIRST_PERSON, null, false);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        String expectedMessage = String.format(ListGameCommand.MESSAGE_SUCCESS,
                firstPerson.getName().fullName,
                "Minecraft");

        assertCommandSuccess(listGameCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndex_failure() {
        Index outOfBoundIndex = Index.fromZeroBased(model.getFilteredPersonList().size() + 1);

        ListGameCommand listGameCommand = new ListGameCommand(outOfBoundIndex, null, false);

        assertCommandFailure(listGameCommand, model,
                seedu.address.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX
                        + "\n" + ListGameCommand.MESSAGE_USAGE);
    }

    @Test
    public void execute_useUserProfile_success() throws Exception {
        Person userProfile = new Person(new Name("John Doe"), new HashSet<>(), true);
        AddressBook ab = new AddressBook();
        ab.addPerson(userProfile);
        Model profileModel = new ModelManager(ab, new UserPrefs());

        Game game = new Game("Valorant");
        new AddGameCommand(null, null, game, true).execute(profileModel);

        ListGameCommand listGameCommand = new ListGameCommand(null, null, true);
        String expectedMessage = String.format(ListGameCommand.MESSAGE_SUCCESS, "John Doe", "Valorant");

        CommandResult result = listGameCommand.execute(profileModel);
        assertEquals(expectedMessage, result.getFeedbackToUser());
    }

    @Test
    public void execute_noProfile_failure() {
        Model emptyModel = new ModelManager(new AddressBook(), new UserPrefs());
        ListGameCommand listGameCommand = new ListGameCommand(null, null, true);
        emptyModel.deletePerson(emptyModel.getFilteredPersonList().get(0));

        assertCommandFailure(listGameCommand, emptyModel, "No user profile found.");
    }

    @Test
    public void execute_nullIndexAndName_failure() {
        ListGameCommand listGameCommand = new ListGameCommand(null, null, false);
        assertCommandFailure(listGameCommand, model, ListGameCommand.MESSAGE_CONTACT_NOT_FOUND);
    }

    @Test
    public void equals() {
        Name nameA = new Name("Alice");
        Name nameB = new Name("Bob");
        Index secondIndex = Index.fromZeroBased(2);

        ListGameCommand listByIndex = new ListGameCommand(INDEX_FIRST_PERSON, null, false);
        ListGameCommand listByName = new ListGameCommand(null, nameA, false);

        // same object -> returns true
        org.junit.jupiter.api.Assertions.assertTrue(listByIndex.equals(listByIndex));

        // same values -> returns true
        ListGameCommand listByIndexCopy = new ListGameCommand(INDEX_FIRST_PERSON, null, false);
        org.junit.jupiter.api.Assertions.assertTrue(listByIndex.equals(listByIndexCopy));

        // different types -> returns false
        org.junit.jupiter.api.Assertions.assertFalse(listByIndex.equals(1));

        // null -> returns false
        org.junit.jupiter.api.Assertions.assertFalse(listByIndex.equals(null));

        // different target types (index vs name) -> returns false
        org.junit.jupiter.api.Assertions.assertFalse(listByIndex.equals(listByName));

        // different index -> returns false
        ListGameCommand listByDiffIndex = new ListGameCommand(secondIndex, null, false);
        org.junit.jupiter.api.Assertions.assertFalse(listByIndex.equals(listByDiffIndex));

        // different name -> returns false
        ListGameCommand listByDiffName = new ListGameCommand(null, nameB, false);
        org.junit.jupiter.api.Assertions.assertFalse(listByName.equals(listByDiffName));
    }
}
