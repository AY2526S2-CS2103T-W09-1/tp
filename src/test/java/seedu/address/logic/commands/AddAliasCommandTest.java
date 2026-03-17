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
        new AddGameCommand(firstPerson.getName(), game).execute(model);

        AddAliasCommand addAliasCommand = new AddAliasCommand(firstPerson.getName(), game, alias);

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

        new AddGameCommand(firstPerson.getName(), game).execute(model);
        new AddAliasCommand(firstPerson.getName(), game, alias).execute(model);

        AddAliasCommand addAliasCommand = new AddAliasCommand(firstPerson.getName(), game, alias);
        assertCommandFailure(addAliasCommand, model, AddAliasCommand.MESSAGE_DUPLICATE_ALIAS);
    }

    @Test
    public void execute_gameNotFound_failure() {
        Person firstPerson = model.getFilteredPersonList().get(0);
        Game game = new Game("NonExistentGame");
        Alias alias = new Alias("SomeAlias");

        AddAliasCommand addAliasCommand = new AddAliasCommand(firstPerson.getName(), game, alias);
        assertCommandFailure(addAliasCommand, model, AddAliasCommand.MESSAGE_GAME_NOT_FOUND);
    }

    @Test
    public void execute_personNotFound_failure() {
        Name notInModelName = new Name("Unknown Person Name");
        Game game = new Game("Valorant");
        Alias alias = new Alias("SomeAlias");

        AddAliasCommand addAliasCommand = new AddAliasCommand(notInModelName, game, alias);
        assertCommandFailure(addAliasCommand, model, AddAliasCommand.MESSAGE_PERSON_NOT_FOUND);
    }

    @Test
    public void execute_caseInsensitiveName_success() throws Exception {
        Person firstPerson = model.getFilteredPersonList().get(0);
        Game game = new Game("Valorant");
        Alias alias = new Alias("Benjumpin");

        // Setup: add the game using exact name
        new AddGameCommand(firstPerson.getName(), game).execute(model);

        // Use all-lowercase version of the stored name
        Name lowerCaseName = new Name(firstPerson.getName().fullName.toLowerCase());
        AddAliasCommand addAliasCommand = new AddAliasCommand(lowerCaseName, game, alias);

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
}
