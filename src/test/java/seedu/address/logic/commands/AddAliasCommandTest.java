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

public class AddAliasCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_addAlias_success() throws Exception {
        Person firstPerson = model.getFilteredPersonList().get(0);
        Game game = new Game("Valorant");
        Alias alias = new Alias("Benjumpin");

        // Setup: add the game first
        new AddGameCommand(null, firstPerson.getName(), game, false).execute(model);

        AddAliasCommand addAliasCommand = new AddAliasCommand(null, firstPerson.getName(), game, alias, false);

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

        CommandResult expectedCommandResult = new CommandResult(expectedMessage, false, false, editedPerson);

        assertCommandSuccess(addAliasCommand, model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_duplicateAlias_failure() throws Exception {
        Person firstPerson = model.getFilteredPersonList().get(0);
        Game game = new Game("Valorant");
        Alias alias = new Alias("Benjumpin");

        new AddGameCommand(null, firstPerson.getName(), game, false).execute(model);
        new AddAliasCommand(null, firstPerson.getName(), game, alias, false).execute(model);

        AddAliasCommand addAliasCommand = new AddAliasCommand(null, firstPerson.getName(), game, alias, false);
        assertCommandFailure(addAliasCommand, model, AddAliasCommand.MESSAGE_DUPLICATE_ALIAS);
    }

    @Test
    public void execute_gameNotFound_failure() {
        Person firstPerson = model.getFilteredPersonList().get(0);
        Game game = new Game("NonExistentGame");
        Alias alias = new Alias("SomeAlias");

        AddAliasCommand addAliasCommand = new AddAliasCommand(null, firstPerson.getName(), game, alias, false);
        assertCommandFailure(addAliasCommand, model, AddAliasCommand.MESSAGE_GAME_NOT_FOUND);
    }

    @Test
    public void execute_personNotFound_failure() {
        Name notInModelName = new Name("Unknown Person Name");
        Game game = new Game("Valorant");
        Alias alias = new Alias("SomeAlias");

        AddAliasCommand addAliasCommand = new AddAliasCommand(null, notInModelName, game, alias, false);
        assertCommandFailure(addAliasCommand, model, AddAliasCommand.MESSAGE_PERSON_NOT_FOUND);
    }

    @Test
    public void execute_caseInsensitiveName_success() throws Exception {
        Person firstPerson = model.getFilteredPersonList().get(0);
        Game game = new Game("Valorant");
        Alias alias = new Alias("Benjumpin");

        // Setup: add the game using exact name
        new AddGameCommand(null, firstPerson.getName(), game, false).execute(model);

        // Use all-lowercase version of the stored name
        Name lowerCaseName = new Name(firstPerson.getName().fullName.toLowerCase());
        AddAliasCommand addAliasCommand = new AddAliasCommand(null, lowerCaseName, game, alias, false);

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

        CommandResult expectedCommandResult = new CommandResult(expectedMessage, false, false, editedPerson);

        assertCommandSuccess(addAliasCommand, model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_addAliasByIndex_success() throws Exception {
        Person firstPerson = model.getFilteredPersonList().get(0);
        Game game = new Game("Valorant");
        Alias alias = new Alias("Benjumpin");

        // Setup: add the game first (using index this time!)
        new AddGameCommand(INDEX_FIRST_PERSON, null, game, false).execute(model);

        // Test adding alias by Index (Name is null)
        AddAliasCommand addAliasCommand = new AddAliasCommand(INDEX_FIRST_PERSON, null, game, alias, false);

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


        CommandResult expectedCommandResult = new CommandResult(expectedMessage, false, false, editedPerson);

        assertCommandSuccess(addAliasCommand, model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_invalidIndex_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        Game game = new Game("Valorant");
        Alias alias = new Alias("Benjumpin");

        AddAliasCommand addAliasCommand = new AddAliasCommand(outOfBoundIndex, null, game, alias, false);

        assertCommandFailure(addAliasCommand,
                model,
                seedu.address.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_useUserProfile_success() throws Exception {
        Person userProfile = new Person(new Name("John Doe"), new HashSet<>(), new HashSet<>(), true);
        AddressBook ab = new AddressBook();
        ab.addPerson(userProfile);
        Model profileModel = new ModelManager(ab, new UserPrefs());

        Game game = new Game("Valorant");
        new AddGameCommand(null, null, game, true).execute(profileModel);

        Alias alias = new Alias("JohnV");
        AddAliasCommand addAliasCommand = new AddAliasCommand(null, null, game, alias, true);
        String expectedMessage = String.format(AddAliasCommand.MESSAGE_SUCCESS, "John Doe", game.gameName, alias);

        CommandResult result = addAliasCommand.execute(profileModel);
        assertEquals(expectedMessage, result.getFeedbackToUser());
        assertTrue(profileModel.getUserProfile().isPresent());
        assertEquals(profileModel.getUserProfile().get(), result.getViewedPerson());
        assertTrue(profileModel.getUserProfile().get().getGames().iterator().next().getAliases().contains(alias));
    }

    @Test
    public void execute_noProfile_failure() {
        Model emptyModel = new ModelManager(new AddressBook(), new UserPrefs());
        Game game = new Game("Valorant");
        Alias alias = new Alias("JohnV");

        AddAliasCommand addAliasCommand = new AddAliasCommand(null, null, game, alias, true);
        assertCommandFailure(addAliasCommand, emptyModel, "No user profile found.");
    }

    @Test
    public void execute_nullIndexAndName_failure() {
        Game game = new Game("Valorant");
        Alias alias = new Alias("SomeAlias");
        AddAliasCommand addAliasCommand = new AddAliasCommand(null, null, game, alias, false);
        assertCommandFailure(addAliasCommand, model, AddAliasCommand.MESSAGE_PERSON_NOT_FOUND);
    }

    @Test
    public void undo_addAlias_aliasRemoved() throws Exception {
        Person firstPerson = model.getFilteredPersonList().get(0);
        Game game = new Game("Valorant");
        Alias alias = new Alias("Benjumpin");

        new AddGameCommand(null, firstPerson.getName(), game, false).execute(model);
        AddAliasCommand addAliasCommand = new AddAliasCommand(null, firstPerson.getName(), game, alias, false);
        addAliasCommand.execute(model);
        addAliasCommand.undo(model);

        Game gameAfterUndo = model.getFilteredPersonList().get(0).getGames().stream()
                .filter(g -> g.equals(game))
                .findFirst()
                .orElseThrow();
        assertFalse(gameAfterUndo.getAliases().contains(alias));
    }

    @Test
    public void toStringMethod() {
        Game game = new Game("Valorant");
        Alias alias = new Alias("Benjumpin");
        AddAliasCommand cmd = new AddAliasCommand(INDEX_FIRST_PERSON, null, game, alias, false);
        assertNotNull(cmd.toString());
    }

    @Test
    public void equals() {
        Game gameA = new Game("Valorant");
        Game gameB = new Game("Minecraft");
        Alias aliasA = new Alias("Benjumpin");
        Alias aliasB = new Alias("Alexyeoh");
        Name nameA = new Name("Alice");

        AddAliasCommand addAliasByIndex = new AddAliasCommand(INDEX_FIRST_PERSON, null, gameA, aliasA, false);
        AddAliasCommand addAliasByName = new AddAliasCommand(null, nameA, gameA, aliasA, false);

        // same object -> returns true
        org.junit.jupiter.api.Assertions.assertTrue(addAliasByIndex.equals(addAliasByIndex));

        // same values -> returns true
        AddAliasCommand addAliasByIndexCopy = new AddAliasCommand(INDEX_FIRST_PERSON, null, gameA, aliasA, false);
        org.junit.jupiter.api.Assertions.assertTrue(addAliasByIndex.equals(addAliasByIndexCopy));

        // different types -> returns false
        org.junit.jupiter.api.Assertions.assertFalse(addAliasByIndex.equals(1));

        // null -> returns false
        org.junit.jupiter.api.Assertions.assertFalse(addAliasByIndex.equals(null));

        // different target types (index vs name) -> returns false
        org.junit.jupiter.api.Assertions.assertFalse(addAliasByIndex.equals(addAliasByName));

        // different game -> returns false
        AddAliasCommand addDiffGame = new AddAliasCommand(INDEX_FIRST_PERSON, null, gameB, aliasA, false);
        org.junit.jupiter.api.Assertions.assertFalse(addAliasByIndex.equals(addDiffGame));

        // different alias -> returns false
        AddAliasCommand addDiffAlias = new AddAliasCommand(INDEX_FIRST_PERSON, null, gameA, aliasB, false);
        org.junit.jupiter.api.Assertions.assertFalse(addAliasByIndex.equals(addDiffAlias));

        // different useUserProfile -> returns false
        AddAliasCommand addWithProfile = new AddAliasCommand(INDEX_FIRST_PERSON, null, gameA, aliasA, true);
        org.junit.jupiter.api.Assertions.assertFalse(addAliasByIndex.equals(addWithProfile));

        // same values by name -> returns true
        AddAliasCommand addAliasByNameCopy = new AddAliasCommand(null, nameA, gameA, aliasA, false);
        org.junit.jupiter.api.Assertions.assertTrue(addAliasByName.equals(addAliasByNameCopy));
    }
}
