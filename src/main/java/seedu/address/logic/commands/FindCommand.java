package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Finds and lists all persons in address book matching the given predicate.
 * Supports search by name, game, or alias.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all persons matching ALL the given search "
            + "criteria (case-insensitive) and displays them as a list with index numbers.\n"
            + "Parameters: [KEYWORD MORE_KEYWORDS...] [g/GAME_NAME] [al/ALIAS]\n"
            + "At least one constraint must be provided. Multiple constraints are combined with AND logic.\n"
            + "Example: " + COMMAND_WORD + " alice pauline\n"
            + "Example: " + COMMAND_WORD + " g/Valorant\n"
            + "Example: " + COMMAND_WORD + " al/BenJumpin\n"
            + "Example: " + COMMAND_WORD + " alice g/Valorant\n"
            + "Example: " + COMMAND_WORD + " alice g/Valorant al/BenJumpin";

    private final Predicate<Person> predicate;

    public FindCommand(Predicate<Person> predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(predicate);
        return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, model.getFilteredPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof FindCommand)) {
            return false;
        }

        FindCommand otherFindCommand = (FindCommand) other;
        return predicate.equals(otherFindCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
