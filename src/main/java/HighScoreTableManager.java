import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Manages the high scores for the game. Allows new entries to be added
 * and saved to a level's high score table.
 * @author Alex Vesely
 * @author Tahi Rahman
 */
public class HighScoreTableManager {
    private static final int MAX_HIGH_SCORES = 10;
    private static final int FINAL_LEVEL = 3;
    private static final int HIGH_SCORE_TABLE_WIDTH = 400;
    private static final int HIGH_SCORE_TABLE_HEIGHT = 500;
    private static final int SPACING = 10;

    /**
     * Displays a level's high score table when accessed from the main menu.
     * @param level        The level's high score table.
     * @param parentDialog The dialog that will be shown when this is closed.
     */
    public static void displayHighScoresInMainMenu(int level, Stage parentDialog) {
        Stage dialog = new Stage();
        dialog.setTitle("High Score Table for Level " + level);
        dialog.initModality(Modality.APPLICATION_MODAL);

        VBox vbox = new VBox(SPACING);
        vbox.setStyle("-fx-padding: 20; -fx-alignment: center;");

        ArrayList<HighScore> highScores = HighScoreTableManager.getHighScores(level);

        for (HighScore highScore : highScores) {
            Label scoreLabel = new Label(highScore.getName() + " : " + highScore.getScore());
            scoreLabel.setStyle("-fx-font-size: 18px; -fx-text-alignment: center;");
            vbox.getChildren().add(scoreLabel);
        }

        Button backButton = new Button("Back to High Scores");
        backButton.setOnAction(event -> {
            dialog.close();
            parentDialog.show();
        });

        vbox.getChildren().add(backButton);
        Scene dialogScene = new Scene(vbox, HIGH_SCORE_TABLE_WIDTH, HIGH_SCORE_TABLE_HEIGHT);
        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }

    /**
     * Displays the high score table for a specific level after the player beats a level.
     * @param level        The level for which to display the high scores.
     * @param scoreAchieved The score the player achieved in the level.
     */
    public static void displayHighScoresAfterLevel(int level, int scoreAchieved) {
        Stage dialog = new Stage();
        dialog.setTitle("High Score Table for Level " + level);
        dialog.initModality(Modality.APPLICATION_MODAL);

        VBox vbox = new VBox(SPACING);
        vbox.setStyle("-fx-padding: 20; -fx-alignment: center;");

        ArrayList<HighScore> highScores = HighScoreTableManager.getHighScores(level);

        for (HighScore highScore : highScores) {
            Label scoreLabel = new Label(highScore.getName() + " : " + highScore.getScore());
            scoreLabel.setStyle("-fx-font-size: 18px; -fx-text-alignment: center;");
            vbox.getChildren().add(scoreLabel);
        }

        Label addSpace = new Label();
        addSpace.setMinHeight(SPACING);
        vbox.getChildren().add(addSpace);

        Label recentScore = new Label("Your Score: " + scoreAchieved);
        recentScore.setStyle("-fx-font-size: 18px; -fx-text-alignment: center;");

        vbox.getChildren().add(recentScore);

        String buttonText;

        if (level == FINAL_LEVEL) {
            buttonText = "Close";
        } else {
            buttonText = "Begin Next Level";
        }

        Button backButton = new Button(buttonText);
        backButton.setOnAction(event -> {
            dialog.close();
        });

        vbox.getChildren().add(backButton);

        Scene dialogScene = new Scene(vbox, HIGH_SCORE_TABLE_WIDTH, HIGH_SCORE_TABLE_HEIGHT);
        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }

    /**
     * Updates the high score table of a specific level with the score a player has achieved.
     * Updates so that only the 10 highest scores are maintained
     * @param playerName The name of the player achieving the score.
     * @param score      The score achieved by the player.
     * @param level      The level for which the high score table should be updated.
     */
    public static void updateHighScoreTable(String playerName, int score, int level) {
        HighScore newHighScore = new HighScore(playerName, score);
        ArrayList<HighScore> highScores = getHighScores(level);
        highScores.add(newHighScore);
        highScores.sort((h1, h2) -> Integer.compare(h2.getScore(), h1.getScore()));
        if (highScores.size() > MAX_HIGH_SCORES) {
            highScores.remove(MAX_HIGH_SCORES);
        }
        saveHighScoreTable(highScores, level);
    }


    /**
     * Retrieves the high scores for a specific level.
     * @param level The level for which to retrieve the high scores.
     * @return A list of high scores for the specified level.
     */
    public static ArrayList<HighScore> getHighScores(int level) {
        ArrayList<HighScore> highScores = new ArrayList<>();
        String fileName = "/txt/HighScoreTable" + level + ".txt";

        try (InputStream inputStream = HighScoreTableManager.class.getResourceAsStream(fileName);
             Scanner scanner = new Scanner(inputStream)) {

            while (scanner.hasNextLine()) {
                String[] splitHighScore = scanner.nextLine().split(" ");
                String name = splitHighScore[0];
                int score = Integer.parseInt(splitHighScore[1]);
                HighScore highScore = new HighScore(name, score);
                highScores.add(highScore);
            }

        } catch (Exception e) {
            System.out.println("Error reading high scores from " + fileName + ": " + e.getMessage());
        }

        return highScores;
    }


    /**
     * Saves a level's high score table to its high score table file.
     * @param highScores The list of high scores to save.
     * @param level      The level for which the High Score Table is of
     */
    public static void saveHighScoreTable(ArrayList<HighScore> highScores, int level) {
        try {
            String outputFile = "txt/HighScoreTable" + level + ".txt";
            PrintWriter out = new PrintWriter(outputFile);
            for (int i = 0; i < highScores.size(); i++) {
                HighScore highScore = highScores.get(i);
                out.print(highScore.getName() + " " + highScore.getScore());
                if (i < highScores.size() - 1) {
                    out.println();
                }
            }
            out.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
