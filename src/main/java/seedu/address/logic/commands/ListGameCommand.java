package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.game.Game;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;

/**
 * Lists all games associated with a specific contact.
 */
public class ListGameCommand extends Command {

    public static final String COMMAND_WORD = "list";
    public static final String MESSAGE_USAGE = "game " + COMMAND_WORD
            + ": Lists all games played by the specified contact.\n"
            + "Parameters: " + PREFIX_NAME + "CONTACT_NAME\n"
            + "Example: game " + COMMAND_WORD + " " + PREFIX_NAME + "Zi Xuan";

    public static final String MESSAGE_SUCCESS = "Games for %1$s: %2$s";
    public static final String MESSAGE_NO_GAMES = "%1$s currently has no games listed.";
    public static final String MESSAGE_CONTACT_NOT_FOUND = "Error: Contact does not exist.";

    private final Name targetName;

    public ListGameCommand(Name targetName) {
        this.targetName = targetName;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        // Find the person by name (case-insensitive)
        Person personToList = null;
        for (Person person : lastShownList) {
            if (person.getName().fullName.equalsIgnoreCase(targetName.fullName)) {
                personToList = person;
                break;
            }
        }

        if (personToList == null) {
            throw new CommandException(MESSAGE_CONTACT_NOT_FOUND);
        }

        Set<Game> games = personToList.getGames();

        if (games.isEmpty()) {
            return new CommandResult(String.format(MESSAGE_NO_GAMES, personToList.getName().fullName));
        }

        // Format the Set of games into a comma-separated string
        String gameListString = games.stream()
                .map(game -> game.gameName)
                .collect(Collectors.joining(", "));

        return new CommandResult(String.format(MESSAGE_SUCCESS, personToList.getName().fullName, gameListString));
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
        return targetName.equals(e.targetName);
    }
}