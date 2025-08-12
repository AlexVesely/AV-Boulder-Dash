import javafx.scene.image.Image;

/**
 * The Firefly class represents a type of fly, that does not drop diamonds when exploded
 * @author Karamveer Singh
 */
public class Firefly extends Fly{
    public static final Boolean dropDiamond = false;
    public Firefly(int row, int column, boolean followsLeftEdge) {
        super(row, column, followsLeftEdge);
        image = new Image("images/firefly.png");
        this.followsLeftEdge = followsLeftEdge;
        name = getName();
    }

    /**
     * Gets the name of the Firefly
     * @return The name of the Firefly.
     */
    public String getName(){
        String extraInfo;
        if (followsLeftEdge) {
            extraInfo = "Left";
        } else {
            extraInfo = "Right";
        }
        return "Firefly" + extraInfo;
    }

    /**
     * Return a string representation of a FireFly.
     * @return "Firefly"
     */
    @Override
    public String toString() {
        return "Firefly";
    }
}
