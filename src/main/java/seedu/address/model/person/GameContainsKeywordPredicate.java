package seedu.address.model.person;

import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person} has a game whose name matches the given keyword (case-insensitive).
 */
public class GameContainsKeywordPredicate implements Predicate<Person> {
    private final String keyword;

    public GameContainsKeywordPredicate(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public boolean test(Person person) {
        return person.getGames().stream()
                .anyMatch(game -> game.gameName.equalsIgnoreCase(keyword));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof GameContainsKeywordPredicate)) {
            return false;
        }

        GameContainsKeywordPredicate otherPredicate = (GameContainsKeywordPredicate) other;
        return keyword.equalsIgnoreCase(otherPredicate.keyword);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("keyword", keyword).toString();
    }
}
