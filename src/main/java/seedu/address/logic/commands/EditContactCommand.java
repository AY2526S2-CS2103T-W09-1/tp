package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NEW_NAME;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;

/**
 * Edits the name of an existing contact in the address book.
 */
public class EditContactCommand extends Command {

    public static final String COMMAND_WORD = "contact edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Edits the name of a contact identified by index or current name.\n"
            + "Parameters (by Index): INDEX (must be a positive integer) " + PREFIX_NEW_NAME + "NEW_NAME\n"
            + "Parameters (by Name): " + PREFIX_NAME + "NAME " + PREFIX_NEW_NAME + "NEW_NAME\n"
            + "Example 1: " + COMMAND_WORD + " 1 " + PREFIX_NEW_NAME + "Jan\n"
            + "Example 2: " + COMMAND_WORD + " " + PREFIX_NAME + "Janelle " + PREFIX_NEW_NAME + "Jan";

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Contact updated: %1$s \u2192 %2$s";
    public static final String MESSAGE_PERSON_NOT_FOUND = "Error: Name not found";
    public static final String MESSAGE_DUPLICATE_PERSON = "Error: A contact with that name already exists";

    private final Index targetIndex;
    private final Name targetName;
    private final Name newName;
    private final boolean useUserProfile;

    /**
     * @param targetIndex    index of the contact to edit, or null if using name/profile
     * @param targetName     current name of the contact to edit, or null if using index/profile
     * @param newName        the new name for the contact
     * @param useUserProfile true if targeting the user profile via index 0
     */
    public EditContactCommand(Index targetIndex, Name targetName, Name newName, boolean useUserProfile) {
        requireNonNull(newName);
        this.targetIndex = targetIndex;
        this.targetName = targetName;
        this.newName = newName;
        this.useUserProfile = useUserProfile;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Person personToEdit;

        if (useUserProfile) {
            personToEdit = model.getUserProfile()
                    .orElseThrow(() -> new CommandException("No user profile found."));
        } else if (targetIndex != null) {
            List<Person> lastShownList = model.getFilteredPersonList();
            if (targetIndex.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }
            personToEdit = lastShownList.get(targetIndex.getZeroBased());
        } else {
            personToEdit = model.getFilteredPersonList().stream()
                    .filter(p -> p.getName().fullName.equalsIgnoreCase(targetName.fullName))
                    .findFirst()
                    .orElseThrow(() -> new CommandException(MESSAGE_PERSON_NOT_FOUND));
        }

        Person editedPerson = new Person(newName, personToEdit.getTags(), personToEdit.getGames(),
                personToEdit.isUserProfile());

        if (!personToEdit.isSamePerson(editedPerson) && model.hasPerson(editedPerson)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(
                String.format(MESSAGE_EDIT_PERSON_SUCCESS, personToEdit.getName(), newName),
                false, false, editedPerson);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof EditContactCommand)) {
            return false;
        }
        EditContactCommand otherCommand = (EditContactCommand) other;
        boolean sameIndex = (targetIndex == null && otherCommand.targetIndex == null)
                || (targetIndex != null && targetIndex.equals(otherCommand.targetIndex));
        boolean sameName = (targetName == null && otherCommand.targetName == null)
                || (targetName != null && targetName.equals(otherCommand.targetName));
        return sameIndex && sameName && newName.equals(otherCommand.newName)
                && useUserProfile == otherCommand.useUserProfile;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .add("targetName", targetName)
                .add("newName", newName)
                .toString();
    }
}
