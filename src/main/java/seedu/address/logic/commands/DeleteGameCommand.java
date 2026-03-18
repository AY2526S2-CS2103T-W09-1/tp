package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
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
            + ": Deletes a game from a contact using either their index OR their full name.\n"
            + "Parameters (by Index): INDEX (must be a positive integer) " + PREFIX_GAME + "GAME_NAME\n"
            + "Parameters (by Name): " + PREFIX_NAME + "CONTACT_NAME " + PREFIX_GAME + "GAME_NAME\n"
            + "Example 1: game " + COMMAND_WORD + " 1 " + PREFIX_GAME + "Minecraft\n"
            + "Example 2: game " + COMMAND_WORD + " " + PREFIX_NAME + "Zi Xuan " + PREFIX_GAME + "Minecraft";

    public static final String MESSAGE_SUCCESS = "Game %1$s removed from %2$s";
    public static final String MESSAGE_CONTACT_NOT_FOUND = "Error: Contact does not exist.";
    public static final String MESSAGE_GAME_NOT_FOUND = "Error: This contact does not have this game.";

    private final Index targetIndex;
    private final Name targetName;
    private final Game gameToDelete;

    /**
     * @param targetIndex the index of the person.
     * @param targetName of the person in the filtered person list to edit.
     * @param gameToDelete the game to remove from the person.
     */
    public DeleteGameCommand(Index targetIndex, Name targetName, Game gameToDelete) {
        requireNonNull(gameToDelete);
        this.targetIndex = targetIndex;
        this.targetName = targetName;
        this.gameToDelete = gameToDelete;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();
        Person personToEdit = null;

        // 1. Find the target person using either Index OR Name
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

        // Check if they actually have the game
        if (!personToEdit.getGames().contains(gameToDelete)) {
            throw new CommandException(MESSAGE_GAME_NOT_FOUND);
        }

        // Create a new Set of games and remove the target game
        Set<Game> updatedGames = new HashSet<>(personToEdit.getGames());
        updatedGames.remove(gameToDelete);

        // Create a copy of the person with the updated games
        Person editedPerson = new Person(
                personToEdit.getName(), personToEdit.getTags(), updatedGames);

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

        // Null-safe checks for both index and name
        boolean isSameIndex = (targetIndex == null && e.targetIndex == null)
                || (targetIndex != null && targetIndex.equals(e.targetIndex));
        boolean isSameName = (targetName == null && e.targetName == null)
                || (targetName != null && targetName.equals(e.targetName));

        return isSameIndex && isSameName && gameToDelete.equals(e.gameToDelete);
    }
}
