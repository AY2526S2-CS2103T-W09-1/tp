package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ALIAS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import seedu.address.logic.commands.AddContactCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.game.Game;
import seedu.address.model.person.Alias;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new AddContactCommand object
 */
public class AddContactCommandParser implements Parser<AddContactCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddContactCommand
     * and returns an AddContactCommand object for execution.
     * Games and Aliases are parsed linearly (left-to-right). Any {@code PREFIX_ALIAS} encountered
     * is automatically assigned to the most recently declared {@code PREFIX_GAME}. This allows a single
     * game to have multiple aliases, or zero aliases.
     *
     * @param args The user input string containing the command arguments.
     * @return An AddContactCommand ready for execution.
     * @throws ParseException if the user input does not conform to the expected format,
     *     or if an alias is declared before any game prefix.
     */
    public AddContactCommand parse(String args) throws ParseException {
        // 1. Add Game and Alias prefixes to tokenizer
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_TAG, PREFIX_GAME, PREFIX_ALIAS);

        if (!arePrefixesPresent(argMultimap, PREFIX_NAME)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddContactCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME);
        Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());
        Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));

        // 2. Iterate linearly to maintain the 1-to-N or 1-to-0 mapping
        Map<Game, Set<Alias>> gameToAliasesMap = new LinkedHashMap<>();
        Game currentGame = null;

        for (Map.Entry<Prefix, String> entry : argMultimap.getOrderedArguments()) {
            Prefix prefix = entry.getKey();
            String value = entry.getValue();

            if (prefix.equals(PREFIX_GAME)) {
                currentGame = ParserUtil.parseGame(value);
                // If game already exists (e.g. g/Valo ... g/Valo), this just ensures the set exists
                gameToAliasesMap.putIfAbsent(currentGame, new HashSet<>());

            } else if (prefix.equals(PREFIX_ALIAS)) {
                if (currentGame == null) {
                    throw new ParseException("Aliases must be preceded by a game prefix (g/).");
                }
                Alias alias = ParserUtil.parseAlias(value);
                gameToAliasesMap.get(currentGame).add(alias);
            }
        }

        // 3. Construct the final Game set
        Set<Game> gameList = new HashSet<>();
        for (Map.Entry<Game, Set<Alias>> entry : gameToAliasesMap.entrySet()) {
            gameList.add(new Game(entry.getKey().gameName, entry.getValue()));
        }

        Person person = new Person(name, tagList, gameList);

        return new AddContactCommand(person);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
