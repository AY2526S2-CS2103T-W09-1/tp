package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
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
                firstPerson.getName(), firstPerson.getTags(), expectedGames);

        AddGameCommand addGameCommand = new AddGameCommand(null, firstPerson.getName(), gameToAdd);

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
        AddGameCommand addGameCommand = new AddGameCommand(null, firstPerson.getName(), gameToAdd);

        // Add the game once successfully
        addGameCommand.execute(model);

        // Executing the exact same command again should trigger the duplicate error
        assertCommandFailure(addGameCommand, model, AddGameCommand.MESSAGE_DUPLICATE_GAME);
    }

    @Test
    public void execute_contactNotFound_failure() {
        Name notInModelName = new Name("Unknown Person Name");
        Game gameToAdd = new Game("Minecraft");
        AddGameCommand addGameCommand = new AddGameCommand(null, notInModelName, gameToAdd);

        assertCommandFailure(addGameCommand, model, AddGameCommand.MESSAGE_CONTACT_NOT_FOUND);
    }

    @Test
    public void execute_caseInsensitiveName_success() {
        Person firstPerson = model.getFilteredPersonList().get(0);
        Game gameToAdd = new Game("Minecraft");

        // Use all-lowercase version of the stored name
        Name lowerCaseName = new Name(firstPerson.getName().fullName.toLowerCase());
        AddGameCommand addGameCommand = new AddGameCommand(null, lowerCaseName, gameToAdd);

        Set<Game> expectedGames = new HashSet<>(firstPerson.getGames());
        expectedGames.add(gameToAdd);
        Person editedPerson = new Person(firstPerson.getName(), firstPerson.getTags(), expectedGames);

        String expectedMessage = String.format(AddGameCommand.MESSAGE_SUCCESS,
                gameToAdd.gameName,
                firstPerson.getName().fullName);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);

        assertCommandSuccess(addGameCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_addGameByIndex_success() {
        Person firstPerson = model.getFilteredPersonList().get(0);
        Game gameToAdd = new Game("Minecraft");

        // Testing the Index path (Name is null)
        AddGameCommand addGameCommand = new AddGameCommand(INDEX_FIRST_PERSON, null, gameToAdd);

        Set<Game> expectedGames = new HashSet<>(firstPerson.getGames());
        expectedGames.add(gameToAdd);
        Person editedPerson = new Person(firstPerson.getName(), firstPerson.getTags(), expectedGames);

        String expectedMessage = String.format(AddGameCommand.MESSAGE_SUCCESS,
                gameToAdd.gameName,
                firstPerson.getName().fullName);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);

        assertCommandSuccess(addGameCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndex_failure() {
        // Create an index that is larger than the size of the list
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        Game gameToAdd = new Game("Minecraft");
        AddGameCommand addGameCommand = new AddGameCommand(outOfBoundIndex, null, gameToAdd);

        assertCommandFailure(addGameCommand,
                model,
                seedu.address.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        Game gameA = new Game("Minecraft");
        Game gameB = new Game("Valorant");
        Name nameA = new Name("Alice");

        AddGameCommand addGameByIndex = new AddGameCommand(INDEX_FIRST_PERSON, null, gameA);
        AddGameCommand addGameByName = new AddGameCommand(null, nameA, gameA);

        // same object -> returns true
        org.junit.jupiter.api.Assertions.assertTrue(addGameByIndex.equals(addGameByIndex));

        // same values -> returns true
        AddGameCommand addGameByIndexCopy = new AddGameCommand(INDEX_FIRST_PERSON, null, gameA);
        org.junit.jupiter.api.Assertions.assertTrue(addGameByIndex.equals(addGameByIndexCopy));

        // different types -> returns false
        org.junit.jupiter.api.Assertions.assertFalse(addGameByIndex.equals(1));

        // null -> returns false
        org.junit.jupiter.api.Assertions.assertFalse(addGameByIndex.equals(null));

        // different game -> returns false
        AddGameCommand addDiffGame = new AddGameCommand(INDEX_FIRST_PERSON, null, gameB);
        org.junit.jupiter.api.Assertions.assertFalse(addGameByIndex.equals(addDiffGame));

        // different target types (index vs name) -> returns false
        org.junit.jupiter.api.Assertions.assertFalse(addGameByIndex.equals(addGameByName));
    }
}
