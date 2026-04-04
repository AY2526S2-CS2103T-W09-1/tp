package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
import seedu.address.model.person.Alias;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;

public class DeleteAliasCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_deleteAlias_success() throws Exception {
        Person firstPerson = model.getFilteredPersonList().get(0);
        Game game = new Game("Valorant");
        Alias alias = new Alias("Benjumpin");

        // Setup: add game and alias
        new AddGameCommand(null, firstPerson.getName(), game, false).execute(model);
        new AddAliasCommand(null, firstPerson.getName(), game, alias, false).execute(model);

        DeleteAliasCommand deleteAliasCommand =
                new DeleteAliasCommand(null, firstPerson.getName(), game, alias, false);

        // Expected model has the game but without the alias (same as after only adding the game)
        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        new AddGameCommand(null, firstPerson.getName(), game, false).execute(expectedModel);

        Person editedPerson = expectedModel.getFilteredPersonList().get(0);

        String expectedMessage = String.format(DeleteAliasCommand.MESSAGE_SUCCESS,
                firstPerson.getName(), game.gameName, alias);

        CommandResult expectedCommandResult = new CommandResult(expectedMessage, false, false, editedPerson);

        assertCommandSuccess(deleteAliasCommand, model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_aliasNotFound_failure() throws Exception {
        Person firstPerson = model.getFilteredPersonList().get(0);
        Game game = new Game("Valorant");
        Alias alias = new Alias("NonExistentAlias");

        new AddGameCommand(null, firstPerson.getName(), game, false).execute(model);

        DeleteAliasCommand deleteAliasCommand =
                new DeleteAliasCommand(null, firstPerson.getName(), game, alias, false);
        assertCommandFailure(deleteAliasCommand, model, DeleteAliasCommand.MESSAGE_ALIAS_NOT_FOUND);
    }

    @Test
    public void execute_gameNotFound_failure() {
        Person firstPerson = model.getFilteredPersonList().get(0);
        Game game = new Game("NonExistentGame");
        Alias alias = new Alias("SomeAlias");

        DeleteAliasCommand deleteAliasCommand =
                new DeleteAliasCommand(null, firstPerson.getName(), game, alias, false);
        assertCommandFailure(deleteAliasCommand, model, DeleteAliasCommand.MESSAGE_GAME_NOT_FOUND);
    }

    @Test
    public void execute_personNotFound_failure() {
        Name notInModelName = new Name("Unknown Person Name");
        Game game = new Game("Valorant");
        Alias alias = new Alias("SomeAlias");

        DeleteAliasCommand deleteAliasCommand = new DeleteAliasCommand(null, notInModelName, game, alias, false);
        assertCommandFailure(deleteAliasCommand, model, DeleteAliasCommand.MESSAGE_PERSON_NOT_FOUND);
    }

    @Test
    public void execute_caseInsensitiveName_success() throws Exception {
        Person firstPerson = model.getFilteredPersonList().get(0);
        Game game = new Game("Valorant");
        Alias alias = new Alias("Benjumpin");

        // Setup: add game and alias using exact name
        new AddGameCommand(null, firstPerson.getName(), game, false).execute(model);
        new AddAliasCommand(null, firstPerson.getName(), game, alias, false).execute(model);

        // Use all-lowercase version of the stored name
        Name lowerCaseName = new Name(firstPerson.getName().fullName.toLowerCase());
        DeleteAliasCommand deleteAliasCommand = new DeleteAliasCommand(null, lowerCaseName, game, alias, false);

        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        new AddGameCommand(null, firstPerson.getName(), game, false).execute(expectedModel);

        Person editedPerson = expectedModel.getFilteredPersonList().get(0);

        String expectedMessage = String.format(DeleteAliasCommand.MESSAGE_SUCCESS,
                firstPerson.getName(), game.gameName, alias);

        CommandResult expectedCommandResult = new CommandResult(expectedMessage, false, false, editedPerson);

        assertCommandSuccess(deleteAliasCommand, model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_deleteAliasByIndex_success() throws Exception {
        Person firstPerson = model.getFilteredPersonList().get(1);
        Game game = new Game("Valorant");
        Alias alias = new Alias("Benjumpin");

        // Setup: add game and alias first
        new AddGameCommand(INDEX_FIRST_PERSON, null, game, false).execute(model);
        new AddAliasCommand(INDEX_FIRST_PERSON, null, game, alias, false).execute(model);

        // Test deleting alias by Index (Name is null)
        DeleteAliasCommand deleteAliasCommand =
                new DeleteAliasCommand(INDEX_FIRST_PERSON, null, game, alias, false);

        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        new AddGameCommand(INDEX_FIRST_PERSON, null, game, false).execute(expectedModel);

        Person editedPerson = expectedModel.getFilteredPersonList().get(1);

        String expectedMessage = String.format(DeleteAliasCommand.MESSAGE_SUCCESS,
                firstPerson.getName(), game.gameName, alias);

        CommandResult expectedCommandResult = new CommandResult(expectedMessage, false, false, editedPerson);

        assertCommandSuccess(deleteAliasCommand, model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_invalidIndex_failure() {
        Index outOfBoundIndex = Index.fromZeroBased(model.getFilteredPersonList().size() + 1);
        Game game = new Game("Valorant");
        Alias alias = new Alias("SomeAlias");

        DeleteAliasCommand deleteAliasCommand = new DeleteAliasCommand(outOfBoundIndex, null, game, alias, false);

        assertCommandFailure(deleteAliasCommand,
                model,
                seedu.address.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_useUserProfile_success() throws Exception {
        Person userProfile = new Person(new Name("John Doe"), new HashSet<>(), true);
        AddressBook ab = new AddressBook();
        ab.addPerson(userProfile);
        Model profileModel = new ModelManager(ab, new UserPrefs());

        Game game = new Game("Valorant");
        Alias alias = new Alias("JohnV");
        new AddGameCommand(null, null, game, true).execute(profileModel);
        new AddAliasCommand(null, null, game, alias, true).execute(profileModel);

        DeleteAliasCommand deleteAliasCommand = new DeleteAliasCommand(null, null, game, alias, true);
        String expectedMessage = String.format(DeleteAliasCommand.MESSAGE_SUCCESS, "John Doe", game.gameName, alias);

        CommandResult result = deleteAliasCommand.execute(profileModel);
        assertEquals(expectedMessage, result.getFeedbackToUser());
        assertEquals(profileModel.getUserProfile().get(), result.getViewedPerson());
    }

    @Test
    public void execute_noProfile_failure() {
        Model emptyModel = new ModelManager(new AddressBook(), new UserPrefs());
        Game game = new Game("Valorant");
        Alias alias = new Alias("JohnV");
        emptyModel.deletePerson(emptyModel.getFilteredPersonList().get(0));

        DeleteAliasCommand deleteAliasCommand = new DeleteAliasCommand(null, null, game, alias, true);
        assertCommandFailure(deleteAliasCommand, emptyModel, "No user profile found.");
    }

    @Test
    public void execute_nullIndexAndName_failure() {
        Game game = new Game("Valorant");
        Alias alias = new Alias("SomeAlias");
        DeleteAliasCommand deleteAliasCommand = new DeleteAliasCommand(null, null, game, alias, false);
        assertCommandFailure(deleteAliasCommand, model, DeleteAliasCommand.MESSAGE_PERSON_NOT_FOUND);
    }

    @Test
    public void undo_deleteAlias_aliasRestored() throws Exception {
        Person firstPerson = model.getFilteredPersonList().get(0);
        Game game = new Game("Valorant");
        Alias alias = new Alias("Benjumpin");

        new AddGameCommand(null, firstPerson.getName(), game, false).execute(model);
        new AddAliasCommand(null, firstPerson.getName(), game, alias, false).execute(model);

        DeleteAliasCommand deleteAliasCommand =
                new DeleteAliasCommand(null, firstPerson.getName(), game, alias, false);
        deleteAliasCommand.execute(model);
        deleteAliasCommand.undo(model);

        Game gameAfterUndo = model.getFilteredPersonList().get(0).getGames().stream()
                .filter(g -> g.equals(game))
                .findFirst()
                .orElseThrow();
        assertTrue(gameAfterUndo.getAliases().contains(alias));
    }

    @Test
    public void toStringMethod() {
        Game game = new Game("Valorant");
        Alias alias = new Alias("Benjumpin");
        DeleteAliasCommand cmd = new DeleteAliasCommand(INDEX_FIRST_PERSON, null, game, alias, false);
        assertNotNull(cmd.toString());
    }

    @Test
    public void equals() {
        Game gameA = new Game("Valorant");
        Game gameB = new Game("Minecraft");
        Alias aliasA = new Alias("Benjumpin");
        Alias aliasB = new Alias("Alexyeoh");
        Name nameA = new Name("Alice");

        DeleteAliasCommand deleteAliasByIndex =
                new DeleteAliasCommand(INDEX_FIRST_PERSON, null, gameA, aliasA, false);
        DeleteAliasCommand deleteAliasByName = new DeleteAliasCommand(null, nameA, gameA, aliasA, false);

        // same object -> returns true
        org.junit.jupiter.api.Assertions.assertTrue(deleteAliasByIndex.equals(deleteAliasByIndex));

        // same values -> returns true
        DeleteAliasCommand deleteAliasByIndexCopy =
                new DeleteAliasCommand(INDEX_FIRST_PERSON, null, gameA, aliasA, false);
        org.junit.jupiter.api.Assertions.assertTrue(deleteAliasByIndex.equals(deleteAliasByIndexCopy));

        // different types -> returns false
        org.junit.jupiter.api.Assertions.assertFalse(deleteAliasByIndex.equals(1));

        // null -> returns false
        org.junit.jupiter.api.Assertions.assertFalse(deleteAliasByIndex.equals(null));

        // different target types (index vs name) -> returns false
        org.junit.jupiter.api.Assertions.assertFalse(deleteAliasByIndex.equals(deleteAliasByName));

        // different game -> returns false
        DeleteAliasCommand deleteDiffGame = new DeleteAliasCommand(INDEX_FIRST_PERSON, null, gameB, aliasA, false);
        org.junit.jupiter.api.Assertions.assertFalse(deleteAliasByIndex.equals(deleteDiffGame));

        // different alias -> returns false
        DeleteAliasCommand deleteDiffAlias =
                new DeleteAliasCommand(INDEX_FIRST_PERSON, null, gameA, aliasB, false);
        org.junit.jupiter.api.Assertions.assertFalse(deleteAliasByIndex.equals(deleteDiffAlias));

        // different useUserProfile -> returns false
        DeleteAliasCommand deleteWithProfile =
                new DeleteAliasCommand(INDEX_FIRST_PERSON, null, gameA, aliasA, true);
        org.junit.jupiter.api.Assertions.assertFalse(deleteAliasByIndex.equals(deleteWithProfile));

        // same values by name -> returns true
        DeleteAliasCommand deleteAliasByNameCopy = new DeleteAliasCommand(null, nameA, gameA, aliasA, false);
        org.junit.jupiter.api.Assertions.assertTrue(deleteAliasByName.equals(deleteAliasByNameCopy));
    }
}
