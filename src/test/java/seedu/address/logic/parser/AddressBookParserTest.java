package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddAliasCommand;
import seedu.address.logic.commands.AddContactCommand;
import seedu.address.logic.commands.AddGameCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.DeleteAliasCommand;
import seedu.address.logic.commands.DeleteContactCommand;
import seedu.address.logic.commands.DeleteGameCommand;
import seedu.address.logic.commands.EditAliasCommand;
import seedu.address.logic.commands.EditContactCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.ListGameCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.game.Game;
import seedu.address.model.person.Alias;
import seedu.address.model.person.Name;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.PersonUtil;

public class AddressBookParserTest {

    private final AddressBookParser parser = new AddressBookParser();

    @Test
    public void parseCommand_add() throws Exception {
        Person person = new PersonBuilder().build();
        AddContactCommand command = (AddContactCommand) parser.parseCommand(PersonUtil.getAddCommand(person));
        assertEquals(new AddContactCommand(person), command);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_contactDelete() throws Exception {
        DeleteContactCommand command = (DeleteContactCommand) parser.parseCommand(
                DeleteContactCommand.COMMAND_WORD + " n/Janelle Lum");
        assertEquals(new DeleteContactCommand(null, new Name("Janelle Lum"), false), command);
    }

    @Test
    public void parseCommand_contactEditByName() throws Exception {
        EditContactCommand command = (EditContactCommand) parser.parseCommand(
                "contact edit n/Janelle e/Jan");
        assertEquals(new EditContactCommand(null, new Name("Janelle"), new Name("Jan"), false), command);
    }

    @Test
    public void parseCommand_contactEditByIndex() throws Exception {
        EditContactCommand command = (EditContactCommand) parser.parseCommand(
                "contact edit 1 e/Jan");
        assertEquals(new EditContactCommand(
                seedu.address.commons.core.index.Index.fromOneBased(1), null, new Name("Jan"), false), command);
    }

    @Test
    public void parseCommand_aliasEditByName() throws Exception {
        EditAliasCommand command = (EditAliasCommand) parser.parseCommand(
                "alias edit n/Benjamin g/Valorant al/JohnnyV na/JohnnyValorant");
        assertEquals(new EditAliasCommand(
                null, new Name("Benjamin"),
                new Game("Valorant"), new Alias("JohnnyV"), new Alias("JohnnyValorant"), false), command);
    }

    @Test
    public void parseCommand_aliasEditByIndex() throws Exception {
        EditAliasCommand command = (EditAliasCommand) parser.parseCommand(
                "alias edit 1 g/Valorant al/JohnnyV na/JohnnyValorant");
        assertEquals(new EditAliasCommand(
                seedu.address.commons.core.index.Index.fromOneBased(1), null,
                new Game("Valorant"), new Alias("JohnnyV"), new Alias("JohnnyValorant"), false), command);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_find() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        FindCommand command = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindCommand(new NameContainsKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD) instanceof ListCommand);
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD + " 3") instanceof ListCommand);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE), ()
                -> parser.parseCommand(""));
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class,
                MESSAGE_UNKNOWN_COMMAND, () -> parser.parseCommand("unknownCommand"));
    }

    @Test
    public void parseCommand_aliasAdd() throws Exception {
        AddAliasCommand command = (AddAliasCommand) parser.parseCommand(
                "alias add n/Benjamin g/Valorant al/Benjumpin");
        assertEquals(new AddAliasCommand(
                null,
                new Name("Benjamin"), new Game("Valorant"), new Alias("Benjumpin"), false
        ), command);
    }

    @Test
    public void parseCommand_aliasDelete() throws Exception {
        DeleteAliasCommand command = (DeleteAliasCommand) parser.parseCommand(
                "alias delete n/Benjamin g/Valorant al/Benjumpin");
        assertEquals(new DeleteAliasCommand(null,
                new Name("Benjamin"),
                new Game("Valorant"), new Alias("Benjumpin"), false
        ), command);
    }

    @Test
    public void parseCommand_gameAdd() throws Exception {
        AddGameCommand command = (AddGameCommand) parser.parseCommand(
                "game add n/Benjamin g/Valorant");
        assertEquals(new AddGameCommand(null,
                new Name("Benjamin"),
                new Game("Valorant"), false
        ), command);
    }

    @Test
    public void parseCommand_gameDelete() throws Exception {
        DeleteGameCommand command = (DeleteGameCommand) parser.parseCommand(
                "game delete n/Benjamin g/Valorant");
        assertEquals(new DeleteGameCommand(null,
                new Name("Benjamin"),
                new Game("Valorant"), false
        ), command);
    }

    @Test
    public void parseCommand_gameList() throws Exception {
        ListGameCommand command = (ListGameCommand) parser.parseCommand(
                "game list n/Benjamin");
        assertEquals(new ListGameCommand(null, new Name("Benjamin"), false), command);
    }
}
