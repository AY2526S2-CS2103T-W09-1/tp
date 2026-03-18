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
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;

public class ListGameCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_listGamesByName_success() throws Exception {
        Person firstPerson = model.getFilteredPersonList().get(0);
        Game gameToAdd = new Game("Minecraft");

        // Setup: Add one game
        new AddGameCommand(null, firstPerson.getName(), gameToAdd).execute(model);

        ListGameCommand listGameCommand = new ListGameCommand(null, firstPerson.getName());

        // Expected model doesn't change during a list command
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        String expectedMessage = String.format(ListGameCommand.MESSAGE_SUCCESS,
                firstPerson.getName().fullName,
                "Minecraft");

        assertCommandSuccess(listGameCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noGames_success() {
        Person firstPerson = model.getFilteredPersonList().get(0);
        ListGameCommand listGameCommand = new ListGameCommand(null, firstPerson.getName());

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        String expectedMessage = String.format(ListGameCommand.MESSAGE_NO_GAMES, firstPerson.getName().fullName);

        assertCommandSuccess(listGameCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_contactNotFoundByName_failure() {
        Name notInModelName = new Name("Unknown Person Name");
        ListGameCommand listGameCommand = new ListGameCommand(null, notInModelName);

        assertCommandFailure(listGameCommand, model, ListGameCommand.MESSAGE_CONTACT_NOT_FOUND);
    }

    // NEW TESTS: Index Support and Equals Method

    @Test
    public void execute_listGamesByIndex_success() throws Exception {
        Person firstPerson = model.getFilteredPersonList().get(0);
        Game gameToAdd = new Game("Minecraft");

        // Setup: Add one game
        new AddGameCommand(INDEX_FIRST_PERSON, null, gameToAdd).execute(model);

        // Test listing by Index (Name is null)
        ListGameCommand listGameCommand = new ListGameCommand(INDEX_FIRST_PERSON, null);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        String expectedMessage = String.format(ListGameCommand.MESSAGE_SUCCESS,
                firstPerson.getName().fullName,
                "Minecraft");

        assertCommandSuccess(listGameCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndex_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);

        ListGameCommand listGameCommand = new ListGameCommand(outOfBoundIndex, null);

        assertCommandFailure(listGameCommand, model,
                seedu.address.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        Name nameA = new Name("Alice");
        Name nameB = new Name("Bob");
        Index secondIndex = Index.fromOneBased(2);

        ListGameCommand listByIndex = new ListGameCommand(INDEX_FIRST_PERSON, null);
        ListGameCommand listByName = new ListGameCommand(null, nameA);

        // same object -> returns true
        org.junit.jupiter.api.Assertions.assertTrue(listByIndex.equals(listByIndex));

        // same values -> returns true
        ListGameCommand listByIndexCopy = new ListGameCommand(INDEX_FIRST_PERSON, null);
        org.junit.jupiter.api.Assertions.assertTrue(listByIndex.equals(listByIndexCopy));

        // different types -> returns false
        org.junit.jupiter.api.Assertions.assertFalse(listByIndex.equals(1));

        // null -> returns false
        org.junit.jupiter.api.Assertions.assertFalse(listByIndex.equals(null));

        // different target types (index vs name) -> returns false
        org.junit.jupiter.api.Assertions.assertFalse(listByIndex.equals(listByName));

        // different index -> returns false
        ListGameCommand listByDiffIndex = new ListGameCommand(secondIndex, null);
        org.junit.jupiter.api.Assertions.assertFalse(listByIndex.equals(listByDiffIndex));

        // different name -> returns false
        ListGameCommand listByDiffName = new ListGameCommand(null, nameB);
        org.junit.jupiter.api.Assertions.assertFalse(listByName.equals(listByDiffName));
    }
}
