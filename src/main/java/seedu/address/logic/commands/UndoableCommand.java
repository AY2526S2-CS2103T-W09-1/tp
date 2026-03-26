package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Represents a command that can be undone after execution.
 */
public interface UndoableCommand {

    /**
     * Reverses the effect of the command on the given {@code model}.
     *
     * @throws CommandException if the undo operation fails.
     */
    void undo(Model model) throws CommandException;
}
