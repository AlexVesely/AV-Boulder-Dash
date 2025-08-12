import javafx.scene.image.Image;

/**
 * Class represents Flies within the game. That move
 * uniquely by following either the left or right edge.
 * @author Karamveer Singh
 */
public class Fly extends Element {
    public static final int POSSIBLE_DIRECTIONS = 4;
    public static final int FEWER_POSSIBLE_DIRECTIONS = 3;
    protected boolean followsLeftEdge;
    private int currentDirection;

    private static final int[][] DIRECTIONS = {
            {-1, 0}, // Up
            {0, 1},  // Right
            {1, 0},  // Down
            {0, -1}  // Left
    };

    /**
     * Creates a new Fly object at the specified row and column.
     * @param row the row of the new Fly
     * @param column the column of the new Fly
     * @param followsLeftEdge whether the Fly follows the left edge
     */
    public Fly(final int row, final int column, final boolean followsLeftEdge) {
        super(row, column);
        image = new Image("images/butterfly.png");
        canBeEntered = false;
        canExplode = true;
        name = "fly";
        this.followsLeftEdge = followsLeftEdge;
        this.currentDirection = 0; // Start with "Up" direction
    }

    /**
     * Moves the Fly to the next valid position in the grid,
     * based on its current direction and the grid's contents.
     * If the Fly reaches a Path, it moves to the new position.
     * If the Fly reaches a Player, it kills the player.
     * @param gridManager the grid manager to update the Fly's position
     * @param player the player to check for collision
     */
    public void move(final GridManager gridManager, final Player player) {
        Element[][] grid = gridManager.getElementGrid();

        // Determine the next valid direction
        int nextDirection = getNextDirection(grid);
        if (nextDirection != -1) {
            // Calculate new position
            int newRow = this.getRow() + DIRECTIONS[nextDirection][0];
            int newCol = this.getColumn() + DIRECTIONS[nextDirection][1];

            Element target = grid[newRow][newCol];
            // Move to new position if the target is a Path
            if (target instanceof Path) {
                gridManager.setElement(this.getRow(), this.getColumn(),
                        new Path(this.getRow(), this.getColumn()));
                // Move to new position
                gridManager.setElement(newRow, newCol, this);
                this.setRow(newRow);
                this.setColumn(newCol);
                // Update the current direction
                this.currentDirection = nextDirection;

                // If the target is a Player, kill the player
            } else if (target instanceof Player) {
                gridManager.setElement(this.getRow(), this.getColumn(),
                        new Path(this.getRow(), this.getColumn()));
                // Replace player with Frog
                gridManager.setElement(newRow, newCol, this);
                // Remove player from the game
                gridManager.destroyRemoveFromList(player);
                this.setRow(newRow);
                this.setColumn(newCol);
                System.out.println("Player has been killed by the fly!");

            } else if (target instanceof Amoeba) {
                // explode!!!!!
            }
        }
    }

    /**
     * Determines the next direction for the fly to move based on the rule.
     * The fly will follow either the left or right edge,
     * depending on the configuration.
     * @param grid the 2D array representing the current state of the grid
     * @return the next direction index for the fly to move,
     * or -1 if no valid move is found
     */
    private int getNextDirection(final Element[][] grid) {
        int direction = currentDirection;

        // Check the wall-following rule
        for (int i = 0; i < POSSIBLE_DIRECTIONS; i++) {
            int checkDirection = followsLeftEdge ? (direction + FEWER_POSSIBLE_DIRECTIONS)
                    % POSSIBLE_DIRECTIONS : (direction + 1) % POSSIBLE_DIRECTIONS;
            int row = this.getRow() + DIRECTIONS[checkDirection][0];
            int col = this.getColumn() + DIRECTIONS[checkDirection][1];

            // If valid tile to move
            if (isValidMove(grid, row, col)) {
                return checkDirection;
            }

            // Rotate fly's direction clockwise or counterclockwise
            direction = followsLeftEdge ? (direction + 1)
                    % POSSIBLE_DIRECTIONS : (direction + FEWER_POSSIBLE_DIRECTIONS) % POSSIBLE_DIRECTIONS;
        }

        // No valid move found
        return -1;
    }


    /**
     * Determines if the fly can move to the specified position in the grid.
     * @param grid the 2D array representing the current state of the grid
     * @param row  the row index of the target position
     * @param col  the column index of the target position
     * @return true if the fly can move to the target position, false otherwise
     */
    private boolean isValidMove(final Element[][] grid,
                                final int row, final int col) {
        // Check boundaries
        if (row < 0 || row >= grid.length || col < 0 || col >= grid[0].length) {
            return false;
        }

        // Tile must be a Path, Player, or Amoeba
        Element target = grid[row][col];
        return (target instanceof Path
                || target instanceof Player || target instanceof Amoeba);
    }

    /**
     * Returns a string representation of the Fly object.
     * @return "fly"
     */
    @Override
    public String toString() {
        return "fly";
    }
}
