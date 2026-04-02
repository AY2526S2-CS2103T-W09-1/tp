package seedu.address.model.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

public class SampleDataUtilTest {

    @Test
    public void getSamplePersons_returnsNonEmptyArray() {
        Person[] persons = SampleDataUtil.getSamplePersons();
        assertNotNull(persons);
        assertTrue(persons.length > 0);
    }

    @Test
    public void getSamplePersons_allPersonsHaveNames() {
        for (Person person : SampleDataUtil.getSamplePersons()) {
            assertNotNull(person.getName());
        }
    }

    @Test
    public void getSampleAddressBook_containsSamplePersons() {
        ReadOnlyAddressBook addressBook = SampleDataUtil.getSampleAddressBook();
        assertNotNull(addressBook);
        assertEquals(SampleDataUtil.getSamplePersons().length,
                addressBook.getPersonList().size());
    }

    @Test
    public void getTagSet_singleTag_returnsCorrectSet() {
        Set<Tag> tags = SampleDataUtil.getTagSet("friends");
        assertEquals(1, tags.size());
        assertTrue(tags.contains(new Tag("friends")));
    }

    @Test
    public void getTagSet_multipleTags_returnsCorrectSet() {
        Set<Tag> tags = SampleDataUtil.getTagSet("friends", "colleagues");
        assertEquals(2, tags.size());
        assertTrue(tags.contains(new Tag("friends")));
        assertTrue(tags.contains(new Tag("colleagues")));
    }

    @Test
    public void getTagSet_noTags_returnsEmptySet() {
        Set<Tag> tags = SampleDataUtil.getTagSet();
        assertTrue(tags.isEmpty());
    }
}
