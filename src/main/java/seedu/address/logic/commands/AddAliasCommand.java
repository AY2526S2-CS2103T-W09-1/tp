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
 * Adds an alias to a game of an existing person in Harmony.
 */
public class AddAliasCommand extends Command implements UndoableCommand {

    public static final String COMMAND_WORD = "alias add";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Adds an alias to a game of a contact using either their index OR their full name.\n"
            + "Parameters (by Index): INDEX (must be a positive integer) "
            + PREFIX_GAME + "GAME " + PREFIX_ALIAS + "ALIAS\n"
            + "Parameters (by Name): "
            + PREFIX_NAME + "CONTACT_NAME " + PREFIX_GAME + "GAME " + PREFIX_ALIAS + "ALIAS\n"
            + "Example 1: "
            + COMMAND_WORD + " 1 " + PREFIX_GAME + "Valorant " + PREFIX_ALIAS + "Benjumpin\n"
            + "Example 2: "
            + COMMAND_WORD + " " + PREFIX_NAME + "Benjamin " + PREFIX_GAME + "Valorant " + PREFIX_ALIAS + "Benjumpin";

    public static final String MESSAGE_SUCCESS = "Alias '%3$s' added to %1$s's game: %2$s";
    public static final String MESSAGE_PERSON_NOT_FOUND = "Error: Contact does not exist.";
    public static final String MESSAGE_ALIAS_NOT_FOUND = "Error: Alias does not exist for this contact";
    public static final String MESSAGE_GAME_NOT_FOUND = "Error: This contact does not have this game.";
    public static final String MESSAGE_DUPLICATE_ALIAS = "Error: This alias already exists for this game.";

    private final Index targetIndex;
    private final Name targetName;
    private final Game targetGame;
    private final Alias aliasToAdd;
    private final boolean useUserProfile;
    private Person personBeforeEdit;
    private Person personAfterEdit;

    /**
     * Creates a AddAliasCommand to add {@code alias} to the person.
     */
    public AddAliasCommand(Index targetIndex, Name targetName, Game game, Alias alias, boolean useUserProfile) {
        requireNonNull(game);
        requireNonNull(alias);
        this.targetIndex = targetIndex;
        this.targetName = targetName;
        this.targetGame = game;
        this.aliasToAdd = alias;
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

        // 2. Find the game
        Optional<Game> gameOptional = personToEdit.getGames().stream()
                .filter(game -> game.equals(targetGame))
                .findFirst();

        if (gameOptional.isEmpty()) {
            throw new CommandException(MESSAGE_GAME_NOT_FOUND);
        }

        Game gameToEdit = gameOptional.get();

        // 3. Check for duplicate alias
        if (gameToEdit.getAliases().contains(aliasToAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_ALIAS);
        }

        // 4. Update alias set and game set
        Set<Alias> updatedAliases = new HashSet<>(gameToEdit.getAliases());
        updatedAliases.add(aliasToAdd);
        Game updatedGame = new Game(gameToEdit.gameName, updatedAliases);

        Set<Game> updatedGames = new HashSet<>(personToEdit.getGames());
        updatedGames.remove(gameToEdit);
        updatedGames.add(updatedGame);

        Person editedPerson = new Person(
                personToEdit.getName(),
                personToEdit.getTags(),
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
                aliasToAdd),
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

        if (!(other instanceof AddAliasCommand)) {
            return false;
        }

        AddAliasCommand e = (AddAliasCommand) other;

        // Null-safe checks
        boolean isSameIndex = (targetIndex == null && e.targetIndex == null)
                || (targetIndex != null && targetIndex.equals(e.targetIndex));
        boolean isSameName = (targetName == null && e.targetName == null)
                || (targetName != null && targetName.equals(e.targetName));

        return isSameIndex && isSameName
                && targetGame.equals(e.targetGame)
                && aliasToAdd.equals(e.aliasToAdd)
                && useUserProfile == e.useUserProfile;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .add("targetName", targetName)
                .add("targetGame", targetGame)
                .add("aliasToAdd", aliasToAdd)
                .toString();
    }
}
