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
 * Adds a game to an existing contact in the address book.
 */
public class AddGameCommand extends Command {

    public static final String COMMAND_WORD = "add";
    public static final String MESSAGE_USAGE = "game " + COMMAND_WORD
            + ": Adds a game to the specified contact.\n"
            + "Parameters: " + PREFIX_NAME + "CONTACT_NAME " + PREFIX_GAME + "GAME_NAME\n"
            + "Example: game " + COMMAND_WORD + " " + PREFIX_NAME + "Zi Xuan " + PREFIX_GAME + "Minecraft";

    public static final String MESSAGE_SUCCESS = "Game %1$s added to %2$s";
    public static final String MESSAGE_CONTACT_NOT_FOUND = "Error: Contact does not exist.";
    public static final String MESSAGE_DUPLICATE_GAME = "Error: Game already exists for the contact.";

    private final Name targetName;
    private final Game gameToAdd;

    /**
     * @param targetName of the person in the filtered person list to edit.
     * @param gameToAdd the game to add to the person.
     */
    public AddGameCommand(Name targetName, Game gameToAdd) {
        this.targetName = targetName;
        this.gameToAdd = gameToAdd;
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

        // Check for duplicate games
        if (personToEdit.getGames().contains(gameToAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_GAME);
        }

        // Create a new Set of games and add the new one
        Set<Game> updatedGames = new HashSet<>(personToEdit.getGames());
        updatedGames.add(gameToAdd);

        // Create a copy of the person with the updated games
        Person editedPerson = new Person(
                personToEdit.getName(), personToEdit.getPhone(), personToEdit.getEmail(),
                personToEdit.getAddress(), personToEdit.getTags(), updatedGames);

        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        return new CommandResult(String.format(MESSAGE_SUCCESS, gameToAdd.gameName, editedPerson.getName().fullName));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof AddGameCommand)) {
            return false;
        }
        AddGameCommand e = (AddGameCommand) other;
        return targetName.equals(e.targetName) && gameToAdd.equals(e.gameToAdd);
    }
}
