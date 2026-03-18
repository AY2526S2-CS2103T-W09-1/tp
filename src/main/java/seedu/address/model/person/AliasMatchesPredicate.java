package seedu.address.model.person;

import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person} has an alias that matches the given keyword (case-insensitive).
 */
public class AliasMatchesPredicate implements Predicate<Person> {
    private final String keyword;

    public AliasMatchesPredicate(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public boolean test(Person person) {
        return person.getGames().stream()
                .flatMap(game -> game.getAliases().stream())
                .anyMatch(alias -> alias.value.equalsIgnoreCase(keyword));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof AliasMatchesPredicate)) {
            return false;
        }

        AliasMatchesPredicate otherPredicate = (AliasMatchesPredicate) other;
        return keyword.equalsIgnoreCase(otherPredicate.keyword);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("keyword", keyword).toString();
    }
}
