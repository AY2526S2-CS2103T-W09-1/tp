package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
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

        AddGameCommand addGameCommand = new AddGameCommand(null,
                firstPerson.getName(),
                gameToAdd,
                false);

        String expectedMessage = String.format(AddGameCommand.MESSAGE_SUCCESS,
                gameToAdd.gameName,
                editedPerson.getName().fullName);

        CommandResult expectedCommandResult = new CommandResult(expectedMessage,
                false,
                false,
                editedPerson);
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);

        assertCommandSuccess(addGameCommand, model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_duplicateGame_failure() throws Exception {
        Person firstPerson = model.getFilteredPersonList().get(0);
        Game gameToAdd = new Game("Minecraft");
        AddGameCommand addGameCommand = new AddGameCommand(null, firstPerson.getName(), gameToAdd, false);

        // Add the game once successfully
        addGameCommand.execute(model);

        // Executing the exact same command again should trigger the duplicate error
        assertCommandFailure(addGameCommand, model, AddGameCommand.MESSAGE_DUPLICATE_GAME);
    }

    @Test
    public void execute_contactNotFound_failure() {
        Name notInModelName = new Name("Unknown Person Name");
        Game gameToAdd = new Game("Minecraft");
        AddGameCommand addGameCommand = new AddGameCommand(null, notInModelName, gameToAdd, false);

        assertCommandFailure(addGameCommand, model, AddGameCommand.MESSAGE_CONTACT_NOT_FOUND);
    }

    @Test
    public void execute_caseInsensitiveName_success() {
        Person firstPerson = model.getFilteredPersonList().get(0);
        Game gameToAdd = new Game("Minecraft");

        // Use all-lowercase version of the stored name
        Name lowerCaseName = new Name(firstPerson.getName().fullName.toLowerCase());
        AddGameCommand addGameCommand = new AddGameCommand(null, lowerCaseName, gameToAdd, false);

        Set<Game> expectedGames = new HashSet<>(firstPerson.getGames());
        expectedGames.add(gameToAdd);
        Person editedPerson = new Person(firstPerson.getName(), firstPerson.getTags(), expectedGames);

        String expectedMessage = String.format(AddGameCommand.MESSAGE_SUCCESS,
                gameToAdd.gameName,
                firstPerson.getName().fullName);

        CommandResult expectedCommandResult = new CommandResult(expectedMessage,
                false,
                false,
                editedPerson);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);

        assertCommandSuccess(addGameCommand, model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_addGameByIndex_success() {
        Person firstPerson = model.getFilteredPersonList().get(0);
        Game gameToAdd = new Game("Minecraft");

        // Testing the Index path (Name is null)
        AddGameCommand addGameCommand = new AddGameCommand(INDEX_FIRST_PERSON, null, gameToAdd, false);

        Set<Game> expectedGames = new HashSet<>(firstPerson.getGames());
        expectedGames.add(gameToAdd);
        Person editedPerson = new Person(firstPerson.getName(), firstPerson.getTags(), expectedGames);

        String expectedMessage = String.format(AddGameCommand.MESSAGE_SUCCESS,
                gameToAdd.gameName,
                firstPerson.getName().fullName);

        CommandResult expectedCommandResult = new CommandResult(expectedMessage, false, false, editedPerson);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);

        assertCommandSuccess(addGameCommand, model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_invalidIndex_failure() {
        // Create an index that is larger than the size of the list
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        Game gameToAdd = new Game("Minecraft");
        AddGameCommand addGameCommand = new AddGameCommand(outOfBoundIndex, null, gameToAdd, false);

        assertCommandFailure(addGameCommand,
                model,
                seedu.address.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_useUserProfile_success() throws Exception {
        Person userProfile = new Person(new Name("John Doe"), new java.util.HashSet<>(), new java.util.HashSet<>(),
                true);
        AddressBook ab = new AddressBook();
        ab.addPerson(userProfile);
        Model profileModel = new ModelManager(ab, new UserPrefs());

        Game gameToAdd = new Game("Minecraft");
        AddGameCommand addGameCommand = new AddGameCommand(null, null, gameToAdd, true);
        String expectedMessage = String.format(AddGameCommand.MESSAGE_SUCCESS, gameToAdd.gameName, "John Doe");

        CommandResult result = addGameCommand.execute(profileModel);
        org.junit.jupiter.api.Assertions.assertEquals(expectedMessage, result.getFeedbackToUser());
        org.junit.jupiter.api.Assertions.assertTrue(profileModel.getUserProfile().isPresent());
        org.junit.jupiter.api.Assertions.assertTrue(
                profileModel.getUserProfile().get().getGames().contains(gameToAdd));
    }

    @Test
    public void execute_noProfile_failure() {
        Model emptyModel = new ModelManager(new AddressBook(), new UserPrefs());
        Game gameToAdd = new Game("Minecraft");
        AddGameCommand addGameCommand = new AddGameCommand(null, null, gameToAdd, true);
        assertCommandFailure(addGameCommand, emptyModel, "No user profile found.");
    }

    @Test
    public void execute_nullIndexAndName_failure() {
        Game gameToAdd = new Game("Minecraft");
        AddGameCommand addGameCommand = new AddGameCommand(null, null, gameToAdd, false);
        assertCommandFailure(addGameCommand, model, AddGameCommand.MESSAGE_CONTACT_NOT_FOUND);
    }

    @Test
    public void undo_addGame_gameRemoved() throws Exception {
        Person firstPerson = model.getFilteredPersonList().get(0);
        Game gameToAdd = new Game("Minecraft");
        AddGameCommand addGameCommand = new AddGameCommand(null, firstPerson.getName(), gameToAdd, false);

        addGameCommand.execute(model);
        addGameCommand.undo(model);

        Assertions.assertFalse(model.getFilteredPersonList().get(0).getGames().contains(gameToAdd));
    }

    @Test
    public void equals() {
        Game gameA = new Game("Minecraft");
        Game gameB = new Game("Valorant");
        Name nameA = new Name("Alice");

        AddGameCommand addGameByIndex = new AddGameCommand(INDEX_FIRST_PERSON, null, gameA, false);
        AddGameCommand addGameByName = new AddGameCommand(null, nameA, gameA, false);

        // same object -> returns true
        Assertions.assertTrue(addGameByIndex.equals(addGameByIndex));

        // same values -> returns true
        AddGameCommand addGameByIndexCopy = new AddGameCommand(INDEX_FIRST_PERSON, null, gameA, false);
        Assertions.assertTrue(addGameByIndex.equals(addGameByIndexCopy));

        // different types -> returns false
        Assertions.assertFalse(addGameByIndex.equals(1));

        // null -> returns false
        Assertions.assertFalse(addGameByIndex.equals(null));

        // different game -> returns false
        AddGameCommand addDiffGame = new AddGameCommand(INDEX_FIRST_PERSON, null, gameB, false);
        Assertions.assertFalse(addGameByIndex.equals(addDiffGame));

        // different target types (index vs name) -> returns false
        Assertions.assertFalse(addGameByIndex.equals(addGameByName));

        // different useUserProfile -> returns false
        AddGameCommand addGameWithProfile = new AddGameCommand(INDEX_FIRST_PERSON, null, gameA, true);
        Assertions.assertFalse(addGameByIndex.equals(addGameWithProfile));

        // same values by name -> returns true
        AddGameCommand addGameByNameCopy = new AddGameCommand(null, nameA, gameA, false);
        Assertions.assertTrue(addGameByName.equals(addGameByNameCopy));
    }
}
