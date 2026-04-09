package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Person;

/**
 * An Immutable AddressBook that is serializable to JSON format.
 */
@JsonRootName(value = "addressbook")
class JsonSerializableAddressBook {

    public static final String MESSAGE_DUPLICATE_PERSON = "Persons list contains duplicate person(s).";

    private final List<JsonAdaptedPerson> persons = new ArrayList<>();

    /**
     * Constructs a {@code JsonSerializableAddressBook} with the given persons.
     */
    @JsonCreator
    public JsonSerializableAddressBook(@JsonProperty("persons") List<JsonAdaptedPerson> persons) {
        this.persons.addAll(persons);
    }

    /**
     * Converts a given {@code ReadOnlyAddressBook} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableAddressBook}.
     */
    public JsonSerializableAddressBook(ReadOnlyAddressBook source) {
        persons.addAll(source.getPersonList().stream().map(JsonAdaptedPerson::new).collect(Collectors.toList()));
    }

    /**
     * Converts this address book into the model's {@code AddressBook} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public AddressBook toModelType() throws IllegalValueException {
        AddressBook addressBook = new AddressBook();

        int userProfileCount = 0;
        Person userProfile = null;
        List<Person> normalContacts = new ArrayList<>();

        // Step 1: Parse all persons and separate the profile from normal contacts
        for (JsonAdaptedPerson jsonAdaptedPerson : persons) {
            Person person = jsonAdaptedPerson.toModelType();

            if (person.isUserProfile()) {
                userProfileCount++;
                // Enforce maximum of 1 profile
                if (userProfileCount > 1) {
                    throw new IllegalValueException("Data file contains multiple user profiles! Only 1 is allowed.");
                }
                userProfile = person;
            } else {
                normalContacts.add(person);
            }
        }

        // Step 2: Handle the User Profile (Add it FIRST so it is at index 0)
        if (userProfile != null) {
            addressBook.addPerson(userProfile);
        } else {
            addressBook.addUserProfile();
        }

        // Step 3: Add the rest of the normal contacts
        for (Person normalContact : normalContacts) {
            // Check for duplicates (this will also check against the user profile we just added!)
            if (addressBook.hasPerson(normalContact)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_PERSON);
            }
            addressBook.addPerson(normalContact);
        }

        return addressBook;
    }

}
