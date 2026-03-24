package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.HashSet;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;

public class ViewProfileCommandTest {

    @Test
    public void execute_profileExists_success() throws Exception {
        Person userProfile = new Person(new Name("John Doe"), new HashSet<>(), new HashSet<>(), true);
        AddressBook ab = new AddressBook();
        ab.addPerson(userProfile);
        Model model = new ModelManager(ab, new UserPrefs());

        CommandResult result = new ViewProfileCommand().execute(model);

        assertEquals(ViewProfileCommand.MESSAGE_SUCCESS, result.getFeedbackToUser());
    }

    @Test
    public void execute_noProfile_throwsCommandException() {
        Model model = new ModelManager(new AddressBook(), new UserPrefs());

        assertThrows(CommandException.class,
                ViewProfileCommand.MESSAGE_NO_PROFILE, () -> new ViewProfileCommand().execute(model));
    }
}
