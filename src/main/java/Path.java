/**
 * Represents a Path tile in the game.
 * This tile can be entered/walked over and can explode.
 * @author Omar Sanad
 * @author Alex Vesely
 */
public class Path extends Element {

    /**
     * Creates a new Path tile at the specified row and column.
     * @param row the row position (x co-ord) of the tile
     * @param column the column position(y co-ord)  of the tile
     */
    public Path(final int row, final int column) {
        super(row, column);
        canExplode = true;
        canBeEntered = true;
        name = "Path";
    }

    /**
     * Returns the name of the tile as a string.
     * @return the name "Path"
     */
    @Override
    public String toString() {
        return "Path";
    }
}
