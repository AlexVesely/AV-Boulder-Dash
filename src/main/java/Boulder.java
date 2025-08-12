import javafx.scene.image.Image;

/**
 * This class represents a boulder in the game, which is a DangerousRock.
 * A boulder can gain momentum, fall, and interact with other elements.
 * @author Omar Sanad
 */
public class Boulder extends  DangerousRock {

    /**
     * Constructs a Boulder positioned at a specified row and column.
     * @param row the row position of the boulder in the grid.
     * @param column the column position of the boulder in the grid.
     */
    public Boulder(int row, int column) {
        super(row, column);
        image = new Image("images/boulder.png");
        name = "Boulder";
    }

    @Override
    public String toString() {
        return "Boulder";
    }
}
