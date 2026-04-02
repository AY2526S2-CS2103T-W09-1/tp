package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.HashSet;
import java.util.List;
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
public class DeleteGameCommand extends Command implements UndoableCommand {

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
    private Person personBeforeEdit;
    private Person personAfterEdit;
    private final boolean useUserProfile;

    /**
     * @param targetIndex    the index of the person.
     * @param targetName     of the person in the filtered person list to edit.
     * @param gameToDelete   the game to remove from the person.
     * @param useUserProfile true if targeting the user profile via index 0.
     */
    public DeleteGameCommand(Index targetIndex, Name targetName, Game gameToDelete, boolean useUserProfile) {
        requireNonNull(gameToDelete);
        this.targetIndex = targetIndex;
        this.targetName = targetName;
        this.gameToDelete = gameToDelete;
        this.useUserProfile = useUserProfile;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        Person personToEdit;

        if (useUserProfile) {
            personToEdit = model.getUserProfile()
                    .orElseThrow(() -> new CommandException("No user profile found."));
        } else {
            List<Person> lastShownList = model.getFilteredPersonList();
            if (targetIndex != null) {
                if (targetIndex.getZeroBased() >= lastShownList.size()) {
                    throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
                }
                personToEdit = lastShownList.get(targetIndex.getZeroBased());
            } else if (targetName != null) {
                personToEdit = lastShownList.stream()
                        .filter(person -> person.getName().fullName.equalsIgnoreCase(targetName.fullName))
                        .findFirst()
                        .orElseThrow(() -> new CommandException(MESSAGE_CONTACT_NOT_FOUND));
            } else {
                throw new CommandException(MESSAGE_CONTACT_NOT_FOUND);
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
                personToEdit.getName(), personToEdit.getTags(), updatedGames, personToEdit.isUserProfile());

        personBeforeEdit = personToEdit;
        personAfterEdit = editedPerson;
        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        return new CommandResult(String.format(MESSAGE_SUCCESS,
                gameToDelete.gameName,
                editedPerson.getName().fullName),
                false,
                false,
                editedPerson);
    }

    @Override
    public void undo(Model model) {
        model.setPerson(personAfterEdit, personBeforeEdit);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
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

        return isSameIndex && isSameName && gameToDelete.equals(e.gameToDelete) && useUserProfile == e.useUserProfile;
    }
}
