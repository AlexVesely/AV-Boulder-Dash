import javafx.scene.image.Image;

/**
 * Represents a Dirt tile in the game.
 * This tile can explode and be walked over.
 * @author Alex Vesely
 * @author Omar Sanad
 */
public class Dirt extends Element {

    /**
     * Creates new Dirt tiles at the specified row and column.
     * @param row the row position of the tile
     * @param column the column  position of the tile
     */
    public Dirt(final int row, final int column) {
        super(row, column);
        canExplode = true;
        image = new Image("images/dirt.png");
        canBeEntered = true;
        name = "Dirt";
    }

    /**
     * Returns a string representation of the dirt tile.
     * @return "dirt"
     */
    @Override
    public String toString() {
        return "dirt";
    }
}
