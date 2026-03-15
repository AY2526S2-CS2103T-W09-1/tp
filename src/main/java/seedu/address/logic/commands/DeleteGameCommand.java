package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.game.Game;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;

/**
 * Deletes a game from an existing contact in the address book.
 */
public class DeleteGameCommand extends Command {

    public static final String COMMAND_WORD = "delete";
    public static final String MESSAGE_USAGE = "game " + COMMAND_WORD
            + ": Deletes a game from the specified contact.\n"
            + "Parameters: " + PREFIX_NAME + "CONTACT_NAME " + PREFIX_GAME + "GAME_NAME\n"
            + "Example: game " + COMMAND_WORD + " " + PREFIX_NAME + "Zi Xuan " + PREFIX_GAME + "Minecraft";

    public static final String MESSAGE_SUCCESS = "Game %1$s removed from %2$s";
    public static final String MESSAGE_CONTACT_NOT_FOUND = "Error: Contact does not exist.";
    public static final String MESSAGE_GAME_NOT_FOUND = "Error: This contact does not have this game.";

    private final Name targetName;
    private final Game gameToDelete;

    /**
     * @param targetName of the person in the filtered person list to edit.
     * @param gameToDelete the game to remove from the person.
     */
    public DeleteGameCommand(Name targetName, Game gameToDelete) {
        this.targetName = targetName;
        this.gameToDelete = gameToDelete;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        // Find the person by name (case-insensitive)
        Person personToEdit = null;
        for (Person person : lastShownList) {
            if (person.getName().fullName.equalsIgnoreCase(targetName.fullName)) {
                personToEdit = person;
                break;
            }
        }

        if (personToEdit == null) {
            throw new CommandException(MESSAGE_CONTACT_NOT_FOUND);
        }

        // Check if they actually have the game
        if (!personToEdit.getGames().contains(gameToDelete)) {
            throw new CommandException(MESSAGE_GAME_NOT_FOUND);
        }

        // Create a new Set of games and remove the target game
        Set<Game> updatedGames = new HashSet<>(personToEdit.getGames());
        updatedGames.remove(gameToDelete);

        // Create a copy of the person with the updated games
        Person editedPerson = new Person(
                personToEdit.getName(), personToEdit.getPhone(), personToEdit.getEmail(),
                personToEdit.getAddress(), personToEdit.getTags(), updatedGames);

        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        return new CommandResult(String.format(MESSAGE_SUCCESS,
                gameToDelete.gameName,
                editedPerson.getName().fullName));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof DeleteGameCommand)) {
            return false;
        }
        DeleteGameCommand e = (DeleteGameCommand) other;
        return targetName.equals(e.targetName) && gameToDelete.equals(e.gameToDelete);
    }
}
