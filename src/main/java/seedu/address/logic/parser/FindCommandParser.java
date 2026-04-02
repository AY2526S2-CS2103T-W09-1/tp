package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ALIAS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GAME;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.AliasMatchesPredicate;
import seedu.address.model.person.GameContainsKeywordPredicate;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Person;

/**
 * Parses input arguments and creates a new FindCommand object.
 */
public class FindCommandParser implements Parser<FindCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns a FindCommand object for execution.
     * Supports combining name keywords, g/ and al/ constraints with AND logic.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(" " + args, PREFIX_GAME, PREFIX_ALIAS);

        String preamble = argMultimap.getPreamble().trim();
        Optional<String> gameValue = argMultimap.getValue(PREFIX_GAME);
        Optional<String> aliasValue = argMultimap.getValue(PREFIX_ALIAS);

        boolean hasName = !preamble.isEmpty();
        boolean hasGame = gameValue.isPresent() && !gameValue.get().isEmpty();
        boolean hasAlias = aliasValue.isPresent() && !aliasValue.get().isEmpty();

        if (gameValue.isPresent() && !hasGame) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }
        if (aliasValue.isPresent() && !hasAlias) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }
        if (!hasName && !hasGame && !hasAlias) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        // Single constraint — return specific predicate type for clean equality semantics
        if (hasName && !hasGame && !hasAlias) {
            String[] nameKeywords = preamble.split("\\s+");
            return new FindCommand(new NameContainsKeywordsPredicate(Arrays.asList(nameKeywords)));
        }
        if (!hasName && hasGame && !hasAlias) {
            return new FindCommand(new GameContainsKeywordPredicate(gameValue.get()));
        }
        if (!hasName && !hasGame && hasAlias) {
            return new FindCommand(new AliasMatchesPredicate(aliasValue.get()));
        }

        // Multiple constraints — AND all predicates together
        Predicate<Person> combined = person -> true;
        if (hasName) {
            String[] nameKeywords = preamble.split("\\s+");
            combined = combined.and(new NameContainsKeywordsPredicate(Arrays.asList(nameKeywords)));
        }
        if (hasGame) {
            combined = combined.and(new GameContainsKeywordPredicate(gameValue.get()));
        }
        if (hasAlias) {
            combined = combined.and(new AliasMatchesPredicate(aliasValue.get()));
        }
        return new FindCommand(combined);
    }
}
