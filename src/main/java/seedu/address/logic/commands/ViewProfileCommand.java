package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;

/**
 * Displays the user profile in the contact list.
 */
public class ViewProfileCommand extends Command {

    public static final String COMMAND_WORD = "profile view";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Views your own profile, or the full details of a specific contact.\n"
            + "To view your profile: " + COMMAND_WORD + "\n"
            + "To view a contact (by Index): " + COMMAND_WORD + " INDEX\n"
            + "To view a contact (by Name): " + COMMAND_WORD + " " + PREFIX_NAME + "CONTACT_NAME\n"
            + "Example 1: " + COMMAND_WORD + " 1\n"
            + "Example 2: " + COMMAND_WORD + " " + PREFIX_NAME + "Zi Xuan";

    public static final String MESSAGE_SUCCESS_SELF = "Displaying your profile.";
    public static final String MESSAGE_NO_PROFILE = "No user profile found.";
    public static final String MESSAGE_SUCCESS_CONTACT = "Viewing Contact: %1$s";
    public static final String MESSAGE_PERSON_NOT_FOUND = "Error: Contact does not exist.";

    private final Index targetIndex;
    private final Name targetName;

    /**
     * Constructor for viewing a specific contact or self.
     */
    public ViewProfileCommand(Index targetIndex, Name targetName) {
        this.targetIndex = targetIndex;
        this.targetName = targetName;
    }

    /**
     * Default constructor for viewing self
     */
    public ViewProfileCommand() {
        this(null, null);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof ViewProfileCommand)) {
            return false;
        }
        ViewProfileCommand e = (ViewProfileCommand) other;

        boolean isSameIndex = (targetIndex == null && e.targetIndex == null)
                || (targetIndex != null && targetIndex.equals(e.targetIndex));
        boolean isSameName = (targetName == null && e.targetName == null)
                || (targetName != null && targetName.equals(e.targetName));

        return isSameIndex && isSameName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(targetIndex, targetName);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        // VIEW SELF
        if (targetIndex == null && targetName == null) {
            if (model.getUserProfile().isEmpty()) {
                throw new CommandException(MESSAGE_NO_PROFILE);
            }
            model.updateFilteredPersonList(Person::isUserProfile);
            Person userProfile = model.getUserProfile().get();
            return new CommandResult(MESSAGE_SUCCESS_SELF, false, false, userProfile);
        }
        // VIEW CONTACT
        List<Person> lastShownList = model.getFilteredPersonList();
        Person personToView = null;

        if (targetIndex != null) {
            if (targetIndex.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }
            personToView = lastShownList.get(targetIndex.getZeroBased());
        } else if (targetName != null) {
            Optional<Person> personOptional = lastShownList.stream()
                    .filter(person -> person.getName().fullName.equalsIgnoreCase(targetName.fullName))
                    .findFirst();

            if (personOptional.isEmpty()) {
                throw new CommandException(MESSAGE_PERSON_NOT_FOUND);
            }
            personToView = personOptional.get();
        }

        if (personToView == null) {
            throw new CommandException(MESSAGE_PERSON_NOT_FOUND);
        }

        return new CommandResult(
                String.format(MESSAGE_SUCCESS_CONTACT, personToView.getName().fullName),
                false, false, personToView);
    }
}
