package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.game.Game;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;

public class AddGameCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_addGame_success() {
        // We use the first person in the typical address book (Usually Alice)
        Person firstPerson = model.getFilteredPersonList().get(0);
        Game gameToAdd = new Game("Minecraft");

        Set<Game> expectedGames = new HashSet<>(firstPerson.getGames());
        expectedGames.add(gameToAdd);

        Person editedPerson = new Person(
                firstPerson.getName(), firstPerson.getPhone(), firstPerson.getEmail(),
                firstPerson.getAddress(), firstPerson.getTags(), expectedGames);

        AddGameCommand addGameCommand = new AddGameCommand(firstPerson.getName(), gameToAdd);

        String expectedMessage = String.format(AddGameCommand.MESSAGE_SUCCESS,
                gameToAdd.gameName,
                editedPerson.getName().fullName);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);

        assertCommandSuccess(addGameCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateGame_failure() throws Exception {
        Person firstPerson = model.getFilteredPersonList().get(0);
        Game gameToAdd = new Game("Minecraft");
        AddGameCommand addGameCommand = new AddGameCommand(firstPerson.getName(), gameToAdd);

        // Add the game once successfully
        addGameCommand.execute(model);

        // Executing the exact same command again should trigger the duplicate error
        assertCommandFailure(addGameCommand, model, AddGameCommand.MESSAGE_DUPLICATE_GAME);
    }

    @Test
    public void execute_contactNotFound_failure() {
        Name notInModelName = new Name("Unknown Person Name");
        Game gameToAdd = new Game("Minecraft");
        AddGameCommand addGameCommand = new AddGameCommand(notInModelName, gameToAdd);

        assertCommandFailure(addGameCommand, model, AddGameCommand.MESSAGE_CONTACT_NOT_FOUND);
    }
}
