import javafx.scene.image.Image;

/**
 * This class represents Butterflies, a type of Fly.
 * Butterflies follow either right/left edge and drop diamonds when exploded.
 * @author Karamveer Singh
 */
public class Butterfly extends Fly {
    public static final Boolean dropDiamond = true;

    /**
     * Constructs a Butterfly with the specified row, column, and edge following behaviour.
     * @param row             The initial row position of the Butterfly.
     * @param column          The initial column position of the Butterfly.
     * @param followsLeftEdge True if the Butterfly follows the left edge, false if it follows the right edge.
     */
    public Butterfly(int row, int column, boolean followsLeftEdge) {
        super(row, column, followsLeftEdge);
        image = new Image("images/butterfly.png");
        this.followsLeftEdge = followsLeftEdge;
        name = getName();
    }

    /**
     * Gets the name of the Butterfly
     * @return The name of the Butterfly.
     */
    public String getName() {
        String extraInfo;
        if (followsLeftEdge) {
            extraInfo = "Left";
        } else {
            extraInfo = "Right";
        }
        return "Butterfly" + extraInfo;
    }

    /**
     * Returns a string representation of the Butterfly.
     * @return "Butterfly".
     */
    @Override
    public String toString() {
        return "Butterfly";
    }
}
