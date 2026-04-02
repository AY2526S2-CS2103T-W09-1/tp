package seedu.address.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.DeleteContactCommand.MESSAGE_PERSON_NOT_FOUND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.AMY;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.logic.commands.AddContactCommand;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.DeleteContactCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.storage.JsonAddressBookStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.StorageManager;
import seedu.address.testutil.PersonBuilder;

public class LogicManagerTest {
    private static final IOException DUMMY_IO_EXCEPTION = new IOException("dummy IO exception");
    private static final IOException DUMMY_AD_EXCEPTION = new AccessDeniedException("dummy access denied exception");

    @TempDir
    public Path temporaryFolder;

    private final Model model = new ModelManager();
    private Logic logic;

    @BeforeEach
    public void setUp() {
        JsonAddressBookStorage addressBookStorage =
                new JsonAddressBookStorage(temporaryFolder.resolve("addressBook.json"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(temporaryFolder.resolve("userPrefs.json"));
        StorageManager storage = new StorageManager(addressBookStorage, userPrefsStorage);
        logic = new LogicManager(model, storage);
    }

    @Test
    public void execute_invalidCommandFormat_throwsParseException() {
        String invalidCommand = "uicfhmowqewca";
        assertParseException(invalidCommand, MESSAGE_UNKNOWN_COMMAND);
    }

    @Test
    public void execute_commandExecutionError_throwsCommandException() {
        String deleteCommand = "contact delete n/NonExistent Person";
        assertCommandException(deleteCommand, MESSAGE_PERSON_NOT_FOUND);
    }

    @Test
    public void execute_validCommand_success() throws Exception {
        String listCommand = ListCommand.COMMAND_WORD;
        assertCommandSuccess(listCommand, ListCommand.MESSAGE_SUCCESS, model);
    }

    @Test
    public void execute_storageThrowsIoException_throwsCommandException() {
        assertCommandFailureForExceptionFromStorage(DUMMY_IO_EXCEPTION, String.format(
                LogicManager.FILE_OPS_ERROR_FORMAT, DUMMY_IO_EXCEPTION.getMessage()));
    }

    @Test
    public void execute_storageThrowsAdException_throwsCommandException() {
        assertCommandFailureForExceptionFromStorage(DUMMY_AD_EXCEPTION, String.format(
                LogicManager.FILE_OPS_PERMISSION_ERROR_FORMAT, DUMMY_AD_EXCEPTION.getMessage()));
    }

    @Test
    public void getFilteredPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> logic.getFilteredPersonList().remove(0));
    }

    // ==================== Delete confirmation flow tests ====================

    @Test
    public void execute_deleteContactCommand_returnsAwaitingConfirmation() throws Exception {
        Person person = new PersonBuilder(AMY).withTags().build();
        model.addPerson(person);

        CommandResult result = logic.execute("contact delete n/" + person.getName());

        assertTrue(result.isAwaitingConfirmation());
        assertEquals(person, result.getPendingPerson());
        assertTrue(model.getFilteredPersonList().contains(person));
    }

    @Test
    public void execute_deleteConfirmYes_deletesContact() throws Exception {
        Person person = new PersonBuilder(AMY).withTags().build();
        model.addPerson(person);
        logic.execute("contact delete n/" + person.getName());

        CommandResult result = logic.execute("y");

        assertEquals(String.format(DeleteContactCommand.MESSAGE_DELETE_PERSON_SUCCESS,
                person.getName()), result.getFeedbackToUser());
        assertFalse(model.getFilteredPersonList().contains(person));
    }

    @Test
    public void execute_deleteConfirmYesFull_deletesContact() throws Exception {
        Person person = new PersonBuilder(AMY).withTags().build();
        model.addPerson(person);
        logic.execute("contact delete n/" + person.getName());

        CommandResult result = logic.execute("yes");

        assertEquals(String.format(DeleteContactCommand.MESSAGE_DELETE_PERSON_SUCCESS,
                person.getName()), result.getFeedbackToUser());
        assertFalse(model.getFilteredPersonList().contains(person));
    }

    @Test
    public void execute_deleteConfirmNo_cancelsDeletion() throws Exception {
        Person person = new PersonBuilder(AMY).withTags().build();
        model.addPerson(person);
        logic.execute("contact delete n/" + person.getName());

        CommandResult result = logic.execute("n");

        assertEquals(String.format(DeleteContactCommand.MESSAGE_DELETE_CANCELLED, person.getName()),
                result.getFeedbackToUser());
        assertTrue(model.getFilteredPersonList().contains(person));
    }

