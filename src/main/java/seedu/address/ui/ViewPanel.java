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
     * Updates the UI to display the details of the given {@code Person}.
     */
    public void setPerson(Person person) {
        if (person == null) {
            return;
        }

        // Show profile container, hide placeholder text
        placeholderText.setVisible(false);
        placeholderText.setManaged(false);
        profileContainer.setVisible(true);
        profileContainer.setManaged(true);

        // Set the Name
        name.setText("User: " + person.getName().fullName);

        // Clear out any prev tags
        tags.getChildren().clear();

        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> {
                    Label tagLabel = new Label(tag.tagName);
                    // Give it a nice, modern blue tag styling to match the dark theme
                    tagLabel.setStyle("-fx-background-color: #3e7b91; -fx-text-fill: white; "
                            + "-fx-padding: 3 7 3 7; -fx-background-radius: 3; -fx-font-size: 11px;");
                    tags.getChildren().add(tagLabel);
                });

        // Clear out any previous games
        gamesList.getChildren().clear();

        // Populate the Games and Aliases
        if (person.getGames().isEmpty()) {
            Label emptyLabel = new Label("No games added yet.");
            emptyLabel.setStyle("-fx-text-fill: derive(white, -30%); -fx-font-style: italic;");
            gamesList.getChildren().add(emptyLabel);
        } else {
            Label gamesTitle = new Label("GAMES & ALIASES");
            gamesTitle.setStyle("-fx-font-weight: bold;"
                    + " -fx-text-fill: #b3d4ff;"
                    + " -fx-font-size: 13px;"
                    + " -fx-padding: 10 0 5 0;");
            gamesList.getChildren().add(gamesTitle);

            person.getGames().forEach(game -> {
                VBox gameBox = new VBox();
                gameBox.setStyle("-fx-background-color: derive(#1d1d1d, 20%);"
                        + " -fx-padding: 10;"
                        + " -fx-background-radius: 5;");

                Label gameNameLabel = new Label("🎮 " + game.gameName);
                gameNameLabel.setWrapText(true); // Wrap text
                //gameNameLabel.setMaxWidth(300); // Force wrap if it's one giant word without spaces
                gameNameLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white; -fx-font-size: 14px;");
                gameBox.getChildren().add(gameNameLabel);

                if (!game.getAliases().isEmpty()) {
                    StringBuilder aliasText = new StringBuilder("Alias: ");
                    game.getAliases().forEach(alias -> aliasText.append(alias).append(", "));
                    aliasText.setLength(aliasText.length() - 2); // Remove trailing comma and space

                    Label aliasLabel = new Label(aliasText.toString());
                    aliasLabel.setStyle("-fx-text-fill: derive(white, -20%);"
                            + " -fx-font-size: 12px;"
                            + " -fx-padding: 3 0 0 5;");
                    aliasLabel.setWrapText(true);
                    gameBox.getChildren().add(aliasLabel);
                }

                gamesList.getChildren().add(gameBox);
            });
        }
    }
}
