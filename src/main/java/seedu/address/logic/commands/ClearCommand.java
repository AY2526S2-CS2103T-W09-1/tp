package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Optional;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Person;

/**
 * Clears the address book.
 */
public class ClearCommand extends Command implements UndoableCommand {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "contact list has been cleared!";
    public static final String MESSAGE_CONFIRMATION = "Are you sure you want to clear all contacts? (y/n)";
    public static final String MESSAGE_CANCELLED = "Clear cancelled.";

    private ReadOnlyAddressBook previousAddressBook;

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        return CommandResult.awaitingClearConfirmation(MESSAGE_CONFIRMATION);
    }

    /**
     * Performs the actual clear operation after confirmation.
     */
    public CommandResult executeConfirmed(Model model) {
        requireNonNull(model);
        previousAddressBook = new AddressBook(model.getAddressBook());
        Optional<Person> userProfile = model.getUserProfile();
        model.setAddressBook(new AddressBook());
        userProfile.ifPresent(model::addPerson);
        return new CommandResult(MESSAGE_SUCCESS);
    }

    @Override
    public void undo(Model model) {
        model.setAddressBook(previousAddressBook);
    }
}
