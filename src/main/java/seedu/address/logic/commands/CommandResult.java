package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.person.Person;

/**
 * Represents the result of a command execution.
 */
public class CommandResult {

    private final String feedbackToUser;

    /**
     * Help information should be shown to the user.
     */
    private final boolean isShowHelp;

    /**
     * The application should exit.
     */
    private final boolean isExit;

    /**
     * A confirmation is required before proceeding.
     */
    private final boolean isAwaitingConfirmation;

    /**
     * The person pending deletion confirmation, or null if not applicable.
     */
    private final Person pendingPerson;

    /**
     * The profile/contact to be viewed in the UI, or null if not applicable.
     * */
    private final Person personToView;

    /**
     * Constructs a {@code CommandResult} with the specified fields.
     */
    public CommandResult(String feedbackToUser, boolean showHelp, boolean exit, Person personToView) {
        this.feedbackToUser = requireNonNull(feedbackToUser);
        this.isShowHelp = showHelp;
        this.isExit = exit;
        this.isAwaitingConfirmation = false;
        this.pendingPerson = null;
        this.personToView = personToView;
    }

    /**
     * Constructs a {@code CommandResult} for commands that require help or exit.
     */
    public CommandResult(String feedbackToUser, boolean showHelp, boolean exit) {
        this(feedbackToUser, showHelp, exit, null);
    }

    /**
     * Constructs a {@code CommandResult} with the specified {@code feedbackToUser},
     * and other fields set to their default value.
     */
    public CommandResult(String feedbackToUser) {
        this(feedbackToUser, false, false, null);
    }

    /**
     * Constructs a {@code CommandResult} that awaits deletion confirmation for the given person.
     */
    public CommandResult(String feedbackToUser, Person pendingPerson) {
        this.feedbackToUser = requireNonNull(feedbackToUser);
        this.isShowHelp = false;
        this.isExit = false;
        this.isAwaitingConfirmation = true;
        this.pendingPerson = requireNonNull(pendingPerson);
        this.personToView = null;
    }

    public String getFeedbackToUser() {
        return feedbackToUser;
    }

    public boolean isShowHelp() {
        return isShowHelp;
    }

    public boolean isExit() {
        return isExit;
    }

    public boolean isAwaitingConfirmation() {
        return isAwaitingConfirmation;
    }

    public Person getPendingPerson() {
        return pendingPerson;
    }

    public Person getViewedPerson() {
        return personToView;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof CommandResult)) {
            return false;
        }

        CommandResult otherCommandResult = (CommandResult) other;
        return feedbackToUser.equals(otherCommandResult.feedbackToUser)
                && isShowHelp == otherCommandResult.isShowHelp
                && isExit == otherCommandResult.isExit
                && isAwaitingConfirmation == otherCommandResult.isAwaitingConfirmation
                && Objects.equals(pendingPerson, otherCommandResult.pendingPerson)
                && Objects.equals(personToView, otherCommandResult.personToView);
    }

    @Override
    public int hashCode() {
        return Objects.hash(feedbackToUser, isShowHelp, isExit, isAwaitingConfirmation, pendingPerson, personToView);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("feedbackToUser", feedbackToUser)
                .add("showHelp", isShowHelp)
                .add("exit", isExit)
                .add("awaitingConfirmation", isAwaitingConfirmation)
                .add("pendingPerson", pendingPerson)
                .add("personToView", personToView)
                .toString();
    }

}
