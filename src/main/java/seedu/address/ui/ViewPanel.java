package seedu.address.ui;

import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
//import javafx.scene.control.Tooltip;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.model.person.Person;

/**
 * A UI component that displays the full profile of a selected contact.
 */
public class ViewPanel extends UiPart<Region> {
    private static final String FXML = "ViewPanel.fxml";

    @FXML
    private Label placeholderText;
    @FXML
    private ScrollPane profileContainer;
    @FXML
    private Label name;
    @FXML
    private VBox gamesList;
    @FXML
    private FlowPane tags;

    /**
     * Constructor for ViewPanel
     */
    public ViewPanel() {
        super(FXML);
    }

    /**
     * Clears the ViewPanel and shows the placeholder text.
     */
    public void clearPerson() {
        placeholderText.setVisible(true);
        placeholderText.setManaged(true);
        profileContainer.setVisible(false);
        profileContainer.setManaged(false);
        name.setText("");
        tags.getChildren().clear();
        gamesList.getChildren().clear();
    }

    /**
     * Updates the UI to display the details of the given {@code Person}.
     */
    public void setPerson(Person person) {
        if (person == null) {
            return;
        }

        placeholderText.setVisible(false);
        placeholderText.setManaged(false);
        profileContainer.setVisible(true);
        profileContainer.setManaged(true);

        name.setText("User: " + person.getName().fullName);

        tags.getChildren().clear();

        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> {
                    Label tagLabel = new Label(tag.tagName);
                    // CHANGED: Use CSS class instead of hardcoded colors
                    tagLabel.getStyleClass().add("profile-tag");
                    tags.getChildren().add(tagLabel);
                });

        gamesList.getChildren().clear();

        if (person.getGames().isEmpty()) {
            Label emptyLabel = new Label("No games added yet.");
            // CHANGED: Use CSS class
            emptyLabel.getStyleClass().add("profile-empty-label");
            gamesList.getChildren().add(emptyLabel);
        } else {
            Label gamesTitle = new Label("GAMES & ALIASES");
            // CHANGED: Use CSS class
            gamesTitle.getStyleClass().add("profile-games-title");
            gamesList.getChildren().add(gamesTitle);

            person.getGames().forEach(game -> {
                VBox gameBox = new VBox();
                // CHANGED: Use CSS class for the dark box
                gameBox.getStyleClass().add("profile-game-box");

                Label gameNameLabel = new Label("🎮 " + game.gameName);
                gameNameLabel.setWrapText(true);
                // CHANGED: Use CSS class
                gameNameLabel.getStyleClass().add("profile-game-name");
                gameBox.getChildren().add(gameNameLabel);

                if (!game.getAliases().isEmpty()) {
                    StringBuilder aliasText = new StringBuilder("Alias: ");
                    game.getAliases().forEach(alias -> aliasText.append(alias).append(", "));
                    aliasText.setLength(aliasText.length() - 2);

                    Label aliasLabel = new Label(aliasText.toString());
                    // CHANGED: Use CSS class
                    aliasLabel.getStyleClass().add("profile-alias-text");
                    aliasLabel.setWrapText(true);
                    gameBox.getChildren().add(aliasLabel);
                }

                gamesList.getChildren().add(gameBox);
            });
        }
    }
}
