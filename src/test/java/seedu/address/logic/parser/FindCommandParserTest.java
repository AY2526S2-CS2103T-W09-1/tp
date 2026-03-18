package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindCommand;
import seedu.address.model.person.AliasMatchesPredicate;
import seedu.address.model.person.GameContainsKeywordPredicate;
import seedu.address.model.person.NameContainsKeywordsPredicate;

public class FindCommandParserTest {

    private FindCommandParser parser = new FindCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validNameArgs_returnsFindCommand() {
        FindCommand expectedFindCommand =
                new FindCommand(new NameContainsKeywordsPredicate(Arrays.asList("Alice", "Bob")));
        assertParseSuccess(parser, "Alice Bob", expectedFindCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " \n Alice \n \t Bob  \t", expectedFindCommand);
    }

    @Test
    public void parse_validGamePrefix_returnsFindCommand() {
        FindCommand expectedFindCommand = new FindCommand(new GameContainsKeywordPredicate("Valorant"));
        assertParseSuccess(parser, "g/Valorant", expectedFindCommand);

        // with leading whitespace
        assertParseSuccess(parser, " g/Valorant", expectedFindCommand);
    }

    @Test
    public void parse_emptyGamePrefix_throwsParseException() {
        assertParseFailure(parser, "g/", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validAliasPrefix_returnsFindCommand() {
        FindCommand expectedFindCommand = new FindCommand(new AliasMatchesPredicate("BenJumpin"));
        assertParseSuccess(parser, "al/BenJumpin", expectedFindCommand);

        // with leading whitespace
        assertParseSuccess(parser, " al/BenJumpin", expectedFindCommand);
    }

    @Test
    public void parse_emptyAliasPrefix_throwsParseException() {
        assertParseFailure(parser, "al/", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }
}
