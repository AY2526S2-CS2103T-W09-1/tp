package seedu.address.model.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Person;

public class GeneratePlaceholderTest {

    @Test
    public void getPlaceHolderddressBook_containsOnePlaceholderProfile() {
        // Execute the method
        ReadOnlyAddressBook addressBook = GeneratePlaceholder.getPlaceHolderddressBook();

        // Verify AddressBook is created and has exactly 1 person
        assertNotNull(addressBook);
        assertEquals(1, addressBook.getPersonList().size());

        // Extract the person
        Person placeholderPerson = addressBook.getPersonList().get(0);

        // Verify the person's attributes exactly match the placeholder definition
        assertEquals("PLACEHOLDER", placeholderPerson.getName().fullName);
        assertTrue(placeholderPerson.getGames().isEmpty(), "Placeholder person should have no games.");
        assertTrue(placeholderPerson.isUserProfile(), "Placeholder person should be flagged as the user profile.");
    }
}
