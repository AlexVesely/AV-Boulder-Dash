import javafx.scene.image.Image;

/**
 * This class represents an amoeba element on the game grid.
 * @author Karamveer Singh
 */
public class Amoeba extends Element {

    /**
     * Creates a new Amoeba object with the specified row and column.
     * @param row the row of the Amoeba in the grid
     * @param column the column of the Amoeba in the grid
     */
    public Amoeba(final int row, final int column) {
        super(row, column);
        image = new Image("images/amoeba.png");
        canBeEntered = false;
        canExplode = true;
        name = "Amoeba";
    }

    /**
     * Returns a string representation of the Amoeba object.
     * @return a string "Amoeba"
     */
    @Override
    public String toString() {
        return "Amoeba";
    }
}
