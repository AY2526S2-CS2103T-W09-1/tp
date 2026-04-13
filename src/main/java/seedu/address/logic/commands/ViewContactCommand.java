package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

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
 * Displays the full details of a specific contact or the user profile.
 */
public class ViewContactCommand extends Command {

    public static final String COMMAND_WORD = "view";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Views the full details of a specific contact. Use 'me' to view your own profile.\n"
            + "Parameters (by Index): INDEX (must be a positive integer)\n"
            + "Parameters (by Name): " + PREFIX_NAME + "CONTACT_NAME\n"
            + "Parameters (User Profile): me\n"
            + "Example 1: " + COMMAND_WORD + " 1\n"
            + "Example 2: " + COMMAND_WORD + " me";

    public static final String MESSAGE_SUCCESS_SELF = "Displaying your profile.";
    public static final String MESSAGE_NO_PROFILE = "No user profile found.";
    public static final String MESSAGE_SUCCESS_CONTACT = "Viewing Contact: %1$s";
    public static final String MESSAGE_PERSON_NOT_FOUND = "Error: Contact not found in the current list."
            + " Use 'list' to show all contacts.";

    private final Index targetIndex;
    private final Name targetName;
    private final boolean useUserProfile;

    /**
     * Constructor for viewing a specific contact or self.
     */
    public ViewContactCommand(Index targetIndex, Name targetName, boolean useUserProfile) {
        this.targetIndex = targetIndex;
        this.targetName = targetName;
        this.useUserProfile = useUserProfile;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof ViewContactCommand)) {
            return false;
        }
        ViewContactCommand e = (ViewContactCommand) other;

        boolean isSameIndex = (targetIndex == null && e.targetIndex == null)
                || (targetIndex != null && targetIndex.equals(e.targetIndex));
        boolean isSameName = (targetName == null && e.targetName == null)
                || (targetName != null && targetName.equals(e.targetName));

        return isSameIndex && isSameName && useUserProfile == e.useUserProfile;
    }

    @Override
    public int hashCode() {
        return Objects.hash(targetIndex, targetName, useUserProfile);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        // VIEW SELF
        if (useUserProfile) {
            if (model.getUserProfile().isEmpty()) {
                throw new CommandException(MESSAGE_NO_PROFILE);
            }
            model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
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

        String contactText = String.format(MESSAGE_SUCCESS_CONTACT, personToView.getName().fullName);
        return new CommandResult(contactText, false, false, personToView);
    }
}
