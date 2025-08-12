/**
 * Represents a PlayerProfile, storing the player's name, id and max level reached.
 * @author Tahi Rahman
 */
public class PlayerProfile {

    private final int playerId;
    private String name;
    private int maxLevelReached;

    /**
     * Constructs a new PlayerProfile with the specified name, maximum level reached, and high score.
     * Each profile is assigned a unique player ID.
     * @param name the name of the player
     */
    public PlayerProfile(String name) {
        this.playerId = ProfileManager.getNextPlayerId();
        this.name = name;
        this.maxLevelReached = 1;
    }

    /**
     * Constructs a new PlayerProfile with the specified name, maximum level reached, and high score.
     * Each profile is assigned a unique player ID.
     */
    public PlayerProfile() {
        this.playerId = ProfileManager.getNextPlayerId();
        this.maxLevelReached = 1;
    }

    /**
     * Constructs a new PlayerProfile with the specified name, maximum level reached, and high score.
     * Each profile is assigned a unique player ID.
     *
     */
    public PlayerProfile(int playerId, String name, int maxLevelReached) {
        this.playerId = playerId;
        this.name = name;
        this.maxLevelReached = maxLevelReached;
    }

    /**
     * Gets the player's unique ID.
     * @return the player's ID
     */
    public int getPlayerId() {
        return playerId;
    }

    /**
     * Gets the player's name.
     * @return the player's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the player's name.
     * @param name the new name of the player
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the maximum level reached by the player.
     * @return the maximum level reached
     */
    public int getMaxLevelReached() {
        return maxLevelReached;
    }

    /**
     * Sets the maximum level reached by the player.
     * @param maxLevelReached the new maximum level reached
     */
    public void setMaxLevelReached(int maxLevelReached) {
        this.maxLevelReached = maxLevelReached;
    }

}