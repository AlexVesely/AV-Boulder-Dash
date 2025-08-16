import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.ArrayList;
import javafx.scene.image.Image;

/**
 * BoulderDashApp sets up the GUI and initialises everything for a game to take place.
 * @author Ibrahim Boukhalfa
 * @author Omar Sanad
 * @author Tahi Rahman
 * @author Alex Vesely
 */
public class BoulderDashApp extends Application {
    public static final int WINDOW_WIDTH = 1500;
    public static final int WINDOW_HEIGHT = 800;
    public static final int GRID_CELL_WIDTH = 30;
    public static final int GRID_CELL_HEIGHT = 30;

    public static final int DIAMOND_SCORE_VALUE = 100;
    public static final int TIME_SCORE_VALUE = 25;
    public static final int SPACING = 10;

    // Timeline for periodic ticks
    private Timeline playerTickTimeline;
    private Timeline dangerousRockFallTickTimeline;
    private Timeline dangerousRockRollTimeline;
    private Timeline frogTickTimeline;
    private Timeline amoebaTickTimeline;
    private Timeline flyTickTimeline;
    private Timeline killPlayerTickTimeLine;
    private Timeline explosionTickTimeLine;
    private Timeline timerTimeline;
    private Timeline diamondCountTimeline;
    private Timeline checkLevelWinTimeline;

    private int secondsRemaining;
    private ArrayList<PlayerProfile> profiles = new ArrayList<>();
    private PlayerProfile currentProfile;

    private Stage primaryStage;

    /**
     * Start the Boulder-Dash-Remake
     * @param primaryStage the primary stage for this application
     */
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("BOULDER DASH");
        primaryStage.getIcons().add(new Image("images/boulder.png"));

        VBox menuBox = MainMenuUI.createMenu(new MainMenuUI.MenuActionListener() {
            @Override
            public void onStartNewGame() {
                currentProfile = ProfileManager.promptForProfile();
                profiles.add(currentProfile);
                String levelFile = "src/main/resources/txt/Level1.txt";
                setupGame(primaryStage, levelFile, true);
            }
            @Override
            public void onLoadGame() {
                showProfileSelectionDialog("Load Game", true);
            }
            @Override
            public void onDeleteProfile() {
                showProfileSelectionDialog("Delete Player Profile", false);
            }
            @Override
            public void onShowHighScores() {
                showHighScoresTableSelection();
            }
            @Override
            public void onQuit() {
                closeGame();
            }
        });

        Scene menuScene = new Scene(menuBox, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setScene(menuScene);

        profiles = ProfileManager.getAvailableProfiles();
        primaryStage.show();
    }

    /**
     * Displays a profile selection dialog for either loading or deleting a profile.
     * @param title Title of what the selection is for
     * @param isLoadGame true if selection is to load game, false if it is for deletion.
     */
    private void showProfileSelectionDialog(String title, boolean isLoadGame) {
        MainMenuUI.showProfileSelectionDialog(
                title,
                isLoadGame,
                profiles,
                new MainMenuUI.ProfileActionListener() {
                    @Override
                    public void onLoadProfile(String profileName, Stage dialog) {
                        handleLoadGame(profileName, dialog);
                    }
                    @Override
                    public void onDeleteProfile(String profileName, Stage dialog) {
                        handleDeleteProfile(profileName, dialog);
                    }
                }
        );
    }

    private void showHighScoresTableSelection() {
        MainMenuUI.showHighScoresTableSelection(
                (level, dialog) -> HighScoreTableManager.displayHighScoresInMainMenu(level, dialog)
        );
    }

    /**
     * Handles loading a game for the selected profile.
     * @param selectedProfileName name of selected profile.
     * @param dialog this window.
     */
    private void handleLoadGame(String selectedProfileName, Stage dialog) {
        int index = 0;
        while (index < profiles.size() && currentProfile == null) {
            PlayerProfile profile = profiles.get(index);
            if (profile.getName().equals(selectedProfileName)) {
                currentProfile = profile;
            }
            index++;
        }

        if (currentProfile != null) {
            int playerID = currentProfile.getPlayerId();
            if (ProfileManager.doesPlayerSaveFileExist(playerID)) {
                String levelFile = "src/main/resources/txt/Save" + playerID + ".txt";
                setupGame(primaryStage, levelFile,true);
            } else {
                int level = currentProfile.getMaxLevelReached();
                String levelFile = "src/main/resources/txt/Level" + level + ".txt";
                setupGame(primaryStage, levelFile,true);
            }
        }
        dialog.close();
    }

