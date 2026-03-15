package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Alias;

/**
 * Jackson-friendly version of {@link Alias}.
 */
class JsonAdaptedAlias {

    private final String aliasName;

    /**
     * Constructs a {@code JsonAdaptedAlias} with the given {@code aliasName}.
     */
    @JsonCreator
    public JsonAdaptedAlias(String aliasName) {
        this.aliasName = aliasName;
    }

    /**
     * Converts a given {@code Alias} into this class for Jackson use.
     */
    public JsonAdaptedAlias(Alias source) {
        aliasName = source.value;
    }

    @JsonValue
    public String getAliasName() {
        return aliasName;
    }

    /**
     * Converts this Jackson-friendly adapted alias object into the model's {@code Alias} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted alias.
     */
    public Alias toModelType() throws IllegalValueException {
        if (!Alias.isValidAlias(aliasName)) {
            throw new IllegalValueException(Alias.MESSAGE_CONSTRAINTS);
        }
        return new Alias(aliasName);
    }
}