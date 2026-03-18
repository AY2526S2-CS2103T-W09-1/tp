package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
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
        new AddGameCommand(null, firstPerson.getName(), game).execute(model);
        new AddAliasCommand(null, firstPerson.getName(), game, alias).execute(model);

        DeleteAliasCommand deleteAliasCommand =
                new DeleteAliasCommand(null, firstPerson.getName(), game, alias);

        // Expected model has the game but without the alias (same as after only adding the game)
        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        new AddGameCommand(null, firstPerson.getName(), game).execute(expectedModel);

        String expectedMessage = String.format(DeleteAliasCommand.MESSAGE_SUCCESS,
                firstPerson.getName(), game.gameName, alias);

        assertCommandSuccess(deleteAliasCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_aliasNotFound_failure() throws Exception {
        Person firstPerson = model.getFilteredPersonList().get(0);
        Game game = new Game("Valorant");
        Alias alias = new Alias("NonExistentAlias");

        new AddGameCommand(null, firstPerson.getName(), game).execute(model);

        DeleteAliasCommand deleteAliasCommand =
                new DeleteAliasCommand(null, firstPerson.getName(), game, alias);
        assertCommandFailure(deleteAliasCommand, model, DeleteAliasCommand.MESSAGE_ALIAS_NOT_FOUND);
    }

    @Test
    public void execute_gameNotFound_failure() {
        Person firstPerson = model.getFilteredPersonList().get(0);
        Game game = new Game("NonExistentGame");
        Alias alias = new Alias("SomeAlias");

        DeleteAliasCommand deleteAliasCommand =
                new DeleteAliasCommand(null, firstPerson.getName(), game, alias);
        assertCommandFailure(deleteAliasCommand, model, DeleteAliasCommand.MESSAGE_GAME_NOT_FOUND);
    }

    @Test
    public void execute_personNotFound_failure() {
        Name notInModelName = new Name("Unknown Person Name");
        Game game = new Game("Valorant");
        Alias alias = new Alias("SomeAlias");

        DeleteAliasCommand deleteAliasCommand = new DeleteAliasCommand(null, notInModelName, game, alias);
        assertCommandFailure(deleteAliasCommand, model, DeleteAliasCommand.MESSAGE_PERSON_NOT_FOUND);
    }

    @Test
    public void execute_caseInsensitiveName_success() throws Exception {
        Person firstPerson = model.getFilteredPersonList().get(0);
        Game game = new Game("Valorant");
        Alias alias = new Alias("Benjumpin");

        // Setup: add game and alias using exact name
        new AddGameCommand(null, firstPerson.getName(), game).execute(model);
        new AddAliasCommand(null, firstPerson.getName(), game, alias).execute(model);

        // Use all-lowercase version of the stored name
        Name lowerCaseName = new Name(firstPerson.getName().fullName.toLowerCase());
        DeleteAliasCommand deleteAliasCommand = new DeleteAliasCommand(null, lowerCaseName, game, alias);

        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        new AddGameCommand(null, firstPerson.getName(), game).execute(expectedModel);

        String expectedMessage = String.format(DeleteAliasCommand.MESSAGE_SUCCESS,
                firstPerson.getName(), game.gameName, alias);

        assertCommandSuccess(deleteAliasCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_deleteAliasByIndex_success() throws Exception {
        Person firstPerson = model.getFilteredPersonList().get(0);
        Game game = new Game("Valorant");
        Alias alias = new Alias("Benjumpin");

        // Setup: add game and alias first
        new AddGameCommand(INDEX_FIRST_PERSON, null, game).execute(model);
        new AddAliasCommand(INDEX_FIRST_PERSON, null, game, alias).execute(model);

        // Test deleting alias by Index (Name is null)
        DeleteAliasCommand deleteAliasCommand =
                new DeleteAliasCommand(INDEX_FIRST_PERSON, null, game, alias);

        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        new AddGameCommand(INDEX_FIRST_PERSON, null, game).execute(expectedModel);

        String expectedMessage = String.format(DeleteAliasCommand.MESSAGE_SUCCESS,
                firstPerson.getName(), game.gameName, alias);

        assertCommandSuccess(deleteAliasCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndex_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        Game game = new Game("Valorant");
        Alias alias = new Alias("SomeAlias");

        DeleteAliasCommand deleteAliasCommand = new DeleteAliasCommand(outOfBoundIndex, null, game, alias);

        assertCommandFailure(deleteAliasCommand,
                model,
                seedu.address.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        Game gameA = new Game("Valorant");
        Game gameB = new Game("Minecraft");
        Alias aliasA = new Alias("Benjumpin");
        Alias aliasB = new Alias("Alexyeoh");
        Name nameA = new Name("Alice");

        DeleteAliasCommand deleteAliasByIndex =
                new DeleteAliasCommand(INDEX_FIRST_PERSON, null, gameA, aliasA);
        DeleteAliasCommand deleteAliasByName = new DeleteAliasCommand(null, nameA, gameA, aliasA);

        // same object -> returns true
        org.junit.jupiter.api.Assertions.assertTrue(deleteAliasByIndex.equals(deleteAliasByIndex));

        // same values -> returns true
        DeleteAliasCommand deleteAliasByIndexCopy =
                new DeleteAliasCommand(INDEX_FIRST_PERSON, null, gameA, aliasA);
        org.junit.jupiter.api.Assertions.assertTrue(deleteAliasByIndex.equals(deleteAliasByIndexCopy));

        // different types -> returns false
        org.junit.jupiter.api.Assertions.assertFalse(deleteAliasByIndex.equals(1));

        // null -> returns false
        org.junit.jupiter.api.Assertions.assertFalse(deleteAliasByIndex.equals(null));

        // different target types (index vs name) -> returns false
        org.junit.jupiter.api.Assertions.assertFalse(deleteAliasByIndex.equals(deleteAliasByName));

        // different game -> returns false
        DeleteAliasCommand deleteDiffGame = new DeleteAliasCommand(INDEX_FIRST_PERSON, null, gameB, aliasA);
        org.junit.jupiter.api.Assertions.assertFalse(deleteAliasByIndex.equals(deleteDiffGame));

        // different alias -> returns false
        DeleteAliasCommand deleteDiffAlias =
                new DeleteAliasCommand(INDEX_FIRST_PERSON, null, gameA, aliasB);
        org.junit.jupiter.api.Assertions.assertFalse(deleteAliasByIndex.equals(deleteDiffAlias));
    }
}
