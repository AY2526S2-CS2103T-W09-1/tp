package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ALIAS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Adds a person to the address book.
 */
public class AddContactCommand extends Command implements UndoableCommand {

    public static final String COMMAND_WORD = "contact add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a person to the contact list. "
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + "[" + PREFIX_GAME + "GAME] "
            + "[" + PREFIX_ALIAS + "ALIAS]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "John Doe "
            + PREFIX_GAME + "Valorant "
            + PREFIX_ALIAS + "JohnValo "
            + PREFIX_ALIAS + "JDog "
            + PREFIX_GAME + "Minecraft";

    public static final String MESSAGE_SUCCESS = "Contact added: %1$s";
    public static final String MESSAGE_DUPLICATE_PERSON = "Error: Contact already exists in the contact list";

    private final Person toAdd;

    /**
     * Creates an AddContactCommand to add the specified {@code Person}
     */
    public AddContactCommand(Person person) {
        requireNonNull(person);
        toAdd = person;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasPerson(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.addPerson(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(toAdd)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddContactCommand otherAddCommand)) {
            return false;
        }

        return toAdd.equals(otherAddCommand.toAdd);
    }

    @Override
    public void undo(Model model) {
        model.deletePerson(toAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toAdd", toAdd)
                .toString();
    }
}
