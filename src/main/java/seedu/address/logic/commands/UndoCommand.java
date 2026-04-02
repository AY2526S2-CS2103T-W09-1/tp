package seedu.address.logic.commands;

import java.util.Deque;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Reverses the most recent undoable command.
 */
public class UndoCommand extends Command {

    public static final String COMMAND_WORD = "undo";
    public static final String MESSAGE_SUCCESS = "Undo successful.";
    public static final String MESSAGE_NOTHING_TO_UNDO = "Error: Nothing to undo.";

    private final Deque<UndoableCommand> commandHistory;

    /**
     * Creates an UndoCommand that operates on the given {@code commandHistory}.
     */
    public UndoCommand(Deque<UndoableCommand> commandHistory) {
        this.commandHistory = commandHistory;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        if (commandHistory.isEmpty()) {
            throw new CommandException(MESSAGE_NOTHING_TO_UNDO);
        }
        commandHistory.pop().undo(model);
        return new CommandResult(MESSAGE_SUCCESS, true);
    }
}
