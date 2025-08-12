import javafx.scene.image.Image;

/**
 * Represents an explosion element within a grid-based game.
 * An Explosion object occupies a grid cell and impacts surrounding cells,
 * potentially transforming them into other elements such as paths or diamonds.
 * @author Omar Sanad
 * @author Alex Vesely
 */
public class Explosion extends Element {

    /**
     * Constructs an Explosion object at the specified grid position.
     * @param row the row position of the explosion.
     * @param column the column position of the explosion.
     */
    public Explosion(int row, int column) {
        super(row, column);
        image = new Image("images/explosion.png");
        canBeEntered = false;
        canExplode = true;
        name = "Explosion";
    }

    /**
     * Creates an explosion effect in a 3x3 grid area around the specified index.
     * This method replaces explodable tiles within the area with explosion tiles.
     * @param row the row index of the center tile around which the explosion occurs.
     * @param col the column index of the center tile around which the explosion occurs.
     * @param gridManager the grid manager that manages the grid of elements.
     */
    public static void createExplosion(final int row, final int col,
                                       final GridManager gridManager) {
        Element[][] grid = gridManager.getElementGrid();

        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = col - 1; j <= col + 1; j++) {
                if (i >= 0 && i < grid.length && j >= 0 && j < grid[0].length) {
                    Element element = gridManager.getElement(i, j);
                    if (element.isCanExplode()) {
                        // If the element is an amoeba, remove it from its group
                        if (element instanceof Amoeba amoeba) {
                            AmoebaGroup group = findAmoebaGroup(amoeba, gridManager);
                            if (group != null) {
                                group.removeAmoeba(amoeba);
                            }
                        }

                        // Replace the element with an explosion
                        gridManager.removeFromList(element);
                        Explosion explosion = new Explosion(i, j);
                        gridManager.setElement(i, j, explosion);
                    }
                }
            }
        }
    }

    /**
     * Replaces explosion elements with paths in a 3x3 grid area
     * around the specified index. This method should only be called
     * after an explosion has occurred in the grid.
     * @param row the row index of the center tile around
     *            which exploded elements will be replaced.
     * @param col the column index of the center tile around
     *            which exploded elements will be replaced.
     * @param gridManager the grid manager that manages the grid of elements.
     */
    public static void createExplosionAfterMath(int row, int col, GridManager gridManager) {
        Element[][] grid = gridManager.getElementGrid();
        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = col - 1; j <= col + 1; j++) {
                if (i >= 0 && i < grid.length && j >= 0 && j < grid[0].length) {
                    if (gridManager.getElement(i, j).isCanExplode()) {
                        Path path = new Path(i, j);
                        gridManager.setElement(i, j, path);
                    }
                }
            }
        }
    }

    /**
     * Transforms exploded elements into diamonds within a 3x3 grid
     * area centered at the specified index. This method should only be called
     * after an explosion has occurred in the grid.
     * @param row the row index of the grid's center tile where
     *            the diamond effect is to be applied.
     * @param col the column index of the grid's center tile
     *            where the diamond effect is to be applied.
     * @param gridManager the grid manager that manages the grid of elements.
     */
    public static void createDiamondExplosionAfterMath(int row, int col, GridManager gridManager) {
        Element[][] grid = gridManager.getElementGrid();
        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = col - 1; j <= col + 1; j++) {
                if (i >= 0 && i < grid.length && j >= 0 && j < grid[0].length) {
                    if (gridManager.getElement(i, j).isCanExplode()) {
                        Diamond diamond = new Diamond(i, j);
                        gridManager.addToList(diamond);
                        gridManager.setElement(i, j, diamond);
                    }
                }
            }
        }
    }

    /**
     * Finds and returns the AmoebaGroup that contains the given amoeba.
     * @param amoeba the Amoeba object to search for.
     * @param gridManager the GridManager containing the list of AmoebaGroups.
     * @return the AmoebaGroup containing the amoeba,
     * or null if no such group exists.
     */
    private static AmoebaGroup findAmoebaGroup(final Amoeba amoeba,
                                               final GridManager gridManager) {
        for (AmoebaGroup group : gridManager.getAmoebaGroups()) {
            if (group.contains(amoeba)) {
                return group;
            }
        }
        return null;
    }
}

