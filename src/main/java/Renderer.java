import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Renderer is responsible for rendering the game's grid and its elements onto the canvas.
 * @author Omar Sanad
 * @author Alex Vesely
 */
public class Renderer {
    private static final int GRID_CELL_WIDTH = Main.GRID_CELL_WIDTH;
    private static final int GRID_CELL_HEIGHT = Main.GRID_CELL_HEIGHT;

    /**
     * Renders the provided grid of elements onto the canvas.
     * Each element in the grid is drawn at its respective grid position.
     * @param gc the GraphicsContext object used for drawing.
     * @param elementGrid 2D array of Element objects representing the grid to be drawn.
     */
    public void draw(GraphicsContext gc, Element[][] elementGrid) {
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        gc.setFill(Color.GRAY);
        gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

        // Draw all elements
        for (int row = 0; row < elementGrid.length; row++) {
            for (int col = 0; col < elementGrid[row].length; col++) {
                Element element = elementGrid[row][col];
                if (element.getImage() != null) {
                    gc.drawImage(element.getImage(), col * GRID_CELL_WIDTH, row * GRID_CELL_HEIGHT);
                }
            }
        }
    }
}
