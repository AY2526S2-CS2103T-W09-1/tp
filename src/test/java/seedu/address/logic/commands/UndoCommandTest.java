package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.ArrayDeque;
import java.util.Deque;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;

public class UndoCommandTest {

    @Test
    public void execute_emptyHistory_throwsCommandException() {
        Deque<UndoableCommand> emptyHistory = new ArrayDeque<>();
        UndoCommand undoCommand = new UndoCommand(emptyHistory);
        Model model = new ModelManager();

        assertThrows(CommandException.class, UndoCommand.MESSAGE_NOTHING_TO_UNDO, () ->
                undoCommand.execute(model));
    }

    @Test
    public void execute_nonEmptyHistory_callsUndoOnLatestCommand() throws Exception {
        Model model = new ModelManager();
        UndoCallTracker tracker = new UndoCallTracker();

        Deque<UndoableCommand> history = new ArrayDeque<>();
        history.push(tracker);

        UndoCommand undoCommand = new UndoCommand(history);
        CommandResult result = undoCommand.execute(model);

        assertEquals(UndoCommand.MESSAGE_SUCCESS, result.getFeedbackToUser());
        assertEquals(1, tracker.getUndoCallCount());
        assertEquals(0, history.size());
    }

    @Test
    public void execute_multipleCommandsInHistory_undosMostRecentFirst() throws Exception {
        Model model = new ModelManager();
        UndoCallTracker first = new UndoCallTracker();
        UndoCallTracker second = new UndoCallTracker();

        Deque<UndoableCommand> history = new ArrayDeque<>();
        history.push(first);
        history.push(second);

        new UndoCommand(history).execute(model);

        assertEquals(1, second.getUndoCallCount());
        assertEquals(0, first.getUndoCallCount());
        assertEquals(1, history.size());
    }

    /**
     * A stub UndoableCommand that tracks how many times undo() was called.
     */
    private static class UndoCallTracker implements UndoableCommand {
        private int undoCallCount = 0;

        public int getUndoCallCount() {
            return undoCallCount;
        }

        @Override
        public void undo(Model model) {
            undoCallCount++;
        }
    }
}
