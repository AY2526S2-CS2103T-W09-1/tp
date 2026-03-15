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

public class ListGameCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_listGames_success() throws Exception {
        Person firstPerson = model.getFilteredPersonList().get(0);
        Game gameToAdd = new Game("Minecraft");

        // Setup: Add one game
        new AddGameCommand(firstPerson.getName(), gameToAdd).execute(model);

        ListGameCommand listGameCommand = new ListGameCommand(firstPerson.getName());

        // Expected model doesn't change during a list command (it's read-only)
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        String expectedMessage = String.format(ListGameCommand.MESSAGE_SUCCESS,
                firstPerson.getName().fullName,
                "Minecraft");

        assertCommandSuccess(listGameCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noGames_success() {
        // ALICE initially has no games in the sample data
        Person firstPerson = model.getFilteredPersonList().get(0);
        ListGameCommand listGameCommand = new ListGameCommand(firstPerson.getName());

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        String expectedMessage = String.format(ListGameCommand.MESSAGE_NO_GAMES, firstPerson.getName().fullName);

        assertCommandSuccess(listGameCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_contactNotFound_failure() {
        Name notInModelName = new Name("Unknown Person Name");
        ListGameCommand listGameCommand = new ListGameCommand(notInModelName);

        assertCommandFailure(listGameCommand, model, ListGameCommand.MESSAGE_CONTACT_NOT_FOUND);
    }
}
