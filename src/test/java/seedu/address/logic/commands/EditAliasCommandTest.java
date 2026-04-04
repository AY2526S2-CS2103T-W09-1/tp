package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
import seedu.address.model.person.Alias;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;

public class EditAliasCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_editAliasByName_success() throws Exception {
        Person firstPerson = model.getFilteredPersonList().get(0);
        Game game = new Game("Valorant");
        Alias oldAlias = new Alias("JohnnyV");
        Alias newAlias = new Alias("JohnnyValorant");

        new AddGameCommand(null, firstPerson.getName(), game, false).execute(model);
        new AddAliasCommand(null, firstPerson.getName(), game, oldAlias, false).execute(model);

        EditAliasCommand editAliasCommand =
                new EditAliasCommand(null, firstPerson.getName(), game, oldAlias, newAlias, false);

        Set<Alias> expectedAliases = new HashSet<>();
        expectedAliases.add(newAlias);
        Game expectedGame = new Game(game.gameName, expectedAliases);
        Set<Game> expectedGames = new HashSet<>();
        expectedGames.add(expectedGame);

        Person editedPerson = new Person(firstPerson.getName(), expectedGames);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(model.getFilteredPersonList().get(0), editedPerson);

        String expectedMessage = String.format(EditAliasCommand.MESSAGE_SUCCESS,
                editedPerson.getName(), game.gameName, oldAlias, newAlias);

        CommandResult expectedCommandResult = new CommandResult(expectedMessage, false, false, editedPerson);