    @Test
    public void execute_deleteConfirmNoFull_cancelsDeletion() throws Exception {
        Person person = new PersonBuilder(AMY).withTags().build();
        model.addPerson(person);
        logic.execute("contact delete n/" + person.getName());

        CommandResult result = logic.execute("no");

        assertEquals(String.format(DeleteContactCommand.MESSAGE_DELETE_CANCELLED, person.getName()),
                result.getFeedbackToUser());
        assertTrue(model.getFilteredPersonList().contains(person));
    }

    @Test
    public void execute_deleteConfirmInvalidInput_cancelsDeletion() throws Exception {
        Person person = new PersonBuilder(AMY).withTags().build();
        model.addPerson(person);
        logic.execute("contact delete n/" + person.getName());

        CommandResult result = logic.execute("maybe");

        assertEquals(String.format(DeleteContactCommand.MESSAGE_DELETE_CANCELLED, person.getName()),
                result.getFeedbackToUser());
        assertTrue(model.getFilteredPersonList().contains(person));
    }

    @Test
    public void execute_deleteConfirmYes_subsequentCommandsWorkNormally() throws Exception {
        Person person = new PersonBuilder(AMY).withTags().build();
        model.addPerson(person);
        logic.execute("contact delete n/" + person.getName());
        logic.execute("y");

        CommandResult result = logic.execute(ListCommand.COMMAND_WORD);
        assertEquals(ListCommand.MESSAGE_SUCCESS, result.getFeedbackToUser());
    }

    @Test
    public void execute_deleteConfirmNo_subsequentCommandsWorkNormally() throws Exception {
        Person person = new PersonBuilder(AMY).withTags().build();
        model.addPerson(person);
        logic.execute("contact delete n/" + person.getName());
        logic.execute("n");

        CommandResult result = logic.execute(ListCommand.COMMAND_WORD);
        assertEquals(ListCommand.MESSAGE_SUCCESS, result.getFeedbackToUser());
    }

    @Test
    public void execute_undoCommand_revertsLastCommand() throws Exception {
        Person person = new PersonBuilder(AMY).withTags().build();
        String addCommand = AddContactCommand.COMMAND_WORD + NAME_DESC_AMY;

        logic.execute(addCommand);
        assertTrue(model.getFilteredPersonList().contains(person));

        CommandResult result = logic.execute(UndoCommand.COMMAND_WORD);
        assertEquals(UndoCommand.MESSAGE_SUCCESS, result.getFeedbackToUser());
        assertFalse(model.getFilteredPersonList().contains(person));
    }

    @Test
    public void getAddressBook_returnsCurrentAddressBook() {
        assertEquals(model.getAddressBook(), logic.getAddressBook());
    }

    @Test
    public void getAddressBookFilePath_returnsCorrectPath() {
        assertEquals(model.getAddressBookFilePath(), logic.getAddressBookFilePath());
    }

    @Test
    public void getGuiSettings_returnsCurrentSettings() {
        assertEquals(model.getGuiSettings(), logic.getGuiSettings());
    }

    @Test
    public void setGuiSettings_updatesSettings() {
        seedu.address.commons.core.GuiSettings newSettings =
                new seedu.address.commons.core.GuiSettings(800, 600, 0, 0);
        logic.setGuiSettings(newSettings);
        assertEquals(newSettings, logic.getGuiSettings());
    }

