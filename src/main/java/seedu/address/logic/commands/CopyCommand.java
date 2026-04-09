package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ALIAS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import java.util.List;

import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.game.Game;
import seedu.address.model.person.Alias;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;

/**
 * Copies a contact's addition command to the system clipboard.
 */
public class CopyCommand extends Command {

    public static final String COMMAND_WORD = "copy";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Copies the 'contact add' command for the specified contact to the clipboard.\n"
            + "Parameters (by Index): INDEX (must be a positive integer)\n"
            + "Parameters (by Name): " + PREFIX_NAME + "NAME\n"
            + "Parameters (User Profile): me\n"
            + "Example 1: " + COMMAND_WORD + " 1\n"
            + "Example 2: " + COMMAND_WORD + " " + PREFIX_NAME + "John Doe\n"
            + "Example 3: " + COMMAND_WORD + " me";

    public static final String MESSAGE_SUCCESS = "Copied contact to clipboard: %1$s";
    public static final String MESSAGE_PERSON_NOT_FOUND = "Error: Contact not found in the current list."
            + " Use 'list' to show all contacts.";
    public static final String MESSAGE_NO_PROFILE = "No user profile found.";

    private final Index targetIndex;
    private final Name targetName;
    private final boolean useUserProfile;

    /**
     * @param targetIndex of the person in the filtered person list to copy
     * @param targetName of the person in the filtered person list to copy
     * @param useUserProfile true if targeting the user profile via 'me'
     */
    public CopyCommand(Index targetIndex, Name targetName, boolean useUserProfile) {
        this.targetIndex = targetIndex;
        this.targetName = targetName;
        this.useUserProfile = useUserProfile;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        Person personToCopy;

        if (useUserProfile) {
            personToCopy = model.getUserProfile()
                    .orElseThrow(() -> new CommandException(MESSAGE_NO_PROFILE));
        } else {
            List<Person> lastShownList = model.getFilteredPersonList();
            if (targetIndex != null) {
                if (targetIndex.getZeroBased() >= lastShownList.size()) {
                    throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
                }
                personToCopy = lastShownList.get(targetIndex.getZeroBased());
            } else {
                personToCopy = lastShownList.stream()
                        .filter(p -> p.getName().fullName.equalsIgnoreCase(targetName.fullName))
                        .findFirst()
                        .orElseThrow(() -> new CommandException(MESSAGE_PERSON_NOT_FOUND));
            }
        }

        String copyString = generateCommandString(personToCopy);
        copyToClipboard(copyString);

        return new CommandResult(String.format(MESSAGE_SUCCESS, personToCopy.getName().fullName));
    }

    /**
     * Reconstructs the exact 'contact add' command string needed to recreate the person.
     */
    private String generateCommandString(Person person) {
        StringBuilder sb = new StringBuilder();
        sb.append(AddContactCommand.COMMAND_WORD).append(" ");
        sb.append(PREFIX_NAME).append(person.getName().fullName).append(" ");

        for (Game game : person.getGames()) {
            sb.append(PREFIX_GAME).append(game.gameName).append(" ");
            for (Alias alias : game.getAliases()) {
                sb.append(PREFIX_ALIAS).append(alias.value).append(" ");
            }
        }

        return sb.toString().trim();
    }

    /**
     * Saves the generated string to the user's system clipboard.
     * Protected to allow overriding in headless unit tests.
     */
    protected void copyToClipboard(String text) {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(text);
        clipboard.setContent(content);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof CopyCommand otherCopyCommand)) {
            return false;
        }

        boolean indexEquals = (targetIndex == null && otherCopyCommand.targetIndex == null)
                || (targetIndex != null && targetIndex.equals(otherCopyCommand.targetIndex));
        boolean nameEquals = (targetName == null && otherCopyCommand.targetName == null)
                || (targetName != null && targetName.equals(otherCopyCommand.targetName));

        return indexEquals && nameEquals && useUserProfile == otherCopyCommand.useUserProfile;
    }
}
