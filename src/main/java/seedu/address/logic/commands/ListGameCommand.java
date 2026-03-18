package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;

/**
 * Lists all games of an existing contact in Harmony.
 */
public class ListGameCommand extends Command {

    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_USAGE = "game " + COMMAND_WORD
            + ": Lists all games of a contact using either their index OR their full name.\n"
            + "Parameters (by Index): INDEX (must be a positive integer)\n"
            + "Parameters (by Name): " + PREFIX_NAME + "CONTACT_NAME\n"
            + "Example 1: game " + COMMAND_WORD + " 1\n"
            + "Example 2: game " + COMMAND_WORD + " " + PREFIX_NAME + "Zi Xuan";

    public static final String MESSAGE_SUCCESS = "%1$s's games: %2$s";
    public static final String MESSAGE_NO_GAMES = "%1$s currently has no games.";
    public static final String MESSAGE_CONTACT_NOT_FOUND = "Error: Contact does not exist.";

    private final Index targetIndex;
    private final Name targetName;

    /**
     * Creates a ListGameCommand to list all games {@code targetIndex} from the person with {@code targetName}.
     */
    public ListGameCommand(Index targetIndex, Name targetName) {
        this.targetIndex = targetIndex;
        this.targetName = targetName;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();
        Person personToEdit = null;

        // 1. Universal Find Person Block
        if (targetIndex != null) {
            if (targetIndex.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }
            personToEdit = lastShownList.get(targetIndex.getZeroBased());
        } else if (targetName != null) {
            Optional<Person> personOptional = lastShownList.stream()
                    .filter(person -> person.getName().fullName.equalsIgnoreCase(targetName.fullName))
                    .findFirst();

            if (personOptional.isEmpty()) {
                throw new CommandException(MESSAGE_CONTACT_NOT_FOUND);
            }
            personToEdit = personOptional.get();
        }

        if (personToEdit == null) {
            throw new CommandException(MESSAGE_CONTACT_NOT_FOUND);
        }

        // 2. Format and return the list of games
        if (personToEdit.getGames().isEmpty()) {
            return new CommandResult(String.format(MESSAGE_NO_GAMES, personToEdit.getName().fullName));
        }

        String gamesString = personToEdit.getGames().stream()
                .map(game -> game.gameName)
                .collect(Collectors.joining(", "));

        return new CommandResult(String.format(MESSAGE_SUCCESS, personToEdit.getName().fullName, gamesString));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof ListGameCommand)) {
            return false;
        }
        ListGameCommand e = (ListGameCommand) other;

        // 3. Null-safe checks
        boolean isSameIndex = (targetIndex == null && e.targetIndex == null)
                || (targetIndex != null && targetIndex.equals(e.targetIndex));
        boolean isSameName = (targetName == null && e.targetName == null)
                || (targetName != null && targetName.equals(e.targetName));

        return isSameIndex && isSameName;
    }
}
