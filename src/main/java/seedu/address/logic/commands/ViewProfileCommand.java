package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Displays the user profile in the contact list.
 */
public class ViewProfileCommand extends Command {

    public static final String COMMAND_WORD = "profile view";
    public static final String MESSAGE_SUCCESS = "Displaying your profile.";
    public static final String MESSAGE_NO_PROFILE = "No user profile found.";

    @Override
    public boolean equals(Object other) {
        return other instanceof ViewProfileCommand;
    }

    @Override
    public int hashCode() {
        return ViewProfileCommand.class.hashCode();
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        if (model.getUserProfile().isEmpty()) {
            throw new CommandException(MESSAGE_NO_PROFILE);
        }
        model.updateFilteredPersonList(p -> p.isUserProfile());
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