    /**
     * Handles deleting the selected profile.
     * @param selectedProfileName name of selected profile.
     * @param dialog this window.
     */
    private void handleDeleteProfile(String selectedProfileName, Stage dialog) {
        PlayerProfile profileToDelete = null;
        int index = 0;
        while (index < profiles.size() && profileToDelete == null) {
            PlayerProfile profile = profiles.get(index);
            if (profile.getName().equals(selectedProfileName)) {
                profileToDelete = profile;
            }
            index++;
        }

        if (profileToDelete != null) {
            profiles.remove(profileToDelete);
            ProfileManager.deleteProfile(profileToDelete.getPlayerId());
            dialog.close();
        }
    }

    /**
     * Sets up the game interface and initializes everything for a game to take place.
     * @param primaryStage the primary stage for the game
     * @param levelFile the file of the level being loaded in
     */
    public void setupGame(Stage primaryStage, String levelFile, boolean autoStart) {
        String[][] initialGrid = FileHandler.readElementGridFromLevelFile(levelFile);
        int amoebaGrowthRate = FileHandler.readAmoebaGrowthRateFromLevelFile(levelFile); //Read amoeba growth rate
        secondsRemaining = FileHandler.readSecondsFromLevelFile(levelFile);

        final int canvasWidth = initialGrid[0].length * GRID_CELL_WIDTH;
        final int canvasHeight = initialGrid.length * GRID_CELL_HEIGHT;

        Canvas canvas = new Canvas(canvasWidth, canvasHeight);

        GameController gameController = initializeGameController(initialGrid, canvas, levelFile);

        Pane root = buildGUI(gameController);

        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            gameController.registerInput(event.getCode());
            event.consume();
        });

        KeyFrame playerKeyFrame = new KeyFrame(Duration.millis(150), event -> {
            gameController.playerTick();
        });

        KeyFrame killPlayerKeyFrame = new KeyFrame(Duration.millis(50), event -> {
            gameController.killTick();
        });

        KeyFrame dangerousRocksRollKeyFrame = new KeyFrame(Duration.millis(120), event -> {
            gameController.dangerousRockRollTick();
        });

        KeyFrame dangerousRocksFallKeyFrame = new KeyFrame(Duration.millis(100), event -> {
            gameController.dangerousRockFallTick();

        });

        KeyFrame flyKeyFrame = new KeyFrame(Duration.millis(2000), event -> {
            gameController.flyTick();
        });

        KeyFrame frogKeyFrame = new KeyFrame(Duration.millis(2000), event -> {
            gameController.frogTick();
        });

        KeyFrame amoebaKeyFrame = new KeyFrame(Duration.millis(amoebaGrowthRate), event -> {
            gameController.amoebaTick();
        });

        KeyFrame explosionKeyFrame = new KeyFrame(Duration.millis(1000), event -> {
            gameController.explosionTick();
        });

        KeyFrame checkLevelWinKeyFrame = new KeyFrame(Duration.millis(49), event -> {
            if (gameController.checkLevelWinTick()) {
                levelCompleted(gameController);
            }
        });

        playerTickTimeline = new Timeline(playerKeyFrame);
        dangerousRockFallTickTimeline = new Timeline(dangerousRocksFallKeyFrame);
        dangerousRockRollTimeline = new Timeline(dangerousRocksRollKeyFrame);
        flyTickTimeline = new Timeline(flyKeyFrame);
        frogTickTimeline = new Timeline(frogKeyFrame);
        amoebaTickTimeline = new Timeline(amoebaKeyFrame);
        killPlayerTickTimeLine = new Timeline(killPlayerKeyFrame);
        explosionTickTimeLine = new Timeline(explosionKeyFrame);
        checkLevelWinTimeline = new Timeline(checkLevelWinKeyFrame);

        setTimelinesToIndefinite();

        gameController.draw();

        primaryStage.setScene(scene);
        primaryStage.show();

        if (autoStart) {
            playAllTimelines();
        }
    }

    /**
     * Build GUI to visualise the game.
     * Sets up layout of game in the center and buttons/game info above it.
     * @param gameController  the gameController managing the game logic and state.
     * @return the Pane of the GUI layout.
     */
    private Pane buildGUI(GameController gameController) {
        BorderPane root = new BorderPane();

        // Add the canvas to the center
        root.setCenter(gameController.getCanvas());

        // Create a toolbar with buttons
        HBox toolbar = new HBox(SPACING);
        toolbar.setPadding(new Insets(SPACING));

        Button startTickButton = new Button("Resume");
        Button stopTickButton = new Button("Pause");
        stopTickButton.setDisable(true);
        Button saveButton = new Button("Save Game");

        Text timerText = new Text("Time Remaining: " + secondsRemaining + "s");

        Button resetGridButton = new Button("Reset Level");
        resetGridButton.setOnAction(e -> {
            int levelReached = currentProfile.getMaxLevelReached();
            String levelFile = "src/main/resources/txt/Level" + levelReached + ".txt";
            secondsRemaining = FileHandler.readSecondsFromLevelFile(levelFile);
            gameController.getPlayer().setDiamondCount(FileHandler.readDiamondsCollectedFromLevelFile(levelFile));
            gameController.getPlayer().setKeyInventory(FileHandler.readKeyInventoryFromLevelFile(levelFile));
            String[][] initialGrid = FileHandler.readElementGridFromLevelFile(levelFile);
            gameController.getGridManager().reinitializeGrid(initialGrid);
            gameController.getGridManager().initializePlayer(initialGrid);
            gameController.setAmoebaLimit(FileHandler.readAmoebaSizeLimitFromLevelFile(levelFile));
            timerText.setText("Time Remaining: " + secondsRemaining + "s");
            gameController.draw();
        });

        saveButton.setOnAction(e -> {
            ArrayList<KeyColour> keyInventory = gameController.getPlayer().getKeyInventory();
            FileHandler.writeFile(gameController, currentProfile, secondsRemaining, keyInventory);
            closeGame();
        });

        startTickButton.setOnAction(e -> {
            playAllTimelines();
            startTickButton.setDisable(true);
            stopTickButton.setDisable(false);
            saveButton.setDisable(true);
            resetGridButton.setDisable(true);
        });

        stopTickButton.setOnAction(e -> {
            timerTimeline.stop();
            diamondCountTimeline.stop();
            playerTickTimeline.stop();
            dangerousRockRollTimeline.stop();
            dangerousRockFallTickTimeline.stop();
            flyTickTimeline.stop();
            frogTickTimeline.stop();
            amoebaTickTimeline.stop();
            killPlayerTickTimeLine.stop();
            explosionTickTimeLine.stop();
            checkLevelWinTimeline.stop();
            stopTickButton.setDisable(true);
            startTickButton.setDisable(false);
            saveButton.setDisable(false);
            resetGridButton.setDisable(false);
        });

        timerTimeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            secondsRemaining--;
            timerText.setText("Time Remaining: " + secondsRemaining + "s");
            if (secondsRemaining == 0) {
                gameController.getGridManager().killPlayer();
                gameController.draw();
                timerTimeline.stop();
            }
        }));
        timerTimeline.setCycleCount(Animation.INDEFINITE);

        int diamondsCollected = gameController.getPlayer().getDiamondCount();
        int diamondsRequired = FileHandler.readRequiredDiamondsFromLevelFile(
                "src/main/resources/txt/Level" + currentProfile.getMaxLevelReached() + ".txt");
        Text diamondCountText = new Text("Diamonds Collected: " + diamondsCollected + " / " + diamondsRequired);
        diamondCountTimeline = new Timeline(new KeyFrame(Duration.millis(49), event -> {
            if (gameController.getPlayer() != null) {
                diamondCountText.setText("Diamonds collected: "
                        + gameController.getPlayer().getDiamondCount() + " / " + diamondsRequired);
            } else {
                diamondCountText.setText("Diamonds collected: 0 / " + diamondsRequired);
            }
        }));
        diamondCountTimeline.setCycleCount(Animation.INDEFINITE);

        Text levelText = new Text("Current Level: " + currentProfile.getMaxLevelReached());

        toolbar.getChildren().addAll(startTickButton, stopTickButton, resetGridButton,
                saveButton, timerText, diamondCountText, levelText);
        root.setTop(toolbar);

        return root;
    }

    /**
     * Closes the game.
     */
    private void closeGame() {
        System.exit(0);
    }

    /**
     * Handles the completion of the current level.
     * Displays High score after level complete.
     * If there is another level, next level will load.
     * If the player has beaten the final level then a victory pop up occurs.
     * @param gameController  the gameController managing the game logic and state.
     */
    public void levelCompleted(GameController gameController) {
        int levelReached = currentProfile.getMaxLevelReached();
        String levelFile = "src/main/resources/txt/Level" + levelReached + ".txt";
        String[][] initialGrid = FileHandler.readElementGridFromLevelFile(levelFile);
        int score = calcScore(secondsRemaining, gameController.getPlayer().getDiamondCount());
        gameController.getGridManager().reinitializeGrid(initialGrid);
        gameController.getGridManager().initializePlayer(initialGrid);

        playerTickTimeline.stop();
        killPlayerTickTimeLine.stop();
        dangerousRockFallTickTimeline.stop();
        dangerousRockRollTimeline.stop();
        flyTickTimeline.stop();
        frogTickTimeline.stop();
        amoebaTickTimeline.stop();
        explosionTickTimeLine.stop();
        timerTimeline.stop();

        // Show the high score table for level just beat
        int currentLevel = currentProfile.getMaxLevelReached();
        String currentPlayerName = currentProfile.getName();
        HighScoreTableManager.updateHighScoreTable(currentPlayerName, score, currentLevel);
        Platform.runLater(() -> {
            HighScoreTableManager.displayHighScoresAfterLevel(currentLevel, score);
        });

        // Check if there’s a next level
        int nextLevel = currentLevel + 1;
        if (nextLevel <= 3) {
            String nextLevelFile = "src/main/resources/txt/Level" + nextLevel + ".txt";
            currentProfile.setMaxLevelReached(nextLevel); // Update player's progress
            profiles.set(profiles.indexOf(currentProfile), currentProfile); // Update profile list
            ProfileManager.saveProfileToFile(currentProfile); // Persist changes

            secondsRemaining = FileHandler.readSecondsFromLevelFile(nextLevelFile);
            setupGame(primaryStage, nextLevelFile, false);
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("VICTORY");
            alert.setHeaderText("Congratulations!");
            alert.setContentText("You have completed all levels!");
            alert.show();

            profiles.remove(currentProfile);
            ProfileManager.deleteProfile(currentProfile.getPlayerId());
            start(primaryStage);
        }
    }

    /**
     * Calculates the score for the level the player just beat
     * @param secondsRemaining the remaining seconds on the timer when the level is completed.
     * @param diamondsCollected the number of diamonds collected by the player when the level is completed.
     * @return the calculated score as an integer value.
     */
    public int calcScore(int secondsRemaining, int diamondsCollected) {
        return (diamondsCollected * DIAMOND_SCORE_VALUE) + (secondsRemaining * TIME_SCORE_VALUE);
    }

    /**
     * Starts all the timelines.
     */
    private void playAllTimelines() {
        timerTimeline.play();
        diamondCountTimeline.play();
        playerTickTimeline.play();
        dangerousRockFallTickTimeline.play();
        dangerousRockRollTimeline.play();
        flyTickTimeline.play();
        frogTickTimeline.play();
        amoebaTickTimeline.play();
        killPlayerTickTimeLine.play();
        explosionTickTimeLine.play();
        checkLevelWinTimeline.play();
    }

    /**
     * Sets all the timelines associated with game events to cycle indefinitely.
     */
    private void setTimelinesToIndefinite() {
        playerTickTimeline.setCycleCount(Animation.INDEFINITE);
        killPlayerTickTimeLine.setCycleCount(Animation.INDEFINITE);
        dangerousRockFallTickTimeline.setCycleCount(Animation.INDEFINITE);
        dangerousRockRollTimeline.setCycleCount(Animation.INDEFINITE);
        flyTickTimeline.setCycleCount(Animation.INDEFINITE);
        frogTickTimeline.setCycleCount(Animation.INDEFINITE);
        amoebaTickTimeline.setCycleCount(Animation.INDEFINITE);
        timerTimeline.setCycleCount(Animation.INDEFINITE);
        explosionTickTimeLine.setCycleCount(Animation.INDEFINITE);
        checkLevelWinTimeline.setCycleCount(Animation.INDEFINITE);
    }

    /**
     * Initializes the game controller and sets its properties.
     * @param initialGrid the 2D array of the initial state of the game grid.
     * @param canvas the canvas used for drawing the game.
     * @param levelFile the file containing the level's data.
     * @return the GameController with key data set.
     */
    private GameController initializeGameController(String[][] initialGrid, Canvas canvas, String levelFile) {
        GameController gameController = new GameController(initialGrid, canvas);

        gameController.setDiamondsRequired(FileHandler.readRequiredDiamondsFromLevelFile(levelFile));
        gameController.getPlayer().setDiamondCount(FileHandler.readDiamondsCollectedFromLevelFile(levelFile));
        gameController.getPlayer().setKeyInventory(FileHandler.readKeyInventoryFromLevelFile(levelFile));
        gameController.setAmoebaLimit(FileHandler.readAmoebaSizeLimitFromLevelFile(levelFile));

        return gameController;
    }

    /**
     * Launch Boulder-Dash-Remake.
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
}
