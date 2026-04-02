package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;

/**
 * Deletes a person identified by index or name from the address book.
 */
public class DeleteContactCommand extends Command implements UndoableCommand {

    public static final String COMMAND_WORD = "contact delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the contact identified by index or name.\n"
            + "Parameters (by Index): INDEX (must be a positive integer)\n"
            + "Parameters (by Name): " + PREFIX_NAME + "NAME\n"
            + "Example 1: " + COMMAND_WORD + " 1\n"
            + "Example 2: " + COMMAND_WORD + " " + PREFIX_NAME + "bob";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Contact deleted: %1$s";
    public static final String MESSAGE_PERSON_NOT_FOUND = "Error: Name not found";
    public static final String MESSAGE_DELETE_CONFIRMATION =
            "%1$s\nAre you sure you want to delete %2$s? (y/n)";

    private final Index targetIndex;
    private final Name targetName;
    private final boolean useUserProfile;
    private Person deletedPerson;

    /**
     * @param targetIndex    index of the contact to delete, or null if using name/profile
     * @param targetName     name of the contact to delete, or null if using index/profile
     * @param useUserProfile true if targeting the user profile via index 0
     */
    public DeleteContactCommand(Index targetIndex, Name targetName, boolean useUserProfile) {
        this.targetIndex = targetIndex;
        this.targetName = targetName;
        this.useUserProfile = useUserProfile;
    }

    public void setDeletedPerson(Person person) {
        this.deletedPerson = person;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        Person personToDelete;

        if (useUserProfile) {
            personToDelete = model.getUserProfile()
                    .orElseThrow(() -> new CommandException("No user profile found."));
        } else {
            List<Person> lastShownList = model.getFilteredPersonList();
            if (targetIndex != null) {
                if (targetIndex.getZeroBased() >= lastShownList.size()) {
                    throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
                }
                personToDelete = lastShownList.get(targetIndex.getZeroBased());
            } else if (targetName != null) {
                personToDelete = lastShownList.stream()
                        .filter(p -> p.getName().fullName.equalsIgnoreCase(targetName.fullName))
                        .findFirst()
                        .orElseThrow(() -> new CommandException(MESSAGE_PERSON_NOT_FOUND));
            } else {
                throw new CommandException(MESSAGE_PERSON_NOT_FOUND);
            }
        }

        String confirmationMessage = String.format(MESSAGE_DELETE_CONFIRMATION,
                Messages.format(personToDelete), personToDelete.getName());
        return new CommandResult(confirmationMessage, personToDelete);
    }

    @Override
    public void undo(Model model) {
        model.addPerson(deletedPerson);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof DeleteContactCommand)) {
            return false;
        }

        DeleteContactCommand otherDeleteCommand = (DeleteContactCommand) other;

        boolean isSameIndex = (targetIndex == null && otherDeleteCommand.targetIndex == null)
                || (targetIndex != null && targetIndex.equals(otherDeleteCommand.targetIndex));
        boolean isSameName = (targetName == null && otherDeleteCommand.targetName == null)
                || (targetName != null && targetName.equals(otherDeleteCommand.targetName));

        return isSameIndex && isSameName && useUserProfile == otherDeleteCommand.useUserProfile;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .add("targetName", targetName)
                .toString();
    }
}
