/**
 * Represents a dangerous rock within the game. This rock
 * can potentially cause harm to entities like players, frogs, or flies
 * when falling or rolling, and can pass through magic walls to transform.
 * @author Omar Sanad
 */
public abstract class DangerousRock extends Element {

    private boolean hasMomentum = false;

    /**
     * Constructs a DangerousRock with specified row and column positions.
     * @param row the row position of the dangerous rock in the grid.
     * @param column the column position of the dangerous rock in the grid.
     */
    public DangerousRock(int row, int column) {
        super(row, column);
        canExplode = true;
        canBeEntered = false;
    }
    /**
     * Gains momentum when the boulder falls.
     */
    public void gainMomentum() {
        hasMomentum = true;
    }

    /**
     * Handles the falling logic for the rock.
     * @param gridManager the grid manager to access and update the grid
     */
    public void fall(GridManager gridManager) {
        Element[][] grid = gridManager.getElementGrid();
        int newRow = this.getRow() + 1;
        int col = this.getColumn();
        if (newRow < grid.length && grid[this.row][this.column]
                instanceof MagicWall && (grid[newRow][col] instanceof Path
                        || grid[newRow][col] instanceof Player
                        || grid[newRow][col] instanceof Frog
                        || grid[newRow][col] instanceof Fly)) {
            // transformed rocks are on the same coordinates as the magic wall
            // this if  statement makes sure magic walls are not replaced
            // with paths after the transformed rock falls.
            gridManager.removeFromList(grid[newRow][col]);
            gridManager.removeElement(newRow, col);
            gridManager.setElement(newRow, col, this);
            // Update position
            this.setRow(newRow);
            gainMomentum();
        } else if (newRow < grid.length && grid[newRow][col] instanceof Path) {
            // Update the grid to move the boulder
            gridManager.removeFromList(grid[newRow][col]);
            Element p = new Path(this.getRow(), this.getColumn());
            gridManager.setElement(this.getRow(), this.getColumn(), p);
            gridManager.addToList(p);

            gridManager.setElement(newRow, col, this);

            // Update position
            this.setRow(newRow);
            gainMomentum();

        } else if (newRow < grid.length && (grid[newRow][col] instanceof Player
                || grid[newRow][col] instanceof Frog
                || grid[newRow][col] instanceof Fly)) {
            // if the rock lands on a player/enemy and has momentum , remove the player/enemy
            // If the boulder lands on a player and has momentum
            if (hasMomentum) {
                gridManager.removeFromList(grid[newRow][col]); // Remove the player or enemy
                gridManager.destroyRemoveFromList(this); //destroy falling boulder/diamond
                gridManager.setElement(newRow, col, this);     // Replace player with the diamond

                Element p = new Path(this.getRow(), this.getColumn());
                gridManager.setElement(this.getRow(), this.getColumn(), p);
                gridManager.addToList(p);

                this.setRow(newRow);
            }
            hasMomentum = false;

        } else if (newRow < grid.length && grid[newRow][col] instanceof MagicWall
                && (grid[newRow + 1][col] instanceof Path
                || grid[newRow + 1][col] instanceof Player
                || grid[newRow + 1][col] instanceof Frog
                || grid[newRow + 1][col] instanceof Fly)) {
            //row under magic wall is within range , and is a path ,
            // anything else it stays over the  magic wall until its clear
            // beneath the magic wall (assuming it wouldn't roll)
            // turn into diamond and vice versa

            MagicWall magicWall = (MagicWall) gridManager.getElement(newRow, col);
            magicWall.transformRock(this, gridManager);

        } else {
            hasMomentum = false; // Reset momentum if the diamond stops
        }
    }

    /**
     * Handles the rolling logic for the Rocks.
     * @param gridManager the grid manager to access and update the grid
     */
    public void roll(GridManager gridManager) {
        Element[][] grid = gridManager.getElementGrid();
        int newRow = this.getRow() + 1; // Row below the current position
        int col = this.getColumn();

        // Check if below is a surface rock can roll on Boulder, Diamond, or NormalWall,
        // and check that the magic wall
        // isnt blocked by something a rock can not crush AKA player,enemy
        if (newRow < grid.length
                && (grid[newRow][col] instanceof Boulder
                        || grid[newRow][col] instanceof Diamond
                        || grid[newRow][col] instanceof NormalWall
                        || grid[newRow][col] instanceof TitaniumWall
                        || (grid[newRow][col] instanceof MagicWall)
                        && !(grid[newRow + 1][col] instanceof Player
                        || grid[newRow + 1][col] instanceof Frog
                        || grid[newRow + 1][col] instanceof Fly))) {
            // Check if rolling to the right is possible by checking if directly right and diagonally right is path.
            if (col + 1 < grid[0].length
                    && grid[newRow][col + 1] instanceof Path
                    && grid[this.getRow()][col + 1] instanceof Path) {

                // Move to the diagonal right
                Element p = new Path(this.getRow(), this.getColumn());
                gridManager.setElement(this.getRow(), this.getColumn(), p);
                gridManager.addToList(p);

                gridManager.setElement(this.getRow(), col + 1, this);
                this.setRow(this.getRow());
                this.setColumn(col + 1);

                this.gainMomentum();
            }
            // Check if rolling to the left is possible by checking
            // if directly left and diagonally left is path.
            if (col - 1 < grid[0].length && grid[newRow][col - 1] instanceof Path
                    && grid[this.getRow()][col - 1] instanceof Path) {

                // Move to the diagonal right
                Element p = new Path(this.getRow(), this.getColumn());
                gridManager.setElement(this.getRow(), this.getColumn(), p);
                gridManager.addToList(p);

                gridManager.setElement(this.getRow(), col - 1, this);
                this.setRow(this.getRow());
                this.setColumn(col - 1);

                this.gainMomentum();
            }
        }
    }
}
