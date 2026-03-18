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
import seedu.address.model.person.Alias;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;

public class AddAliasCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_addAlias_success() throws Exception {
        Person firstPerson = model.getFilteredPersonList().get(0);
        Game game = new Game("Valorant");
        Alias alias = new Alias("Benjumpin");

        // Setup: add the game first
        new AddGameCommand(null, firstPerson.getName(), game).execute(model);

        AddAliasCommand addAliasCommand = new AddAliasCommand(null, firstPerson.getName(), game, alias);

        Set<Alias> expectedAliases = new HashSet<>();
        expectedAliases.add(alias);
        Game expectedGame = new Game(game.gameName, expectedAliases);
        Set<Game> expectedGames = new HashSet<>();
        expectedGames.add(expectedGame);

        Person editedPerson = new Person(firstPerson.getName(), firstPerson.getTags(), expectedGames);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(model.getFilteredPersonList().get(0), editedPerson);

        String expectedMessage = String.format(AddAliasCommand.MESSAGE_SUCCESS,
                editedPerson.getName(), game.gameName, alias);

        assertCommandSuccess(addAliasCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateAlias_failure() throws Exception {
        Person firstPerson = model.getFilteredPersonList().get(0);
        Game game = new Game("Valorant");
        Alias alias = new Alias("Benjumpin");

        new AddGameCommand(null, firstPerson.getName(), game).execute(model);
        new AddAliasCommand(null, firstPerson.getName(), game, alias).execute(model);

        AddAliasCommand addAliasCommand = new AddAliasCommand(null, firstPerson.getName(), game, alias);
        assertCommandFailure(addAliasCommand, model, AddAliasCommand.MESSAGE_DUPLICATE_ALIAS);
    }

    @Test
    public void execute_gameNotFound_failure() {
        Person firstPerson = model.getFilteredPersonList().get(0);
        Game game = new Game("NonExistentGame");
        Alias alias = new Alias("SomeAlias");

        AddAliasCommand addAliasCommand = new AddAliasCommand(null, firstPerson.getName(), game, alias);
        assertCommandFailure(addAliasCommand, model, AddAliasCommand.MESSAGE_GAME_NOT_FOUND);
    }

    @Test
    public void execute_personNotFound_failure() {
        Name notInModelName = new Name("Unknown Person Name");
        Game game = new Game("Valorant");
        Alias alias = new Alias("SomeAlias");

        AddAliasCommand addAliasCommand = new AddAliasCommand(null, notInModelName, game, alias);
        assertCommandFailure(addAliasCommand, model, AddAliasCommand.MESSAGE_PERSON_NOT_FOUND);
    }

    @Test
    public void execute_caseInsensitiveName_success() throws Exception {
        Person firstPerson = model.getFilteredPersonList().get(0);
        Game game = new Game("Valorant");
        Alias alias = new Alias("Benjumpin");

        // Setup: add the game using exact name
        new AddGameCommand(null, firstPerson.getName(), game).execute(model);

        // Use all-lowercase version of the stored name
        Name lowerCaseName = new Name(firstPerson.getName().fullName.toLowerCase());
        AddAliasCommand addAliasCommand = new AddAliasCommand(null, lowerCaseName, game, alias);

        Set<Alias> expectedAliases = new HashSet<>();
        expectedAliases.add(alias);
        Game expectedGame = new Game(game.gameName, expectedAliases);
        Set<Game> expectedGames = new HashSet<>();
        expectedGames.add(expectedGame);

        Person editedPerson = new Person(firstPerson.getName(), firstPerson.getTags(), expectedGames);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(model.getFilteredPersonList().get(0), editedPerson);

        String expectedMessage = String.format(AddAliasCommand.MESSAGE_SUCCESS,
                editedPerson.getName(), game.gameName, alias);

        assertCommandSuccess(addAliasCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_addAliasByIndex_success() throws Exception {
        Person firstPerson = model.getFilteredPersonList().get(0);
        Game game = new Game("Valorant");
        Alias alias = new Alias("Benjumpin");

        // Setup: add the game first (using index this time!)
        new AddGameCommand(INDEX_FIRST_PERSON, null, game).execute(model);

        // Test adding alias by Index (Name is null)
        AddAliasCommand addAliasCommand = new AddAliasCommand(INDEX_FIRST_PERSON, null, game, alias);

        Set<Alias> expectedAliases = new HashSet<>();
        expectedAliases.add(alias);
        Game expectedGame = new Game(game.gameName, expectedAliases);
        Set<Game> expectedGames = new HashSet<>();
        expectedGames.add(expectedGame);

        Person editedPerson = new Person(firstPerson.getName(), firstPerson.getTags(), expectedGames);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(model.getFilteredPersonList().get(0), editedPerson);

        String expectedMessage = String.format(AddAliasCommand.MESSAGE_SUCCESS,
                editedPerson.getName(), game.gameName, alias);

        assertCommandSuccess(addAliasCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndex_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        Game game = new Game("Valorant");
        Alias alias = new Alias("Benjumpin");

        AddAliasCommand addAliasCommand = new AddAliasCommand(outOfBoundIndex, null, game, alias);

        assertCommandFailure(addAliasCommand,
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

        AddAliasCommand addAliasByIndex = new AddAliasCommand(INDEX_FIRST_PERSON, null, gameA, aliasA);
        AddAliasCommand addAliasByName = new AddAliasCommand(null, nameA, gameA, aliasA);

        // same object -> returns true
        org.junit.jupiter.api.Assertions.assertTrue(addAliasByIndex.equals(addAliasByIndex));

        // same values -> returns true
        AddAliasCommand addAliasByIndexCopy = new AddAliasCommand(INDEX_FIRST_PERSON, null, gameA, aliasA);
        org.junit.jupiter.api.Assertions.assertTrue(addAliasByIndex.equals(addAliasByIndexCopy));

        // different types -> returns false
        org.junit.jupiter.api.Assertions.assertFalse(addAliasByIndex.equals(1));

        // null -> returns false
        org.junit.jupiter.api.Assertions.assertFalse(addAliasByIndex.equals(null));

        // different target types (index vs name) -> returns false
        org.junit.jupiter.api.Assertions.assertFalse(addAliasByIndex.equals(addAliasByName));

        // different game -> returns false
        AddAliasCommand addDiffGame = new AddAliasCommand(INDEX_FIRST_PERSON, null, gameB, aliasA);
        org.junit.jupiter.api.Assertions.assertFalse(addAliasByIndex.equals(addDiffGame));

        // different alias -> returns false
        AddAliasCommand addDiffAlias = new AddAliasCommand(INDEX_FIRST_PERSON, null, gameA, aliasB);
        org.junit.jupiter.api.Assertions.assertFalse(addAliasByIndex.equals(addDiffAlias));
    }
}
