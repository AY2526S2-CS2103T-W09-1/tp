package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ALIAS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.game.Game;
import seedu.address.model.person.Alias;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;

/**
 * Deletes an alias from an existing person in Harmony.
 */
public class DeleteAliasCommand extends Command implements ConfirmableDeleteCommand, UndoableCommand {

    public static final String COMMAND_WORD = "alias delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes an alias from a game of a contact using either their index, full name,"
            + " or 'me' for your own profile.\n"
            + "Parameters (by Index): INDEX (must be a positive integer) "
            + PREFIX_GAME + "GAME " + PREFIX_ALIAS + "ALIAS\n"
            + "Parameters (by Name): "
            + PREFIX_NAME + "CONTACT_NAME " + PREFIX_GAME + "GAME " + PREFIX_ALIAS + "ALIAS\n"
            + "Parameters (User Profile): me " + PREFIX_GAME + "GAME " + PREFIX_ALIAS + "ALIAS\n"
            + "Example 1: " + COMMAND_WORD + " 1 " + PREFIX_GAME + "Valorant "
            + PREFIX_ALIAS + "Benjumpin\n"
            + "Example 2: " + COMMAND_WORD + " " + PREFIX_NAME + "Benjamin "
            + PREFIX_GAME + "Valorant " + PREFIX_ALIAS + "Benjumpin\n"
            + "Example 3: " + COMMAND_WORD + " me " + PREFIX_GAME + "Valorant " + PREFIX_ALIAS + "Benjumpin";

    public static final String MESSAGE_SUCCESS = "Alias \"%3$s\" removed from %1$s in %2$s";
    public static final String MESSAGE_PERSON_NOT_FOUND = "Error: Name does not exist";
    public static final String MESSAGE_ALIAS_NOT_FOUND = "Error: Alias does not exist for this contact";
    public static final String MESSAGE_GAME_NOT_FOUND = "Error: Game does not exist for this contact";
    public static final String MESSAGE_DELETE_CONFIRMATION =
            "Are you sure you want to delete alias \"%3$s\" from %1$s in %2$s? (y/n)";
    public static final String MESSAGE_DELETE_CANCELLED =
            "Deletion of alias \"%3$s\" from %1$s in %2$s cancelled.";

    private final Index targetIndex;
    private final Name targetName;
    private final Game targetGame;
    private final Alias aliasToDelete;
    private Person personBeforeEdit;
    private Person personAfterEdit;
    private final boolean useUserProfile;

    /**
     * Creates a DeleteAliasCommand to remove {@code alias} from the person.
     */
    public DeleteAliasCommand(Index targetIndex, Name targetName, Game game, Alias alias, boolean useUserProfile) {
        requireNonNull(game);
        requireNonNull(alias);
        this.targetIndex = targetIndex;
        this.targetName = targetName;
        this.targetGame = game;
        this.aliasToDelete = alias;
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
                        .orElseThrow(() -> new CommandException(MESSAGE_PERSON_NOT_FOUND));
            } else {
                throw new CommandException(MESSAGE_PERSON_NOT_FOUND);
            }
        }

        Optional<Game> gameOptional = personToEdit.getGames().stream()
                .filter(game -> game.equals(targetGame))
                .findFirst();

        if (gameOptional.isEmpty()) {
            throw new CommandException(MESSAGE_GAME_NOT_FOUND);
        }

        Game gameToEdit = gameOptional.get();

        if (!gameToEdit.getAliases().contains(aliasToDelete)) {
            throw new CommandException(MESSAGE_ALIAS_NOT_FOUND);
        }

        Set<Alias> updatedAliases = new HashSet<>(gameToEdit.getAliases());
        updatedAliases.remove(aliasToDelete);
        Game updatedGame = new Game(gameToEdit.gameName, updatedAliases);

        Set<Game> updatedGames = new HashSet<>(personToEdit.getGames());
        updatedGames.remove(gameToEdit);
        updatedGames.add(updatedGame);

        Person editedPerson = new Person(
                personToEdit.getName(),
                updatedGames,
                personToEdit.isUserProfile()
        );

        personBeforeEdit = personToEdit;
        personAfterEdit = editedPerson;

        String confirmationMessage = String.format(MESSAGE_DELETE_CONFIRMATION,
                personToEdit.getName(), targetGame.gameName, aliasToDelete);
        return new CommandResult(confirmationMessage, personToEdit);
    }

    @Override
    public String getCancelMessage() {
        return String.format(MESSAGE_DELETE_CANCELLED,
                personBeforeEdit.getName(), targetGame.gameName, aliasToDelete);
    }

    /**
     * Performs the actual alias deletion after confirmation.
     */
    public CommandResult performDeletion(Model model) {
        model.setPerson(personBeforeEdit, personAfterEdit);
        return new CommandResult(String.format(MESSAGE_SUCCESS,
                personAfterEdit.getName(), targetGame.gameName, aliasToDelete),
                false, false, personAfterEdit);
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
        if (!(other instanceof DeleteAliasCommand)) {
            return false;
        }
        DeleteAliasCommand e = (DeleteAliasCommand) other;

        boolean isSameIndex = (targetIndex == null && e.targetIndex == null)
                || (targetIndex != null && targetIndex.equals(e.targetIndex));
        boolean isSameName = (targetName == null && e.targetName == null)
                || (targetName != null && targetName.equals(e.targetName));

        return isSameIndex && isSameName
                && targetGame.equals(e.targetGame)
                && aliasToDelete.equals(e.aliasToDelete)
                && useUserProfile == e.useUserProfile;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .add("targetName", targetName)
                .add("targetGame", targetGame)
                .add("aliasToDelete", aliasToDelete)
                .toString();
    }
}
