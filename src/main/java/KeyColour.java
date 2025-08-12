/**
 * Represents the colours of keys in the game.
 * Each colour corresponds to a different type of key.
 *  @author Joshua Aka
 */
public enum KeyColour {
    RED, GREEN, YELLOW, BLUE, RAINBOW;

    /**
     * Gets name of key colour.
     * @return the name of key colour as a string.
     */
    public String getName() {
        return name();
    }
}
