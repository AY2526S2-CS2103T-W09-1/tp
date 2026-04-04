package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ALIAS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NEW_ALIAS;

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
 * Edits an alias of a game of an existing person in Harmony.
 */
public class EditAliasCommand extends Command implements UndoableCommand {

    public static final String COMMAND_WORD = "alias edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Edits an alias of a game of a contact using either their index, full name,"
            + " or 'me' for your own profile.\n"
            + "Parameters (by Index): INDEX (must be a positive integer) "
            + PREFIX_GAME + "GAME " + PREFIX_ALIAS + "OLD_ALIAS " + PREFIX_NEW_ALIAS + "NEW_ALIAS\n"
            + "Parameters (by Name): "
            + PREFIX_NAME + "CONTACT_NAME " + PREFIX_GAME + "GAME " + PREFIX_ALIAS + "OLD_ALIAS "
            + PREFIX_NEW_ALIAS + "NEW_ALIAS\n"
            + "Parameters (User Profile): me " + PREFIX_GAME + "GAME " + PREFIX_ALIAS + "OLD_ALIAS "
            + PREFIX_NEW_ALIAS + "NEW_ALIAS\n"
            + "Example 1: " + COMMAND_WORD + " 1 " + PREFIX_GAME + "Valorant "
            + PREFIX_ALIAS + "JohnnyV " + PREFIX_NEW_ALIAS + "JohnnyValorant\n"
            + "Example 2: " + COMMAND_WORD + " " + PREFIX_NAME + "John "
            + PREFIX_GAME + "Valorant " + PREFIX_ALIAS + "JohnnyV " + PREFIX_NEW_ALIAS + "JohnnyValorant\n"
            + "Example 3: " + COMMAND_WORD + " me " + PREFIX_GAME + "Valorant "
            + PREFIX_ALIAS + "JohnnyV " + PREFIX_NEW_ALIAS + "JohnnyValorant";

    public static final String MESSAGE_SUCCESS = "Alias \"%3$s\" updated to \"%4$s\" for %1$s in %2$s";
    public static final String MESSAGE_PERSON_NOT_FOUND = "Error: Name not found";
    public static final String MESSAGE_GAME_NOT_FOUND = "Error: Game not found";
    public static final String MESSAGE_ALIAS_NOT_FOUND = "Error: Alias not found";
    public static final String MESSAGE_DUPLICATE_ALIAS = "Error: This alias already exists for this game.";

    private final Index targetIndex;
    private final Name targetName;
    private final Game targetGame;
    private final Alias oldAlias;
    private final Alias newAlias;
    private final boolean useUserProfile;
    private Person personBeforeEdit;
    private Person personAfterEdit;

    /**
     * Creates an EditAliasCommand to update {@code oldAlias} to {@code newAlias} for the person.
     * @param targetIndex    index of the contact to edit, or null if using name/profile
     * @param targetName     current name of the contact to edit, or null if using index/profile
     * @param targetGame     the game for which the alias belongs to
     * @param oldAlias       the alias to be edited
     * @param newAlias       the new alias to update to
     * @param useUserProfile true if targeting the user profile via index 0
     */
    public EditAliasCommand(Index targetIndex, Name targetName, Game game,
                            Alias oldAlias, Alias newAlias, boolean useUserProfile) {
        requireNonNull(game);
        requireNonNull(oldAlias);
        requireNonNull(newAlias);
        this.targetIndex = targetIndex;
        this.targetName = targetName;
        this.targetGame = game;
        this.oldAlias = oldAlias;
        this.newAlias = newAlias;
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

        if (!gameToEdit.getAliases().contains(oldAlias)) {
            throw new CommandException(MESSAGE_ALIAS_NOT_FOUND);
        }

        if (gameToEdit.getAliases().contains(newAlias)) {
            throw new CommandException(MESSAGE_DUPLICATE_ALIAS);
        }

        Set<Alias> updatedAliases = new HashSet<>(gameToEdit.getAliases());
        updatedAliases.remove(oldAlias);
        updatedAliases.add(newAlias);
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
        model.setPerson(personToEdit, editedPerson);

        return new CommandResult(String.format(
                MESSAGE_SUCCESS,
                editedPerson.getName(),
                updatedGame.gameName,
                oldAlias,
                newAlias),
                false,
                false,
                editedPerson);
    }

    @Override
    public void undo(Model model) {
        model.setPerson(personAfterEdit, personBeforeEdit);
        model.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof EditAliasCommand)) {
            return false;
        }
        EditAliasCommand e = (EditAliasCommand) other;

        boolean isSameIndex = (targetIndex == null && e.targetIndex == null)
                || (targetIndex != null && targetIndex.equals(e.targetIndex));
        boolean isSameName = (targetName == null && e.targetName == null)
                || (targetName != null && targetName.equals(e.targetName));

        return isSameIndex && isSameName
                && targetGame.equals(e.targetGame)
                && oldAlias.equals(e.oldAlias)
                && newAlias.equals(e.newAlias)
                && useUserProfile == e.useUserProfile;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .add("targetName", targetName)
                .add("targetGame", targetGame)
                .add("oldAlias", oldAlias)
                .add("newAlias", newAlias)
                .toString();
    }
}
