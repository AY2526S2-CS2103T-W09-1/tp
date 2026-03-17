package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

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
        new AddGameCommand(firstPerson.getName(), game).execute(model);
        new AddAliasCommand(firstPerson.getName(), game, alias).execute(model);

        DeleteAliasCommand deleteAliasCommand = new DeleteAliasCommand(firstPerson.getName(), game, alias);

        // Expected model has the game but without the alias (same as after only adding the game)
        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        new AddGameCommand(firstPerson.getName(), game).execute(expectedModel);

        String expectedMessage = String.format(DeleteAliasCommand.MESSAGE_SUCCESS,
                firstPerson.getName(), game.gameName, alias);

        assertCommandSuccess(deleteAliasCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_aliasNotFound_failure() throws Exception {
        Person firstPerson = model.getFilteredPersonList().get(0);
        Game game = new Game("Valorant");
        Alias alias = new Alias("NonExistentAlias");

        new AddGameCommand(firstPerson.getName(), game).execute(model);

        DeleteAliasCommand deleteAliasCommand = new DeleteAliasCommand(firstPerson.getName(), game, alias);
        assertCommandFailure(deleteAliasCommand, model, DeleteAliasCommand.MESSAGE_ALIAS_NOT_FOUND);
    }

    @Test
    public void execute_gameNotFound_failure() {
        Person firstPerson = model.getFilteredPersonList().get(0);
        Game game = new Game("NonExistentGame");
        Alias alias = new Alias("SomeAlias");

        DeleteAliasCommand deleteAliasCommand = new DeleteAliasCommand(firstPerson.getName(), game, alias);
        assertCommandFailure(deleteAliasCommand, model, DeleteAliasCommand.MESSAGE_GAME_NOT_FOUND);
    }

    @Test
    public void execute_personNotFound_failure() {
        Name notInModelName = new Name("Unknown Person Name");
        Game game = new Game("Valorant");
        Alias alias = new Alias("SomeAlias");

        DeleteAliasCommand deleteAliasCommand = new DeleteAliasCommand(notInModelName, game, alias);
        assertCommandFailure(deleteAliasCommand, model, DeleteAliasCommand.MESSAGE_PERSON_NOT_FOUND);
    }

    @Test
    public void execute_caseInsensitiveName_success() throws Exception {
        Person firstPerson = model.getFilteredPersonList().get(0);
        Game game = new Game("Valorant");
        Alias alias = new Alias("Benjumpin");

        // Setup: add game and alias using exact name
        new AddGameCommand(firstPerson.getName(), game).execute(model);
        new AddAliasCommand(firstPerson.getName(), game, alias).execute(model);

        // Use all-lowercase version of the stored name
        Name lowerCaseName = new Name(firstPerson.getName().fullName.toLowerCase());
        DeleteAliasCommand deleteAliasCommand = new DeleteAliasCommand(lowerCaseName, game, alias);

        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        new AddGameCommand(firstPerson.getName(), game).execute(expectedModel);

        String expectedMessage = String.format(DeleteAliasCommand.MESSAGE_SUCCESS,
                firstPerson.getName(), game.gameName, alias);

        assertCommandSuccess(deleteAliasCommand, model, expectedMessage, expectedModel);
    }
}