        assertCommandSuccess(editAliasCommand, model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_editAliasByIndex_success() throws Exception {
        Person firstPerson = model.getFilteredPersonList().get(1);
        Game game = new Game("Valorant");
        Alias oldAlias = new Alias("JohnnyV");
        Alias newAlias = new Alias("JohnnyValorant");

        new AddGameCommand(INDEX_FIRST_PERSON, null, game, false).execute(model);
        new AddAliasCommand(INDEX_FIRST_PERSON, null, game, oldAlias, false).execute(model);

        EditAliasCommand editAliasCommand =
                new EditAliasCommand(INDEX_FIRST_PERSON, null, game, oldAlias, newAlias, false);

        Set<Alias> expectedAliases = new HashSet<>();
        expectedAliases.add(newAlias);
        Game expectedGame = new Game(game.gameName, expectedAliases);
        Set<Game> expectedGames = new HashSet<>();
        expectedGames.add(expectedGame);

        Person editedPerson = new Person(firstPerson.getName(), expectedGames);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(model.getFilteredPersonList().get(1), editedPerson);

        String expectedMessage = String.format(EditAliasCommand.MESSAGE_SUCCESS,
                editedPerson.getName(), game.gameName, oldAlias, newAlias);

        CommandResult expectedCommandResult = new CommandResult(expectedMessage, false, false, editedPerson);

        assertCommandSuccess(editAliasCommand, model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_personNotFound_failure() {
        Name notInModelName = new Name("Unknown Person Name");
        Game game = new Game("Valorant");
        Alias oldAlias = new Alias("JohnnyV");
        Alias newAlias = new Alias("JohnnyValorant");

        EditAliasCommand editAliasCommand =
                new EditAliasCommand(null, notInModelName, game, oldAlias, newAlias, false);
        assertCommandFailure(editAliasCommand, model, EditAliasCommand.MESSAGE_PERSON_NOT_FOUND);
    }

    @Test
    public void execute_gameNotFound_failure() {
        Person firstPerson = model.getFilteredPersonList().get(0);
        Game game = new Game("NonExistentGame");
        Alias oldAlias = new Alias("JohnnyV");
        Alias newAlias = new Alias("JohnnyValorant");

        EditAliasCommand editAliasCommand =
                new EditAliasCommand(null, firstPerson.getName(), game, oldAlias, newAlias, false);
        assertCommandFailure(editAliasCommand, model, EditAliasCommand.MESSAGE_GAME_NOT_FOUND);
    }

    @Test
    public void execute_aliasNotFound_failure() throws Exception {
        Person firstPerson = model.getFilteredPersonList().get(0);
        Game game = new Game("Valorant");
        Alias nonExistentAlias = new Alias("NonExistentAlias");
        Alias newAlias = new Alias("JohnnyValorant");

        new AddGameCommand(null, firstPerson.getName(), game, false).execute(model);

        EditAliasCommand editAliasCommand =
                new EditAliasCommand(null, firstPerson.getName(), game, nonExistentAlias, newAlias, false);
        assertCommandFailure(editAliasCommand, model, EditAliasCommand.MESSAGE_ALIAS_NOT_FOUND);
    }

    @Test
    public void execute_duplicateNewAlias_failure() throws Exception {
        Person firstPerson = model.getFilteredPersonList().get(0);
        Game game = new Game("Valorant");
        Alias aliasA = new Alias("JohnnyV");
        Alias aliasB = new Alias("JohnnyValorant");

        new AddGameCommand(null, firstPerson.getName(), game, false).execute(model);
        new AddAliasCommand(null, firstPerson.getName(), game, aliasA, false).execute(model);
        new AddAliasCommand(null, firstPerson.getName(), game, aliasB, false).execute(model);

        EditAliasCommand editAliasCommand =
                new EditAliasCommand(null, firstPerson.getName(), game, aliasA, aliasB, false);
        assertCommandFailure(editAliasCommand, model, EditAliasCommand.MESSAGE_DUPLICATE_ALIAS);
    }

    @Test
    public void execute_invalidIndex_failure() {
        Index outOfBoundIndex = Index.fromZeroBased(model.getFilteredPersonList().size() + 1);
        Game game = new Game("Valorant");
        Alias oldAlias = new Alias("JohnnyV");
        Alias newAlias = new Alias("JohnnyValorant");

        EditAliasCommand editAliasCommand =
                new EditAliasCommand(outOfBoundIndex, null, game, oldAlias, newAlias, false);
        assertCommandFailure(editAliasCommand, model,
                seedu.address.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_nullIndexAndName_failure() {
        Game game = new Game("Valorant");
        Alias oldAlias = new Alias("JohnnyV");
        Alias newAlias = new Alias("JohnnyValorant");

        EditAliasCommand editAliasCommand =
                new EditAliasCommand(null, null, game, oldAlias, newAlias, false);
        assertCommandFailure(editAliasCommand, model, EditAliasCommand.MESSAGE_PERSON_NOT_FOUND);
    }

    @Test
    public void execute_useUserProfile_success() throws Exception {
        Person userProfile = new Person(new Name("John Doe"), new HashSet<>(), true);
        AddressBook ab = new AddressBook();
        ab.addPerson(userProfile);
        Model profileModel = new ModelManager(ab, new UserPrefs());

        Game game = new Game("Valorant");
        Alias oldAlias = new Alias("JohnnyV");
        Alias newAlias = new Alias("JohnnyValorant");

        new AddGameCommand(null, null, game, true).execute(profileModel);
        new AddAliasCommand(null, null, game, oldAlias, true).execute(profileModel);

        EditAliasCommand editAliasCommand =
                new EditAliasCommand(null, null, game, oldAlias, newAlias, true);
        String expectedMessage = String.format(EditAliasCommand.MESSAGE_SUCCESS,
                "John Doe", game.gameName, oldAlias, newAlias);

        CommandResult result = editAliasCommand.execute(profileModel);
        assertEquals(expectedMessage, result.getFeedbackToUser());
        assertEquals(profileModel.getUserProfile().get(), result.getViewedPerson());
    }

    @Test
    public void execute_noProfile_failure() {
        Model emptyModel = new ModelManager(new AddressBook(), new UserPrefs());
        Game game = new Game("Valorant");
        Alias oldAlias = new Alias("JohnnyV");
        Alias newAlias = new Alias("JohnnyValorant");
        emptyModel.deletePerson(emptyModel.getFilteredPersonList().get(0));

        EditAliasCommand editAliasCommand =
                new EditAliasCommand(null, null, game, oldAlias, newAlias, true);
        assertCommandFailure(editAliasCommand, emptyModel, "No user profile found.");
    }

    @Test
    public void undo_editAlias_restoresOriginalAlias() throws Exception {
        Person firstPerson = model.getFilteredPersonList().get(0);
        Game game = new Game("Valorant");
        Alias oldAlias = new Alias("JohnnyV");
        Alias newAlias = new Alias("JohnnyValorant");

        new AddGameCommand(null, firstPerson.getName(), game, false).execute(model);
        new AddAliasCommand(null, firstPerson.getName(), game, oldAlias, false).execute(model);

        EditAliasCommand editAliasCommand =
                new EditAliasCommand(null, firstPerson.getName(), game, oldAlias, newAlias, false);
        editAliasCommand.execute(model);

        Person afterEdit = model.getFilteredPersonList().get(0);
        Game gameAfterEdit = afterEdit.getGames().stream().filter(g -> g.equals(game)).findFirst().orElseThrow();
        assertTrue(gameAfterEdit.getAliases().contains(newAlias));
        assertFalse(gameAfterEdit.getAliases().contains(oldAlias));

        editAliasCommand.undo(model);

        Person afterUndo = model.getFilteredPersonList().get(0);
        Game gameAfterUndo = afterUndo.getGames().stream().filter(g -> g.equals(game)).findFirst().orElseThrow();
        assertTrue(gameAfterUndo.getAliases().contains(oldAlias));
        assertFalse(gameAfterUndo.getAliases().contains(newAlias));
    }

    @Test
    public void toStringMethod() {
        Game game = new Game("Valorant");
        Alias oldAlias = new Alias("JohnnyV");
        Alias newAlias = new Alias("JohnnyValorant");
        EditAliasCommand cmd = new EditAliasCommand(INDEX_FIRST_PERSON, null, game, oldAlias, newAlias, false);
        assertNotNull(cmd.toString());
    }

    @Test
    public void equals() {
        Game gameA = new Game("Valorant");
        Game gameB = new Game("Minecraft");
        Alias aliasA = new Alias("JohnnyV");
        Alias aliasB = new Alias("JohnnyValorant");
        Alias aliasC = new Alias("SomeOtherAlias");
        Name nameA = new Name("Alice");

        EditAliasCommand editByIndex = new EditAliasCommand(INDEX_FIRST_PERSON, null, gameA, aliasA, aliasB, false);
        EditAliasCommand editByName = new EditAliasCommand(null, nameA, gameA, aliasA, aliasB, false);

        // same object -> returns true
        org.junit.jupiter.api.Assertions.assertTrue(editByIndex.equals(editByIndex));

        // same values (by index) -> returns true
        EditAliasCommand editByIndexCopy =
                new EditAliasCommand(INDEX_FIRST_PERSON, null, gameA, aliasA, aliasB, false);
        org.junit.jupiter.api.Assertions.assertTrue(editByIndex.equals(editByIndexCopy));

        // same values (by name) -> returns true
        EditAliasCommand editByNameCopy = new EditAliasCommand(null, nameA, gameA, aliasA, aliasB, false);
        org.junit.jupiter.api.Assertions.assertTrue(editByName.equals(editByNameCopy));

        // different types -> returns false
        org.junit.jupiter.api.Assertions.assertFalse(editByIndex.equals(1));

        // null -> returns false
        org.junit.jupiter.api.Assertions.assertFalse(editByIndex.equals(null));

        // different target types (index vs name) -> returns false
        org.junit.jupiter.api.Assertions.assertFalse(editByIndex.equals(editByName));

        // different game -> returns false
        EditAliasCommand editDiffGame =
                new EditAliasCommand(INDEX_FIRST_PERSON, null, gameB, aliasA, aliasB, false);
        org.junit.jupiter.api.Assertions.assertFalse(editByIndex.equals(editDiffGame));

        // different old alias -> returns false
        EditAliasCommand editDiffOldAlias =
                new EditAliasCommand(INDEX_FIRST_PERSON, null, gameA, aliasC, aliasB, false);
        org.junit.jupiter.api.Assertions.assertFalse(editByIndex.equals(editDiffOldAlias));

        // different new alias -> returns false
        EditAliasCommand editDiffNewAlias =
                new EditAliasCommand(INDEX_FIRST_PERSON, null, gameA, aliasA, aliasC, false);
        org.junit.jupiter.api.Assertions.assertFalse(editByIndex.equals(editDiffNewAlias));
    }
}
