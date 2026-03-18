package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ALIAS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.game.Game;
import seedu.address.model.person.Alias;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;

/**
 * Adds an alias to a game of an existing person in Harmony.
 */
public class AddAliasCommand extends Command {

    public static final String COMMAND_WORD = "alias add";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Adds an alias to a game of an existing person in Harmony. "
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_GAME + "GAME "
            + PREFIX_ALIAS + "ALIAS\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "Benjamin "
            + PREFIX_GAME + "Valorant "
            + PREFIX_ALIAS + "Benjumpin";

    public static final String MESSAGE_SUCCESS = "Alias added to %1$s in %2$s: %3$s";
    public static final String MESSAGE_PERSON_NOT_FOUND = "Error: Name does not exist";
    public static final String MESSAGE_ALIAS_NOT_FOUND = "Error: Alias does not exist for this contact";
    public static final String MESSAGE_GAME_NOT_FOUND = "Error: Game does not exist for this contact";
    public static final String MESSAGE_DUPLICATE_ALIAS = "Error: This alias already exists for this game";


    private final Name targetName;
    private final Game targetGame;
    private final Alias aliasToAdd;

    /**
     * Creates an AddAliasCommand to add {@code alias} to {@code game} for the person with {@code name}.
     */
    public AddAliasCommand(Name name, Game game, Alias alias) {
        requireNonNull(name);
        requireNonNull(game);
        requireNonNull(alias);
        this.targetName = name;
        this.targetGame = game;
        this.aliasToAdd = alias;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Optional<Person> personOptional = model.getFilteredPersonList().stream()
                .filter(person -> person.getName().fullName
                .equalsIgnoreCase(targetName.fullName))
                .findFirst();

        if (personOptional.isEmpty()) {
            throw new CommandException(MESSAGE_PERSON_NOT_FOUND);
        }

        Person personToEdit = personOptional.get();

        Optional<Game> gameOptional = personToEdit.getGames().stream()
                .filter(game -> game.equals(targetGame))
                .findFirst();

        if (gameOptional.isEmpty()) {
            throw new CommandException(MESSAGE_GAME_NOT_FOUND);
        }

        Game gameToEdit = gameOptional.get();

        if (gameToEdit.getAliases().contains(aliasToAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_ALIAS);
        }

        Set<Alias> updatedAliases = new HashSet<>(gameToEdit.getAliases());
        updatedAliases.add(aliasToAdd);
        Game updatedGame = new Game(gameToEdit.gameName, updatedAliases);

        Set<Game> updatedGames = new HashSet<>(personToEdit.getGames());
        updatedGames.remove(gameToEdit);
        updatedGames.add(updatedGame);

        Person editedPerson = new Person(
                personToEdit.getName(),
                personToEdit.getTags(),
                updatedGames
        );

        model.setPerson(personToEdit, editedPerson);

        return new CommandResult(String.format(
                MESSAGE_SUCCESS,
                editedPerson.getName(),
                updatedGame.gameName,
                aliasToAdd));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof AddAliasCommand)) {
            return false;
        }

        AddAliasCommand otherCommand = (AddAliasCommand) other;
        return targetName.equals(otherCommand.targetName)
                && targetGame.equals(otherCommand.targetGame)
                && aliasToAdd.equals(otherCommand.aliasToAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetName", targetName)
                .add("targetGame", targetGame)
                .add("aliasToAdd", aliasToAdd)
                .toString();
    }
}
