package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.game.Game;
import seedu.address.model.tag.Tag;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {

    // Identity fields
    private final Name name;

    // Data fields
    private final Set<Tag> tags = new HashSet<>();
    private final Set<Game> games = new HashSet<>();
    private final boolean isUserProfile;

    /**
     * Every field must be present and not null. isUserProfile defaults to false.
     */
    public Person(Name name, Set<Tag> tags, Set<Game> games) {
        this(name, tags, games, false);
    }

    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Set<Tag> tags, Set<Game> games, boolean isUserProfile) {
        requireAllNonNull(name, tags);
        this.name = name;
        this.tags.addAll(tags);
        this.games.addAll(games);
        this.isUserProfile = isUserProfile;
    }

    public Name getName() {
        return name;
    }

    public boolean isUserProfile() {
        return isUserProfile;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns an immutable game set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Game> getGames() {
        return Collections.unmodifiableSet(games);
    }

    /**
     * Returns true if both persons have the same name.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.getName().fullName.equalsIgnoreCase(getName().fullName);
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;
        return name.equals(otherPerson.name)
                && tags.equals(otherPerson.tags)
                && games.equals(otherPerson.games);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, tags, games);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("tags", tags)
                .toString();
    }

}
