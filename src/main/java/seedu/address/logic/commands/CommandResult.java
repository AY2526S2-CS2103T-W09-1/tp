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

    /** Help information should be shown to the user. */
    private final boolean isShowHelp;

    /** The application should exit. */
    private final boolean isExit;

    /** A delete confirmation is required before proceeding. */
    private final boolean isAwaitingConfirmation;

    /** The person pending deletion confirmation, or null if not applicable. */
    private final Person pendingPerson;

    /** A clear confirmation is required before proceeding. */
    private final boolean isAwaitingClearConfirmation;

    /** The theme to switch to, or null if not applicable. */
    private final String themeToSwitch;

    /** The profile/contact to be viewed in the UI, or null if not applicable. */
    private final Person personToView;

    /** Whether the ViewPanel should be cleared. */
    private final boolean shouldClearView;

    /** Private master constructor. */
    private CommandResult(String feedbackToUser, boolean isShowHelp, boolean isExit,
                          boolean isAwaitingConfirmation, boolean isAwaitingClearConfirmation,
                          Person pendingPerson, String themeToSwitch, Person personToView,
                          boolean shouldClearView) {
        this.feedbackToUser = requireNonNull(feedbackToUser);
        this.isShowHelp = isShowHelp;
        this.isExit = isExit;
        this.isAwaitingConfirmation = isAwaitingConfirmation;
        this.isAwaitingClearConfirmation = isAwaitingClearConfirmation;
        this.pendingPerson = pendingPerson;
        this.themeToSwitch = themeToSwitch;
        this.personToView = personToView;
        this.shouldClearView = shouldClearView;
    }

    /** Constructs a {@code CommandResult} with the specified fields. */
    public CommandResult(String feedbackToUser, boolean showHelp, boolean exit, Person personToView) {
        this(feedbackToUser, showHelp, exit, false, false, null, null, personToView, false);
    }

    /** Constructs a {@code CommandResult} that signals the ViewPanel should be cleared. */
    public CommandResult(String feedbackToUser, boolean shouldClearView) {
        this(feedbackToUser, false, false, false, false, null, null, null, shouldClearView);
    }

    /** Constructs a {@code CommandResult} for commands that require help or exit. */
    public CommandResult(String feedbackToUser, boolean showHelp, boolean exit) {
        this(feedbackToUser, showHelp, exit, null);
    }

    /** Constructs a {@code CommandResult} for commands that require a theme switch. */
    public CommandResult(String feedbackToUser, String themeToSwitch) {
        this(feedbackToUser, false, false, false, false, null, themeToSwitch, null, false);
    }

    /** Constructs a {@code CommandResult} with default field values. */
    public CommandResult(String feedbackToUser) {
        this(feedbackToUser, false, false, null);
    }

    /** Constructs a {@code CommandResult} that awaits deletion confirmation for the given person. */
    public CommandResult(String feedbackToUser, Person pendingPerson) {
        this(feedbackToUser, false, false, true, false, requireNonNull(pendingPerson), null, pendingPerson, false);
    }

    /** Returns a {@code CommandResult} that awaits clear confirmation. */
    public static CommandResult awaitingClearConfirmation(String feedbackToUser) {
        return new CommandResult(feedbackToUser, false, false, false, true, null, null, null, false);
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

    public boolean isAwaitingClearConfirmation() {
        return isAwaitingClearConfirmation;
    }

    public Person getPendingPerson() {
        return pendingPerson;
    }

    public String getThemeToSwitch() {
        return themeToSwitch;
    }

    public Person getViewedPerson() {
        return personToView;
    }

    public boolean isClearView() {
        return shouldClearView;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof CommandResult)) {
            return false;
        }

        CommandResult otherCommandResult = (CommandResult) other;
        return feedbackToUser.equals(otherCommandResult.feedbackToUser)
                && isShowHelp == otherCommandResult.isShowHelp
                && isExit == otherCommandResult.isExit
                && isAwaitingConfirmation == otherCommandResult.isAwaitingConfirmation
                && isAwaitingClearConfirmation == otherCommandResult.isAwaitingClearConfirmation
                && Objects.equals(themeToSwitch, otherCommandResult.themeToSwitch)
                && Objects.equals(pendingPerson, otherCommandResult.pendingPerson)
                && Objects.equals(personToView, otherCommandResult.personToView);
    }

    @Override
    public int hashCode() {
        return Objects.hash(feedbackToUser, isShowHelp, isExit, isAwaitingConfirmation,
                isAwaitingClearConfirmation, themeToSwitch, pendingPerson, personToView);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("feedbackToUser", feedbackToUser)
                .add("showHelp", isShowHelp)
                .add("exit", isExit)
                .add("awaitingConfirmation", isAwaitingConfirmation)
                .add("awaitingClearConfirmation", isAwaitingClearConfirmation)
                .add("themeToSwitch", themeToSwitch)
                .add("pendingPerson", pendingPerson)
                .add("personToView", personToView)
                .toString();
    }

}
