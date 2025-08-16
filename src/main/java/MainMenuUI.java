import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.util.List;

public class MainMenuUI {
    public static final int LOGO_WIDTH = 700;
    public static final int PROFILE_SELECTION_W = 300;
    public static final int PROFILE_SELECTION_H = 200;
    public static final int HIGH_SCORE_SELECTION_W = 300;
    public static final int HIGH_SCORE_SELECTION_H = 150;


    // Interface to avoid tight coupling between the UI and the game logic
    public interface MenuActionListener {
        void onStartNewGame();
        void onLoadGame();
        void onDeleteProfile();
        void onShowHighScores();
        void onQuit();
    }

    public interface ProfileActionListener {
        void onLoadProfile(String profileName, Stage dialog);
        void onDeleteProfile(String profileName, Stage dialog);
    }

    public interface HighScoresActionListener {
        void onShowLevelScores(int level, Stage dialog);
    }

    public static VBox createMenu(MenuActionListener listener) {
        VBox menuBox = new VBox(BoulderDashApp.SPACING);

        ImageView logo = new ImageView(new Image("images/title.png"));
        logo.setFitWidth(LOGO_WIDTH);
        logo.setPreserveRatio(true);

        Button newGameButton = new Button("Start New Game");
        newGameButton.setOnAction(e -> listener.onStartNewGame());

        Button loadGameButton = new Button("Load Game");
        loadGameButton.setOnAction(e -> listener.onLoadGame());

        Button profileButton = new Button("Delete Player Profile");
        profileButton.setOnAction(e -> listener.onDeleteProfile());

        Button highScoresButton = new Button("High Scores");
        highScoresButton.setOnAction(e -> listener.onShowHighScores());

        Button quitButton = new Button("Quit Game");
        quitButton.setOnAction(e -> listener.onQuit());

        menuBox.getChildren().addAll(logo, newGameButton, loadGameButton,
                profileButton, highScoresButton, quitButton);
        menuBox.setStyle("-fx-padding: 20; -fx-alignment: center;");
        return menuBox;
    }

    public static void showProfileSelectionDialog(
            String title,
            boolean isLoadGame,
            List<PlayerProfile> profiles,
            ProfileActionListener listener) {

        Stage dialog = new Stage();
        dialog.setTitle(title);

        ComboBox<String> profileDropdown = new ComboBox<>();
        for (PlayerProfile profile : profiles) {
            profileDropdown.getItems().add(profile.getName());
        }

        String buttonText = isLoadGame ? "Load" : "Delete";
        Button actionButton = new Button(buttonText);

        if (!isLoadGame) {
            actionButton.setStyle("-fx-background-color: red;");
        }

        actionButton.setOnAction(event -> {
            String selectedProfileName = profileDropdown.getValue();
            if (selectedProfileName != null) {
                if (isLoadGame) {
                    listener.onLoadProfile(selectedProfileName, dialog);
                } else {
                    listener.onDeleteProfile(selectedProfileName, dialog);
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("No Selection");
                alert.setHeaderText("No Profile Selected");
                alert.setContentText("Please select a profile");
                alert.showAndWait();
            }
        });

        Label label = new Label("Select a player profile:");

        VBox layout = new VBox(BoulderDashApp.SPACING);
        layout.getChildren().addAll(label, profileDropdown, actionButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, PROFILE_SELECTION_W, PROFILE_SELECTION_H);
        dialog.setScene(scene);
        dialog.show();
    }

    public static void showHighScoresTableSelection(HighScoresActionListener listener) {

        Stage dialog = new Stage();
        dialog.setTitle("High Scores");
        dialog.initModality(Modality.APPLICATION_MODAL);

        Button highScores1Button = new Button("Level 1 High Scores");
        highScores1Button.setOnAction(event -> {
            dialog.hide();
            listener.onShowLevelScores(1, dialog);
        });

        Button highScores2Button = new Button("Level 2 High Scores");
        highScores2Button.setOnAction(event -> {
            dialog.hide();
            listener.onShowLevelScores(2, dialog);
        });

        Button highScores3Button = new Button("Level 3 High Scores");
        highScores3Button.setOnAction(event -> {
            dialog.hide();
            listener.onShowLevelScores(3, dialog);
        });

        VBox dialogBox = new VBox(BoulderDashApp.SPACING, highScores1Button, highScores2Button, highScores3Button);
        dialogBox.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Scene dialogScene = new Scene(dialogBox, HIGH_SCORE_SELECTION_W, HIGH_SCORE_SELECTION_H);
        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }
}
