package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;

/**
 * Deletes a person identified by name from the address book.
 */
public class DeleteContactCommand extends Command implements UndoableCommand {

    public static final String COMMAND_WORD = "contact delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the contact with the specified name.\n"
            + "Parameters: " + PREFIX_NAME + "NAME\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_NAME + "bob";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Contact deleted: %1$s";
    public static final String MESSAGE_PERSON_NOT_FOUND = "Error: Name not found";
    public static final String MESSAGE_DELETE_CONFIRMATION =
            "%1$s\nAre you sure you want to delete %2$s? (y/n)";

    private final Name targetName;
    private Person deletedPerson;

    public DeleteContactCommand(Name targetName) {
        this.targetName = targetName;
    }

    public void setDeletedPerson(Person person) {
        this.deletedPerson = person;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Person personToDelete = model.getFilteredPersonList().stream()
                .filter(p -> p.getName().equals(targetName))
                .findFirst()
                .orElseThrow(() -> new CommandException(MESSAGE_PERSON_NOT_FOUND));

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
        return targetName.equals(otherDeleteCommand.targetName);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetName", targetName)
                .toString();
    }
}