    /**
     * Executes the command and confirms that
     * - no exceptions are thrown <br>
     * - the feedback message is equal to {@code expectedMessage} <br>
     * - the internal model manager state is the same as that in {@code expectedModel} <br>
     *
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandSuccess(String inputCommand, String expectedMessage,
                                      Model expectedModel) throws CommandException, ParseException {
        CommandResult result = logic.execute(inputCommand);
        assertEquals(expectedMessage, result.getFeedbackToUser());
        assertEquals(expectedModel, model);
    }

    /**
     * Executes the command, confirms that a ParseException is thrown and that the result message is correct.
     *
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertParseException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, ParseException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that a CommandException is thrown and that the result message is correct.
     *
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, CommandException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that the exception is thrown and that the result message is correct.
     *
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandFailure(String inputCommand, Class<? extends Throwable> expectedException,
                                      String expectedMessage) {
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        assertCommandFailure(inputCommand, expectedException, expectedMessage, expectedModel);
    }

    /**
     * Executes the command and confirms that
     * - the {@code expectedException} is thrown <br>
     * - the resulting error message is equal to {@code expectedMessage} <br>
     * - the internal model manager state is the same as that in {@code expectedModel} <br>
     *
     * @see #assertCommandSuccess(String, String, Model)
     */
    private void assertCommandFailure(String inputCommand, Class<? extends Throwable> expectedException,
                                      String expectedMessage, Model expectedModel) {
        assertThrows(expectedException, expectedMessage, () -> logic.execute(inputCommand));
        assertEquals(expectedModel, model);
    }

    @Test
    public void execute_deleteConfirmYes_storageIoExceptionThrown() throws Exception {
        Person person = new PersonBuilder(AMY).withTags().build();

        // Inject a storage that always throws on save
        JsonAddressBookStorage failingStorage = new JsonAddressBookStorage(
                temporaryFolder.resolve("failing.json")) {
            @Override
            public void saveAddressBook(ReadOnlyAddressBook addressBook, Path filePath)
                    throws IOException {
                throw DUMMY_IO_EXCEPTION;
            }
        };
        StorageManager storage = new StorageManager(failingStorage,
                new JsonUserPrefsStorage(temporaryFolder.resolve("prefs.json")));
        logic = new LogicManager(model, storage);

        model.addPerson(person);
        logic.execute("contact delete n/" + person.getName());

        assertThrows(CommandException.class,
                String.format(LogicManager.FILE_OPS_ERROR_FORMAT, DUMMY_IO_EXCEPTION.getMessage()), () ->
                logic.execute("y"));
    }

    @Test
    public void execute_deleteConfirmYes_storageAdExceptionThrown() throws Exception {
        Person person = new PersonBuilder(AMY).withTags().build();

        JsonAddressBookStorage failingStorage = new JsonAddressBookStorage(
                temporaryFolder.resolve("failing2.json")) {
            @Override
            public void saveAddressBook(ReadOnlyAddressBook addressBook, Path filePath)
                    throws IOException {
                throw DUMMY_AD_EXCEPTION;
            }
        };
        StorageManager storage = new StorageManager(failingStorage,
                new JsonUserPrefsStorage(temporaryFolder.resolve("prefs2.json")));
        logic = new LogicManager(model, storage);

        model.addPerson(person);
        logic.execute("contact delete n/" + person.getName());

        assertThrows(CommandException.class,
                String.format(LogicManager.FILE_OPS_PERMISSION_ERROR_FORMAT, DUMMY_AD_EXCEPTION.getMessage()), () ->
                logic.execute("y"));
    }

    /**
     * Tests the Logic component's handling of an {@code IOException} thrown by the Storage component.
     *
     * @param e               the exception to be thrown by the Storage component
     * @param expectedMessage the message expected inside exception thrown by the Logic component
     */
    private void assertCommandFailureForExceptionFromStorage(IOException e, String expectedMessage) {
        Path prefPath = temporaryFolder.resolve("ExceptionUserPrefs.json");

        // Inject LogicManager with an AddressBookStorage that throws the IOException e when saving
        JsonAddressBookStorage addressBookStorage = new JsonAddressBookStorage(prefPath) {
            @Override
            public void saveAddressBook(ReadOnlyAddressBook addressBook, Path filePath)
                    throws IOException {
                throw e;
            }
        };

        JsonUserPrefsStorage userPrefsStorage =
                new JsonUserPrefsStorage(temporaryFolder.resolve("ExceptionUserPrefs.json"));
        StorageManager storage = new StorageManager(addressBookStorage, userPrefsStorage);

        logic = new LogicManager(model, storage);

        // Triggers the saveAddressBook method by executing an add command
        String addCommand = AddContactCommand.COMMAND_WORD + NAME_DESC_AMY;
        Person expectedPerson = new PersonBuilder(AMY).withTags().build();
        ModelManager expectedModel = new ModelManager();
        expectedModel.addPerson(expectedPerson);
        assertCommandFailure(addCommand, CommandException.class, expectedMessage, expectedModel);
    }
}
