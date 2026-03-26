package seedu.address.ui;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * The UI component that is responsible for receiving user command inputs.
 */
public class CommandBox extends UiPart<Region> {

    public static final String ERROR_STYLE_CLASS = "error";
    private static final String FXML = "CommandBox.fxml";

    private final CommandExecutor commandExecutor;

    // --- History Tracking Variables ---
    private final List<String> commandHistory = new ArrayList<>();
    private int historyPointer = 0;

    @FXML
    private TextField commandTextField;

    /**
     * Creates a {@code CommandBox} with the given {@code CommandExecutor}.
     */
    public CommandBox(CommandExecutor commandExecutor) {
        super(FXML);
        this.commandExecutor = commandExecutor;
        // calls #setStyleToDefault() whenever there is a change to the text of the command box.
        commandTextField.textProperty().addListener((unused1, unused2, unused3) -> setStyleToDefault());

        // Add a listener to handle Up and Down arrow key presses
        commandTextField.setOnKeyPressed(this::handleKeyPress);
    }

    /**
     * Handles the Enter button pressed event.
     */
    @FXML
    private void handleCommandEntered() {
        String commandText = commandTextField.getText();
        if (commandText.equals("")) {
            return;
        }

        // Add the command to history and reset the pointer to the end
        commandHistory.add(commandText);
        historyPointer = commandHistory.size();

        try {
            commandExecutor.execute(commandText);
            commandTextField.setText("");
        } catch (CommandException | ParseException e) {
            setStyleToIndicateCommandFailure();
        }
    }

    /**
     * Handles Up and Down arrow key presses for navigating command history.
     */
    private void handleKeyPress(KeyEvent event) {
        if (commandHistory.isEmpty()) {
            return;
        }

        if (event.getCode() == KeyCode.UP) {
            // Move pointer up, but not past the first command
            if (historyPointer > 0) {
                historyPointer--;
                setCommandTextFromHistory();
            }
            event.consume(); // Prevents the default cursor movement

        } else if (event.getCode() == KeyCode.DOWN) {
            // Move pointer down
            if (historyPointer < commandHistory.size() - 1) {
                historyPointer++;
                setCommandTextFromHistory();
            } else if (historyPointer == commandHistory.size() - 1) {
                // If we are at the last command and press down, clear the text box
                historyPointer++;
                commandTextField.setText("");
            }
            event.consume(); // Prevents the default cursor movement
        }
    }

    /**
     * Sets the text field with the command from history and moves the cursor to the end.
     */
    private void setCommandTextFromHistory() {
        commandTextField.setText(commandHistory.get(historyPointer));
        commandTextField.positionCaret(commandTextField.getText().length());
    }

    /**
     * Sets the command box style to use the default style.
     */
    private void setStyleToDefault() {
        commandTextField.getStyleClass().remove(ERROR_STYLE_CLASS);
    }

    /**
     * Sets the command box style to indicate a failed command.
     */
    private void setStyleToIndicateCommandFailure() {
        ObservableList<String> styleClass = commandTextField.getStyleClass();

        if (styleClass.contains(ERROR_STYLE_CLASS)) {
            return;
        }

        styleClass.add(ERROR_STYLE_CLASS);
    }

    /**
     * Represents a function that can execute commands.
     */
    @FunctionalInterface
    public interface CommandExecutor {
        /**
         * Executes the command and returns the result.
         *
         * @see seedu.address.logic.Logic#execute(String)
         */
        CommandResult execute(String commandText) throws CommandException, ParseException;
    }

}
