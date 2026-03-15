package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.game.Game;
import seedu.address.model.person.Alias;

/**
 * Jackson-friendly version of {@link Game}.
 */
class JsonAdaptedGame {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Game's %s field is missing!";

    private final String gameName;
    private final List<JsonAdaptedAlias> aliases = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedGame} with the given game details.
     */
    @JsonCreator
    public JsonAdaptedGame(@JsonProperty("gameName") String gameName,
                           @JsonProperty("aliases") List<JsonAdaptedAlias> aliases) {
        this.gameName = gameName;
        if (aliases != null) {
            this.aliases.addAll(aliases);
        }
    }

    /**
     * Converts a given {@code Game} into this class for Jackson use.
     */
    public JsonAdaptedGame(Game source) {
        gameName = source.gameName;
        aliases.addAll(source.getAliases().stream()
                .map(JsonAdaptedAlias::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted game object into the model's {@code Game} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted game.
     */
    public Game toModelType() throws IllegalValueException {
        final List<Alias> gameAliases = new ArrayList<>();
        for (JsonAdaptedAlias alias : aliases) {
            gameAliases.add(alias.toModelType());
        }

        if (gameName == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    "gameName"));
        }
        if (!Game.isValidGameName(gameName)) {
            throw new IllegalValueException(Game.MESSAGE_CONSTRAINTS);
        }

        final Set<Alias> modelAliases = new HashSet<>(gameAliases);
        return new Game(gameName, modelAliases);
    }
}