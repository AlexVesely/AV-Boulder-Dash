import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

public class MainMenuUI {
    public interface MenuActionListener {
        void onStartNewGame();
        void onLoadGame();
        void onDeleteProfile();
        void onShowHighScores();
        void onQuit();
    }

    public static VBox createMenu(MenuActionListener listener, int spacing, int logoWidth) {
        VBox menuBox = new VBox(spacing);

        ImageView logo = new ImageView(new Image("images/title.png"));
        logo.setFitWidth(logoWidth);
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
}
