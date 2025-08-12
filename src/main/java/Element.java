import javafx.scene.image.Image;

/**
 * Represents an abstract base class for elements within the grid-based game.
 * The Element class maintains attributes for its position, image,
 * and properties such as whether it can explode or be entered by a player.
 * @author Omar Sanad
 * @author Alex Vesely
 */
public abstract class Element {

    protected String name;
    protected Image image;
    protected int column;
    protected int row;
    protected boolean canExplode;
    protected boolean canBeEntered;
    protected GridManager gridManager;

    /**
     * Constructs an Element object with specified row and column positions.
     * @param row the row position of the element in the grid.
     * @param column the column position of the element in the grid.
     */
    public Element(int row, int column) {
        this.column = column;
        this.row = row;
    }

    /**
     * Retrieves the name of the element.
     * @return the name of the element as a String.
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the row position of the element within the grid.
     * @return the row position as an integer.
     */
    public int getRow() {
        return row;
    }

    /**
     * Sets the row position of the element within the grid.
     * @param row the new row position to be set.
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * Retrieves the column position of the element within the grid.
     * @return the column position as an integer.
     */
    public int getColumn() {
        return column;
    }

    /**
     * Sets the column position of the element within the grid.
     * @param column the new column position to be set.
     */
    public void setColumn(int column) {
        this.column = column;
    }

    public Image getImage() {
        return image;
    }

    /**
     * Sets the image representation of this Element.
     * @param image the Image object to be set for this Element's visual representation.
     */
    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * Checks whether the element is capable of exploding.
     * @return true if the element can explode, false otherwise.
     */
    public boolean isCanExplode() {
        return canExplode;
    }

    /**
     * Determines whether the element can be entered by a player.
     * @return true if the element is enterable, false otherwise.
     */
    public boolean isCanBeEntered() {
        return canBeEntered;
    }

    /**
     * Returns a string representation of the element.
     * @return the name of the element as a string.
     */
    @Override
    public String toString() {
        return name;
    }
}

