package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Switches the application's UI theme.
 */
public class ThemeCommand extends Command {

    public static final String COMMAND_WORD = "theme";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Switches the application theme to light or dark.\n"
            + "Parameters: THEME (must be 'light' or 'dark')\n"
            + "Example: " + COMMAND_WORD + " light";

    public static final String MESSAGE_SUCCESS = "Theme switched to: %1$s";
    public static final String MESSAGE_INVALID_THEME = "Invalid theme! Please specify either 'light' or 'dark'.";

    private final String themeParameter;

    /**
     * Creates a ThemeCommand to switch to the specified theme.
     */
    public ThemeCommand(String themeParameter) {
        requireNonNull(themeParameter);
        this.themeParameter = themeParameter.toLowerCase().trim();
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        if (!themeParameter.equals("light") && !themeParameter.equals("dark") && !themeParameter.equals("rainbow")) {
            throw new CommandException(MESSAGE_INVALID_THEME);
        }

        // Return the new CommandResult passing the theme string!
        return new CommandResult(String.format(MESSAGE_SUCCESS, themeParameter), themeParameter);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof ThemeCommand)) {
            return false;
        }
        ThemeCommand e = (ThemeCommand) other;
        return themeParameter.equals(e.themeParameter);
    }
}
