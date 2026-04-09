package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
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
    public void execute_deleteAlias_returnsConfirmation() throws Exception {
        Person firstPerson = model.getFilteredPersonList().get(0);
        Game game = new Game("Valorant");
        Alias alias = new Alias("Benjumpin");

        new AddGameCommand(null, firstPerson.getName(), game, false).execute(model);
        new AddAliasCommand(null, firstPerson.getName(), game, alias, false).execute(model);
        Person firstPersonAfterSetup = model.getFilteredPersonList().get(0);

        DeleteAliasCommand deleteAliasCommand =
                new DeleteAliasCommand(null, firstPerson.getName(), game, alias, false);

        CommandResult result = deleteAliasCommand.execute(model);

        assertTrue(result.isAwaitingConfirmation());
        assertEquals(firstPersonAfterSetup, result.getPendingPerson());
    }

    @Test
    public void performDeletion_deleteAlias_success() throws Exception {
        Person firstPerson = model.getFilteredPersonList().get(0);
        Game game = new Game("Valorant");
        Alias alias = new Alias("Benjumpin");

        new AddGameCommand(null, firstPerson.getName(), game, false).execute(model);
        new AddAliasCommand(null, firstPerson.getName(), game, alias, false).execute(model);

        DeleteAliasCommand deleteAliasCommand =
                new DeleteAliasCommand(null, firstPerson.getName(), game, alias, false);

        deleteAliasCommand.execute(model);

        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        new AddGameCommand(null, firstPerson.getName(), game, false).execute(expectedModel);

        Person editedPerson = expectedModel.getFilteredPersonList().get(0);

        String expectedMessage = String.format(DeleteAliasCommand.MESSAGE_SUCCESS,
                firstPerson.getName(), game.gameName, alias);

        CommandResult result = deleteAliasCommand.performDeletion(model);

        assertEquals(expectedMessage, result.getFeedbackToUser());
        assertEquals(editedPerson, result.getViewedPerson());
        assertEquals(expectedModel, model);
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
    public void execute_caseInsensitiveName_returnsConfirmation() throws Exception {
        Person firstPerson = model.getFilteredPersonList().get(0);
        Game game = new Game("Valorant");
        Alias alias = new Alias("Benjumpin");

        new AddGameCommand(null, firstPerson.getName(), game, false).execute(model);
        new AddAliasCommand(null, firstPerson.getName(), game, alias, false).execute(model);

        Name lowerCaseName = new Name(firstPerson.getName().fullName.toLowerCase());
        DeleteAliasCommand deleteAliasCommand = new DeleteAliasCommand(null, lowerCaseName, game, alias, false);

        CommandResult result = deleteAliasCommand.execute(model);
        assertTrue(result.isAwaitingConfirmation());
    }

    @Test
    public void execute_deleteAliasByIndex_returnsConfirmation() throws Exception {
        Person secondPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Game game = new Game("Valorant");
        Alias alias = new Alias("Benjumpin");

        new AddGameCommand(null, secondPerson.getName(), game, false).execute(model);
        new AddAliasCommand(null, secondPerson.getName(), game, alias, false).execute(model);

        DeleteAliasCommand deleteAliasCommand =
                new DeleteAliasCommand(INDEX_FIRST_PERSON, null, game, alias, false);

        CommandResult result = deleteAliasCommand.execute(model);
        assertTrue(result.isAwaitingConfirmation());
    }

    @Test
    public void execute_invalidIndex_failure() {
        Index outOfBoundIndex = Index.fromZeroBased(model.getFilteredPersonList().size() + 1);
        Game game = new Game("Valorant");
        Alias alias = new Alias("SomeAlias");

        DeleteAliasCommand deleteAliasCommand = new DeleteAliasCommand(outOfBoundIndex, null, game, alias, false);

        assertCommandFailure(deleteAliasCommand,
                model,
                seedu.address.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX
                        + "\n" + DeleteAliasCommand.MESSAGE_USAGE);
    }

    @Test
    public void execute_useUserProfile_returnsConfirmation() throws Exception {
        Person userProfile = new Person(new Name("John Doe"), new HashSet<>(), true);
        AddressBook ab = new AddressBook();
        ab.addPerson(userProfile);
        Model profileModel = new ModelManager(ab, new UserPrefs());

        Game game = new Game("Valorant");
        Alias alias = new Alias("JohnV");
        new AddGameCommand(null, null, game, true).execute(profileModel);
        new AddAliasCommand(null, null, game, alias, true).execute(profileModel);

        DeleteAliasCommand deleteAliasCommand = new DeleteAliasCommand(null, null, game, alias, true);
        CommandResult result = deleteAliasCommand.execute(profileModel);

        assertTrue(result.isAwaitingConfirmation());
        assertEquals(profileModel.getUserProfile().get(), result.getPendingPerson());
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
    public void getCancelMessage_returnsCorrectMessage() throws Exception {
        Person firstPerson = model.getFilteredPersonList().get(0);
        Game game = new Game("Valorant");
        Alias alias = new Alias("Benjumpin");

        new AddGameCommand(null, firstPerson.getName(), game, false).execute(model);
        new AddAliasCommand(null, firstPerson.getName(), game, alias, false).execute(model);

        DeleteAliasCommand deleteAliasCommand =
                new DeleteAliasCommand(null, firstPerson.getName(), game, alias, false);
        deleteAliasCommand.execute(model);

        String expectedMessage = String.format(DeleteAliasCommand.MESSAGE_DELETE_CANCELLED,
                firstPerson.getName(), game.gameName, alias);
        assertEquals(expectedMessage, deleteAliasCommand.getCancelMessage());
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
        deleteAliasCommand.performDeletion(model);
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

        assertTrue(deleteAliasByIndex.equals(deleteAliasByIndex));

        DeleteAliasCommand deleteAliasByIndexCopy =
                new DeleteAliasCommand(INDEX_FIRST_PERSON, null, gameA, aliasA, false);
        assertTrue(deleteAliasByIndex.equals(deleteAliasByIndexCopy));

        org.junit.jupiter.api.Assertions.assertFalse(deleteAliasByIndex.equals(1));
        org.junit.jupiter.api.Assertions.assertFalse(deleteAliasByIndex.equals(null));
        org.junit.jupiter.api.Assertions.assertFalse(deleteAliasByIndex.equals(deleteAliasByName));

        DeleteAliasCommand deleteDiffGame = new DeleteAliasCommand(INDEX_FIRST_PERSON, null, gameB, aliasA, false);
        org.junit.jupiter.api.Assertions.assertFalse(deleteAliasByIndex.equals(deleteDiffGame));

        DeleteAliasCommand deleteDiffAlias =
                new DeleteAliasCommand(INDEX_FIRST_PERSON, null, gameA, aliasB, false);
        org.junit.jupiter.api.Assertions.assertFalse(deleteAliasByIndex.equals(deleteDiffAlias));

        DeleteAliasCommand deleteWithProfile =
                new DeleteAliasCommand(INDEX_FIRST_PERSON, null, gameA, aliasA, true);
        org.junit.jupiter.api.Assertions.assertFalse(deleteAliasByIndex.equals(deleteWithProfile));

        DeleteAliasCommand deleteAliasByNameCopy = new DeleteAliasCommand(null, nameA, gameA, aliasA, false);
        org.junit.jupiter.api.Assertions.assertTrue(deleteAliasByName.equals(deleteAliasByNameCopy));
    }
}
