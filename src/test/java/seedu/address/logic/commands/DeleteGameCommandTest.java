package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

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
        new AddGameCommand(null, firstPerson.getName(), gameToProcess).execute(model);

        // Now prepare for deletion
        DeleteGameCommand deleteGameCommand =
                new DeleteGameCommand(null, firstPerson.getName(), gameToProcess);

        // The expected model is exactly the original clean model (since adding and deleting leaves it unchanged)
        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        String expectedMessage = String.format(DeleteGameCommand.MESSAGE_SUCCESS,
                gameToProcess.gameName,
                firstPerson.getName().fullName);

        assertCommandSuccess(deleteGameCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_gameNotFound_failure() {
        Person firstPerson = model.getFilteredPersonList().get(0);
        Game gameToDelete = new Game("NonExistentGame");
        DeleteGameCommand deleteGameCommand =
                new DeleteGameCommand(null, firstPerson.getName(), gameToDelete);

        assertCommandFailure(deleteGameCommand, model, DeleteGameCommand.MESSAGE_GAME_NOT_FOUND);
    }

    @Test
    public void execute_contactNotFound_failure() {
        Name notInModelName = new Name("Unknown Person Name");
        Game gameToDelete = new Game("Minecraft");
        DeleteGameCommand deleteGameCommand = new DeleteGameCommand(null, notInModelName, gameToDelete);

        assertCommandFailure(deleteGameCommand, model, DeleteGameCommand.MESSAGE_CONTACT_NOT_FOUND);
    }

    @Test
    public void execute_caseInsensitiveName_success() throws Exception {
        Person firstPerson = model.getFilteredPersonList().get(0);
        Game gameToProcess = new Game("Minecraft");

        // Setup: add game using exact name
        new AddGameCommand(null, firstPerson.getName(), gameToProcess).execute(model);

        // Use all-lowercase version of the stored name
        Name lowerCaseName = new Name(firstPerson.getName().fullName.toLowerCase());
        DeleteGameCommand deleteGameCommand = new DeleteGameCommand(null, lowerCaseName, gameToProcess);

        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        String expectedMessage = String.format(DeleteGameCommand.MESSAGE_SUCCESS,
                gameToProcess.gameName,
                firstPerson.getName().fullName);

        assertCommandSuccess(deleteGameCommand, model, expectedMessage, expectedModel);
    }
}
