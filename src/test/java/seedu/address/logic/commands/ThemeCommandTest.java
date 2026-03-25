package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

public class ThemeCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validThemeLight_success() {
        ThemeCommand themeCommand = new ThemeCommand("light");
        String expectedMessage = String.format(ThemeCommand.MESSAGE_SUCCESS, "light");
        CommandResult expectedCommandResult = new CommandResult(expectedMessage, "light");
        assertCommandSuccess(themeCommand, model, expectedCommandResult, model);
    }

    @Test
    public void execute_invalidTheme_throwsCommandException() {
        ThemeCommand themeCommand = new ThemeCommand("invalid");
        assertCommandFailure(themeCommand, model, ThemeCommand.MESSAGE_INVALID_THEME);
    }

    @Test
    public void equals() {
        ThemeCommand lightCommand = new ThemeCommand("light");
        ThemeCommand darkCommand = new ThemeCommand("dark");

        // same object -> returns true
        assertEquals(lightCommand, lightCommand);

        // same values -> returns true
        ThemeCommand lightCommandCopy = new ThemeCommand("light");
        assertEquals(lightCommand, lightCommandCopy);

        // different types -> returns false
        assertNotEquals(1, lightCommand);

        // null -> returns false
        assertNotEquals(null, lightCommand);

        // different theme -> returns false
        assertNotEquals(lightCommand, darkCommand);
    }
}
