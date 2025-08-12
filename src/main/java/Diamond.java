import javafx.scene.image.Image;

/**
 * Represents a diamond within the grid-based game, which is a type of dangerous rock.
 * The diamond can fall and roll within the grid and interacts with other elements.
 * @author Omar Sanad
 */
public class Diamond extends DangerousRock {

    /**
     * Constructs a Diamond object with specified row and column positions.
     * @param row the row position of the diamond in the grid.
     * @param column the column position of the diamond in the grid.
     */
    public Diamond(int row, int column) {
        super(row, column);
        image = new Image("images/diamond.png");
        name = "Diamond";
    }

    /**
     * Returns a String representation of a diamond.
     * @return "Diamond".
     */
    @Override
    public String toString() {
        return "Diamond";
    }
}
