package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.HashSet;

import org.junit.jupiter.api.Test;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.game.Game;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;

public class DeleteGameCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_deleteGame_success() throws Exception {
        Person firstPerson = model.getFilteredPersonList().get(0);
        Game gameToProcess = new Game("Minecraft");

        // Setup: Add the game to the model first so we have something to delete
        new AddGameCommand(null, firstPerson.getName(), gameToProcess, false).execute(model);

        // Now prepare for deletion
        DeleteGameCommand deleteGameCommand =
                new DeleteGameCommand(null, firstPerson.getName(), gameToProcess, false);

        // The expected model is exactly the original clean model (since adding and deleting leaves it unchanged)
        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        Person editedPerson = expectedModel.getFilteredPersonList().get(0);

        String expectedMessage = String.format(DeleteGameCommand.MESSAGE_SUCCESS,
                gameToProcess.gameName,
                firstPerson.getName().fullName);

        CommandResult expectedCommandResult = new CommandResult(expectedMessage, false, false, editedPerson);

        assertCommandSuccess(deleteGameCommand, model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_gameNotFound_failure() {
        Person firstPerson = model.getFilteredPersonList().get(0);
        Game gameToDelete = new Game("NonExistentGame");
        DeleteGameCommand deleteGameCommand =
                new DeleteGameCommand(null, firstPerson.getName(), gameToDelete, false);

        assertCommandFailure(deleteGameCommand, model, DeleteGameCommand.MESSAGE_GAME_NOT_FOUND);
    }

    @Test
    public void execute_contactNotFound_failure() {
        Name notInModelName = new Name("Unknown Person Name");
        Game gameToDelete = new Game("Minecraft");
        DeleteGameCommand deleteGameCommand = new DeleteGameCommand(null, notInModelName, gameToDelete, false);

        assertCommandFailure(deleteGameCommand, model, DeleteGameCommand.MESSAGE_CONTACT_NOT_FOUND);
    }

    @Test
    public void execute_deleteGameByIndex_success() throws Exception {
        Person firstPerson = model.getFilteredPersonList().get(1);
        Game gameToProcess = new Game("Minecraft");

        new AddGameCommand(INDEX_FIRST_PERSON, null, gameToProcess, false).execute(model);

        DeleteGameCommand deleteGameCommand =
                new DeleteGameCommand(INDEX_FIRST_PERSON, null, gameToProcess, false);

        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        Person editedPerson = expectedModel.getFilteredPersonList().get(1);

        String expectedMessage = String.format(DeleteGameCommand.MESSAGE_SUCCESS,
                gameToProcess.gameName,
                firstPerson.getName().fullName);

        CommandResult expectedCommandResult = new CommandResult(expectedMessage, false, false, editedPerson);

        assertCommandSuccess(deleteGameCommand, model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_invalidIndex_failure() {
        seedu.address.commons.core.index.Index outOfBoundIndex =
                seedu.address.commons.core.index.Index.fromZeroBased(model.getFilteredPersonList().size() + 1);
        Game gameToDelete = new Game("Minecraft");
        DeleteGameCommand deleteGameCommand = new DeleteGameCommand(outOfBoundIndex, null, gameToDelete, false);

        assertCommandFailure(deleteGameCommand, model,
                seedu.address.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_useUserProfile_success() throws Exception {
        Person userProfile = new Person(new seedu.address.model.person.Name("John Doe"),
                new HashSet<>(), true);
        AddressBook ab = new AddressBook();
        ab.addPerson(userProfile);
        Model profileModel = new ModelManager(ab, new UserPrefs());

        Game gameToAdd = new Game("Valorant");
        new AddGameCommand(null, null, gameToAdd, true).execute(profileModel);

        DeleteGameCommand deleteGameCommand = new DeleteGameCommand(null, null, gameToAdd, true);
        String expectedMessage = String.format(DeleteGameCommand.MESSAGE_SUCCESS,
                gameToAdd.gameName, "John Doe");

        CommandResult result = deleteGameCommand.execute(profileModel);
        assertEquals(expectedMessage, result.getFeedbackToUser());
        assertTrue(profileModel.getUserProfile().isPresent());
        assertTrue(profileModel.getUserProfile().get().getGames().isEmpty());
        assertEquals(profileModel.getUserProfile().get(), result.getViewedPerson());
    }

    @Test
    public void execute_noProfile_failure() {
        Model emptyModel = new ModelManager(new AddressBook(), new UserPrefs());
        Game gameToDelete = new Game("Valorant");
        DeleteGameCommand deleteGameCommand = new DeleteGameCommand(null, null, gameToDelete, true);
        emptyModel.deletePerson(emptyModel.getFilteredPersonList().get(0));

        assertCommandFailure(deleteGameCommand, emptyModel, "No user profile found.");
    }

    @Test
    public void execute_caseInsensitiveName_success() throws Exception {
        Person firstPerson = model.getFilteredPersonList().get(0);
        Game gameToProcess = new Game("Minecraft");

        // Setup: add game using exact name
        new AddGameCommand(null, firstPerson.getName(), gameToProcess, false).execute(model);

        // Use all-lowercase version of the stored name
        Name lowerCaseName = new Name(firstPerson.getName().fullName.toLowerCase());
        DeleteGameCommand deleteGameCommand = new DeleteGameCommand(null, lowerCaseName, gameToProcess, false);

        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        Person editedPerson = expectedModel.getFilteredPersonList().get(0);

        String expectedMessage = String.format(DeleteGameCommand.MESSAGE_SUCCESS,
                gameToProcess.gameName,
                firstPerson.getName().fullName);

        CommandResult expectedCommandResult = new CommandResult(expectedMessage, false, false, editedPerson);

        assertCommandSuccess(deleteGameCommand, model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_nullIndexAndName_failure() {
        Game gameToDelete = new Game("Minecraft");
        DeleteGameCommand deleteGameCommand = new DeleteGameCommand(null, null, gameToDelete, false);
        assertCommandFailure(deleteGameCommand, model, DeleteGameCommand.MESSAGE_CONTACT_NOT_FOUND);
    }

    @Test
    public void undo_deleteGame_gameRestored() throws Exception {
        Person firstPerson = model.getFilteredPersonList().get(0);
        Game gameToProcess = new Game("Minecraft");

        new AddGameCommand(null, firstPerson.getName(), gameToProcess, false).execute(model);
        DeleteGameCommand deleteGameCommand =
                new DeleteGameCommand(null, firstPerson.getName(), gameToProcess, false);
        deleteGameCommand.execute(model);
        deleteGameCommand.undo(model);

        assertTrue(model.getFilteredPersonList().get(0).getGames().contains(gameToProcess));
    }

    @Test
    public void equals() {
        Game gameA = new Game("Minecraft");
        Game gameB = new Game("Valorant");
        Name nameA = new Name("Alice");

        DeleteGameCommand deleteGameByIndex = new DeleteGameCommand(INDEX_FIRST_PERSON, null, gameA, false);
        DeleteGameCommand deleteGameByName = new DeleteGameCommand(null, nameA, gameA, false);

        // same object -> returns true
        assertTrue(deleteGameByIndex.equals(deleteGameByIndex));

        // same values -> returns true
        DeleteGameCommand deleteGameByIndexCopy = new DeleteGameCommand(INDEX_FIRST_PERSON, null, gameA, false);
        assertTrue(deleteGameByIndex.equals(deleteGameByIndexCopy));

        // different types -> returns false
        assertFalse(deleteGameByIndex.equals(1));

        // null -> returns false
        assertFalse(deleteGameByIndex.equals(null));

        // different game -> returns false
        DeleteGameCommand deleteDiffGame = new DeleteGameCommand(INDEX_FIRST_PERSON, null, gameB, false);
        assertFalse(deleteGameByIndex.equals(deleteDiffGame));

        // different target types (index vs name) -> returns false
        assertFalse(deleteGameByIndex.equals(deleteGameByName));

        // different useUserProfile -> returns false
        DeleteGameCommand deleteGameWithProfile = new DeleteGameCommand(INDEX_FIRST_PERSON, null, gameA, true);
        assertFalse(deleteGameByIndex.equals(deleteGameWithProfile));

        // same values by name -> returns true
        DeleteGameCommand deleteGameByNameCopy = new DeleteGameCommand(null, nameA, gameA, false);
        assertTrue(deleteGameByName.equals(deleteGameByNameCopy));
    }
}
