package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
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
    public void execute_deleteGame_returnsConfirmation() throws Exception {
        Person firstPerson = model.getFilteredPersonList().get(0);
        Game gameToProcess = new Game("Minecraft");

        new AddGameCommand(null, firstPerson.getName(), gameToProcess, false).execute(model);
        Person firstPersonAfterSetup = model.getFilteredPersonList().get(0);

        DeleteGameCommand deleteGameCommand =
                new DeleteGameCommand(null, firstPerson.getName(), gameToProcess, false);

        CommandResult result = deleteGameCommand.execute(model);

        assertTrue(result.isAwaitingConfirmation());
        assertEquals(firstPersonAfterSetup, result.getPendingPerson());
    }

    @Test
    public void performDeletion_deleteGame_success() throws Exception {
        Person firstPerson = model.getFilteredPersonList().get(0);
        Game gameToProcess = new Game("Minecraft");

        new AddGameCommand(null, firstPerson.getName(), gameToProcess, false).execute(model);

        DeleteGameCommand deleteGameCommand =
                new DeleteGameCommand(null, firstPerson.getName(), gameToProcess, false);

        deleteGameCommand.execute(model);

        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Person editedPerson = expectedModel.getFilteredPersonList().get(0);

        String expectedMessage = String.format(DeleteGameCommand.MESSAGE_SUCCESS,
                gameToProcess.gameName, firstPerson.getName().fullName);

        CommandResult result = deleteGameCommand.performDeletion(model);

        assertEquals(expectedMessage, result.getFeedbackToUser());
        assertEquals(editedPerson, result.getViewedPerson());
        assertEquals(expectedModel, model);
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
    public void execute_deleteGameByIndex_returnsConfirmation() throws Exception {
        Game gameToProcess = new Game("Minecraft");

        new AddGameCommand(INDEX_FIRST_PERSON, null, gameToProcess, false).execute(model);
        Person firstPersonAfterSetup = model.getFilteredPersonList().get(0);

        DeleteGameCommand deleteGameCommand =
                new DeleteGameCommand(INDEX_FIRST_PERSON, null, gameToProcess, false);

        CommandResult result = deleteGameCommand.execute(model);

        assertTrue(result.isAwaitingConfirmation());
        assertEquals(firstPersonAfterSetup, result.getPendingPerson());
    }

    @Test
    public void execute_invalidIndex_failure() {
        seedu.address.commons.core.index.Index outOfBoundIndex =
                seedu.address.commons.core.index.Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        Game gameToDelete = new Game("Minecraft");
        DeleteGameCommand deleteGameCommand = new DeleteGameCommand(outOfBoundIndex, null, gameToDelete, false);

        assertCommandFailure(deleteGameCommand, model,
                seedu.address.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_useUserProfile_returnsConfirmation() throws Exception {
        Person userProfile = new Person(new seedu.address.model.person.Name("John Doe"),
                new HashSet<>(), new HashSet<>(), true);
        AddressBook ab = new AddressBook();
        ab.addPerson(userProfile);
        Model profileModel = new ModelManager(ab, new UserPrefs());

        Game gameToAdd = new Game("Valorant");
        new AddGameCommand(null, null, gameToAdd, true).execute(profileModel);

        DeleteGameCommand deleteGameCommand = new DeleteGameCommand(null, null, gameToAdd, true);
        CommandResult result = deleteGameCommand.execute(profileModel);

        assertTrue(result.isAwaitingConfirmation());
        assertEquals(profileModel.getUserProfile().get(), result.getPendingPerson());
    }

    @Test
    public void execute_noProfile_failure() {
        Model emptyModel = new ModelManager(new AddressBook(), new UserPrefs());
        Game gameToDelete = new Game("Valorant");
        DeleteGameCommand deleteGameCommand = new DeleteGameCommand(null, null, gameToDelete, true);

        assertCommandFailure(deleteGameCommand, emptyModel, "No user profile found.");
    }

    @Test
    public void execute_caseInsensitiveName_returnsConfirmation() throws Exception {
        Person firstPerson = model.getFilteredPersonList().get(0);
        Game gameToProcess = new Game("Minecraft");

        new AddGameCommand(null, firstPerson.getName(), gameToProcess, false).execute(model);

        Name lowerCaseName = new Name(firstPerson.getName().fullName.toLowerCase());
        DeleteGameCommand deleteGameCommand = new DeleteGameCommand(null, lowerCaseName, gameToProcess, false);

        CommandResult result = deleteGameCommand.execute(model);
        assertTrue(result.isAwaitingConfirmation());
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
        deleteGameCommand.performDeletion(model);
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

        assertTrue(deleteGameByIndex.equals(deleteGameByIndex));

        DeleteGameCommand deleteGameByIndexCopy = new DeleteGameCommand(INDEX_FIRST_PERSON, null, gameA, false);
        assertTrue(deleteGameByIndex.equals(deleteGameByIndexCopy));

        assertFalse(deleteGameByIndex.equals(1));
        assertFalse(deleteGameByIndex.equals(null));

        DeleteGameCommand deleteDiffGame = new DeleteGameCommand(INDEX_FIRST_PERSON, null, gameB, false);
        assertFalse(deleteGameByIndex.equals(deleteDiffGame));

        assertFalse(deleteGameByIndex.equals(deleteGameByName));

        DeleteGameCommand deleteGameWithProfile = new DeleteGameCommand(INDEX_FIRST_PERSON, null, gameA, true);
        assertFalse(deleteGameByIndex.equals(deleteGameWithProfile));

        DeleteGameCommand deleteGameByNameCopy = new DeleteGameCommand(null, nameA, gameA, false);
        assertTrue(deleteGameByName.equals(deleteGameByNameCopy));
    }
}
