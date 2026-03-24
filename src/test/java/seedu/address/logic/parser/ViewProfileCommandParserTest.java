package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ViewProfileCommand;
import seedu.address.model.person.Name;

public class ViewProfileCommandParserTest {

    private ViewProfileCommandParser parser = new ViewProfileCommandParser();

    @Test
    public void parse_emptyArgs_returnsViewSelfCommand() {
        // Simulates: profile view
        assertParseSuccess(parser, "  ", new ViewProfileCommand(null, null));
    }

    @Test
    public void parse_validArgsByName_returnsViewCommand() {
        // Simulates: profile view n/Alice Pauline
        assertParseSuccess(parser, " n/Alice Pauline", new ViewProfileCommand(null, new Name("Alice Pauline")));
    }

    @Test
    public void parse_validArgsByIndex_returnsViewCommand() {
        // Simulates: profile view 1
        assertParseSuccess(parser, " 1", new ViewProfileCommand(INDEX_FIRST_PERSON, null));
    }

    @Test
    public void parse_bothIndexAndName_throwsParseException() {
        // Simulates mutually exclusive error: profile view 1 n/Alice Pauline
        assertParseFailure(parser, " 1 n/Alice Pauline",
                "Please provide either an index OR a name, not both.");
    }
}
