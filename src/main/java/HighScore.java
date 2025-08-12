/**
 * This represents a high score entry in the game, storing the player's name and score.
 * @author Tahi Rahman
 * @author Alex Vesely
 */
public class HighScore {
    private int score;
    private String name;

    /**
     * Constructor  makes a new HighScore with the specified name and score.
     * @param name the name of the player who is getting a score
     * @param score the score achieved by the player
     */
    public HighScore(String name, int score) {
        this.score = score;
        this.name = name;
    }

    /**
     * Gets the score achieved by the player.
     * @return the player's score
     */
    public int getScore() {
        return score;
    }

    /**
     * Sets the score achieved by the player.
     * @param score the new score of the player
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Gets the name of the player.
     * @return the player's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the player.
     * @param name the new name of the player
     */
    public void setName(String name) {
        this.name = name;
    }
}