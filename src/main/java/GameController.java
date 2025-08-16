import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import java.util.ArrayList;

/**
 * GameController manages the element's movements, interactions,
 * and rendering of the game.
 * @author Omar Sanad
 */
public class GameController {
    private final Canvas canvas;
    private final GridManager gridManager;
    private final Renderer renderer;
    private final InputHandler inputHandler;

    private static int nextExplosionRow;
    private static int nextExplosionCol;
    public static boolean waitingForExplosionAfterMath = false;
    public static boolean waitingForExplosion = false;
    private static boolean transformToDiamonds;

    private int diamondsRequired;

    private static boolean gameStatus = true;

    /**
     * Represents possible inputs for the player.
     */
    public enum PlayerInput {
        LEFT,
        RIGHT,
        DOWN,
        UP
    }

    /**
     * Constructs a GameController with the given grid template and canvas.
     * Initializes the grid, player, renderer, and input handler.
     * @param gridTemplate the 2D array representing the initial grid layout
     * @param canvas       the Canvas object used for rendering the game
     */
    public GameController(String[][] gridTemplate, Canvas canvas) {
        this.canvas = canvas;
        this.gridManager = new GridManager(gridTemplate);
        this.renderer = new Renderer();
        this.inputHandler = new InputHandler();
    }

    /**
     * Replaces the player at the specified grid position with a Path element,
     * removes the player from the game, and optionally ends the game.
     * @param targetRow the row position of the amoeba to be replaced
     * @param targetColumn the column position of the amoeba to be replaced
     */
    private void replaceEnemyWithPath(int targetRow, int targetColumn) {
        // Remove the target from the game
        gridManager.destroyRemoveFromList(gridManager.getElement(targetRow, targetColumn));

        // Replace the target with a Path in the grid
        gridManager.setElement(targetRow, targetColumn, new Path(targetRow, targetColumn));

    }

    /**
     * Replaces the player at the specified grid position with a Path element,
     * removes the player from the game, and optionally ends the game.
     * @param playerRow the row position of the player to be replaced
     * @param playerCol the column position of the player to be replaced
     */
    private void replacePlayerWithPath(int playerRow, int playerCol) {
        // Replace the player with a Path in the grid
        gridManager.setElement(playerRow, playerCol, new Path(playerRow, playerCol));

        // Remove the player from the game
        gridManager.destroyRemoveFromList(gridManager.getPlayer());

        //Stop input handling and end the game
        gameOver();
    }


    /**
     * Checks the neighboring elements surrounding the specified enemy in the grid
     * to determine if a neighboring element is a player. If a player is found in
     * any of the neighboring positions (up, down, left, or right), it replaces the
     * player with a path element.
     * @param enemy the enemy element whose neighbors are to be checked
     * @param grid a 2D array of elements representing the game grid
     */
    private void checkNeighboursForPlayer(Element enemy, Element[][] grid) {
        // Check bounds and get neighbors safely
        int enemyRow = enemy.getRow();
        int enemyCol = enemy.getColumn();
        Element currentRightNeighbor = (enemyCol + 1 < grid[0].length) ? grid[enemyRow][enemyCol + 1] : null;
        Element currentLeftNeighbor = (enemyCol - 1 >= 0) ? grid[enemyRow][enemyCol - 1] : null;
        Element currentDownNeighbor = (enemyRow + 1 < grid.length) ? grid[enemyRow + 1][enemyCol] : null;
        Element currentUpNeighbor = (enemyRow - 1 >= 0) ? grid[enemyRow - 1][enemyCol] : null;

        if (currentUpNeighbor instanceof Player) {
            replacePlayerWithPath(enemyRow - 1, enemyCol); // Replace player at the UP position
        } else if (currentDownNeighbor instanceof Player) {
            replacePlayerWithPath(enemyRow + 1, enemyCol); // Replace player at the DOWN position
        } else if (currentRightNeighbor instanceof Player) {
            replacePlayerWithPath(enemyRow, enemyCol + 1); // Replace player at the RIGHT position
        } else if (currentLeftNeighbor instanceof Player) {
            replacePlayerWithPath(enemyRow, enemyCol - 1); // Replace player at the LEFT position
        }
    }

    /**
     * Goes through all enemies on the grid, and checks their neighbours for amoeba.
     * @param enemy an enemy on the grid.
     * @param grid the grid of elements.
     */
    private void checkNeighboursForAmoeba(Element enemy, Element[][] grid) {
        // Check bounds and get neighbors safely
        int enemyRow = enemy.getRow();
        int enemyCol = enemy.getColumn();
        Element currentRightNeighbor = (enemyCol + 1 < grid[0].length) ? grid[enemyRow][enemyCol + 1] : null;
        Element currentLeftNeighbor = (enemyCol - 1 >= 0) ? grid[enemyRow][enemyCol - 1] : null;
        Element currentDownNeighbor = (enemyRow + 1 < grid.length) ? grid[enemyRow + 1][enemyCol] : null;
        Element currentUpNeighbor = (enemyRow - 1 >= 0) ? grid[enemyRow - 1][enemyCol] : null;

        if (currentUpNeighbor instanceof Amoeba) {
            replaceEnemyWithPath(enemyRow, enemyCol); // Replace Enemy if UP position is amoeba
        } else if (currentDownNeighbor instanceof Amoeba) {
            replaceEnemyWithPath(enemyRow, enemyCol); // Replace Enemy if DOWN position is amoeba
        } else if (currentRightNeighbor instanceof Amoeba) {
            replaceEnemyWithPath(enemyRow, enemyCol); // Replace Enemy if RIGHT position is amoeba
        } else if (currentLeftNeighbor instanceof Amoeba) {
            replaceEnemyWithPath(enemyRow, enemyCol); // Replace Enemy if LEFT position is amoeba
        }
    }


    /**
     * Goes through all enemies on the grid, and checks their neighbours to kill the player or die from amoeba.
     */
    public void killTick() {
        ArrayList<Frog> frogs = gridManager.getFrogs();
        ArrayList<Fly> flies = gridManager.getFlies();
        ArrayList<Element> enemies = new ArrayList<>();
        enemies.addAll(frogs);
        enemies.addAll(flies);

        // Iterate through all enemies to check for neighboring player
        for (Element enemy : enemies) {
            checkNeighboursForPlayer(enemy, gridManager.getElementGrid());
        }

        // Iterate through all enemies to check for neighboring amoeba
        for (Element enemy : enemies) {
            checkNeighboursForAmoeba(enemy, gridManager.getElementGrid());
        }
    }

    /**
     * Executes the boulder tick, triggering all boulders to perform their movement logic.
     * Updates the grid and redraws the game.
     */
    public void dangerousRockRollTick() {
        // Making a copy of the boulders Arraylist,
        // avoids problems with concurrently changing
        // the arraylist when a diamond passes through magic wall
        ArrayList<Boulder> boulders = new ArrayList<>(gridManager.getBoulders());
        ArrayList<Diamond> diamonds = new ArrayList<>(gridManager.getDiamonds());

        for (Boulder boulder : boulders) {
            boulder.roll(gridManager);
        }

        for (Diamond diamond : diamonds) {
            diamond.roll(gridManager);
        }

        draw();
    }

    /**
     * Executes the boulder tick, triggering all boulders to perform their movement logic.
     * Updates the grid and redraws the game.
     */
    public void dangerousRockFallTick() {
        ArrayList<Diamond> diamonds = new ArrayList<>(gridManager.getDiamonds());
        ArrayList<Boulder> boulders = new ArrayList<>(gridManager.getBoulders());

        for (Diamond diamond : diamonds) {
            diamond.fall(gridManager);
        }

        for (Boulder boulder : boulders) {
            boulder.fall(gridManager);
        }
            draw();
    }

    /**
     * Executes the frog's tick method and redraws the game.
     */
    public void frogTick() {
        // Making a copy of the boulders Arraylist,
        // avoids problems with concurrency
        ArrayList<Frog> frogs = new ArrayList<>(gridManager.getFrogs());
        for (Frog frog : frogs) {
            frog.seekAndKill(gridManager, gridManager.getPlayer());
        }
        draw();
    }

    /**
     * Executes the amoeba tick, which checks and updates all active amoeba groups on the game grid.
     * If amoeba groups are present, it updates their state using the GridManager.
     * After updating the amoeba groups, the game grid is redrawn.
     */
    public void amoebaTick() {
        if (!AmoebaManager.isEmpty()) { // Check if there are any active amoeba groups
            AmoebaManager.updateAll(gridManager); // Update all amoeba groups
        }
        draw(); // Redraw the grid after updating
    }

    /**
     * Executes the fly tick, which processes all butterfly movements on the grid.
     * This method retrieves the current list of butterflies from the grid manager,
     * iterates over each butterfly, and invokes its movement logic.
     * After all butterflies have moved, the game state is redrawn to reflect any changes.
     */
    public void flyTick() {
        ArrayList<Fly> flies = new ArrayList<>(gridManager.getFlies());
        for (Fly fly : flies) {
            fly.move(gridManager, gridManager.getPlayer());
        }
        draw();
    }

    /**
     * Executes the player tick, handling input and updating the player's position on the grid.
     * Processes player movement and redraws the game.
     */
    public void playerTick() {
        if (gameStatus) {
            if (inputHandler.isInputPending()) {
                PlayerInput input = inputHandler.consumeInput();
                if (input != null) {
                    switch (input) {
                        case UP -> gridManager.getPlayer().movePlayer(
                                gridManager.getPlayer().getRow() - 1,
                                gridManager.getPlayer().getColumn(),
                                gridManager
                        );
                        case DOWN -> gridManager.getPlayer().movePlayer(
                                gridManager.getPlayer().getRow() + 1,
                                gridManager.getPlayer().getColumn(),
                                gridManager
                        );
                        case LEFT -> gridManager.getPlayer().movePlayer(
                                gridManager.getPlayer().getRow(),
                                gridManager.getPlayer().getColumn() - 1,
                                gridManager
                        );
                        case RIGHT -> gridManager.getPlayer().movePlayer(
                                gridManager.getPlayer().getRow(),
                                gridManager.getPlayer().getColumn() + 1,
                                gridManager
                        );
                    }
                    getPlayer().imageAnimation();
                }
            }
            draw();
        }
    }

    /**
     * Selects an index in the ElementGrid to create a 3x3 Explosion and then its aftermath at the specified spot.
     * @param row the row index on the grid where the explosion will occur.
     * @param column the column index on the grid where the explosion will occur.
     * @param dropsDiamonds true if the explosion should cause diamonds to drop, false otherwise.
     */
    public static void applyExplosion(int row, int column, boolean dropsDiamonds) {
        waitingForExplosion = true;
        nextExplosionRow = row;
        nextExplosionCol = column;
        transformToDiamonds = dropsDiamonds;

    }

    /**
     * Executes the explosion tick logic for handling explosions and their aftermath on the game grid.
     */
    public void explosionTick() {
        //Explosion Tick Method, if an applyExplosion has occurred then it is waiting for explosion, after an explosion
        // the next tick cycle and explosion aftermath should occur
        if (waitingForExplosion) {
            // Create the initial explosion
            Explosion.createExplosion(nextExplosionRow, nextExplosionCol, gridManager);
            draw();
            waitingForExplosionAfterMath = true;
            waitingForExplosion = false;
        } else if // Create the aftermath
        (waitingForExplosionAfterMath && transformToDiamonds) {
            // if dropsDiamonds
            Explosion.createDiamondExplosionAfterMath(nextExplosionRow, nextExplosionCol, gridManager);
            draw();
            waitingForExplosionAfterMath = false;
            transformToDiamonds = false;
        } else if // Create the aftermath
         (waitingForExplosionAfterMath) {
          // if dropsDiamonds
            Explosion.createExplosionAfterMath(nextExplosionRow, nextExplosionCol, gridManager);
            draw();
            waitingForExplosionAfterMath = false;
        }
    }

    /**
     * Checks if the player has won the level.
     * @return true if the player has won, false otherwise.
     */
    public boolean checkLevelWinTick() {
        return gridManager.getPlayer().hasPlayerWon();
    }

    /**
     * Ends the current game session by setting the game status to false
     * and displaying a "GAME OVER" message to the console.
     */
    public static void gameOver() {
        gameStatus = false;
    }

    public static void gameStart() {
        gameStatus = true;
    }

    /**
     * Draws the current state of the game using the Renderer.
     */
    public void draw() {
        renderer.draw(canvas.getGraphicsContext2D(), gridManager.getElementGrid());
    }

    /**
     * Registers a key press input and passes it to the InputHandler.
     * @param code the KeyCode representing the player's input.
     */
    public void registerInput(KeyCode code) {
        inputHandler.registerInput(code);
    }

    /**
     * Retrieves the Canvas object used for rendering the game.
     * @return the Canvas object.
     */
    public Canvas getCanvas() {
        return canvas;
    }

    /**
     * Retrieves the GridManager responsible for managing the game grid.
     * @return the GridManager object.
     */
    public GridManager getGridManager() {
        return gridManager;
    }

    /**
     * Retrieves the player currently managed by the GameController.
     * @return the Player object being managed.
     */
    public Player getPlayer() {
        return gridManager.getPlayer();
    }

    /**
     * Sets the number of diamonds required for completing the game level.
     * Updates both the game controller's configuration and notifies the player.
     * @param diamondsRequired the integer representing the number of diamonds
     *                         needed to finish the current level.
     */
    public void setDiamondsRequired(int diamondsRequired) {
        this.diamondsRequired = diamondsRequired;
        this.gridManager.getPlayer().setDiamondsRequired(diamondsRequired);

    }

    /**
     * Returns the diamonds required for the player to beat a level.
     * @return diamonds required for player to beat a level.
     */
    public int getDiamondsRequired() {
        return diamondsRequired;
    }

    /**
     * Sets the maximum allowable limit for amoebas in the game.
     * @param amoebaLimit the maximum number of amoebas allowed
     */
    public void setAmoebaLimit(int amoebaLimit) {
        for (int i = 0; i < gridManager.getAmoebaGroups().size(); i++) {
            gridManager.getAmoebaGroups().get(i).setAmoebaSizeLimit(amoebaLimit);
        }
    }

}
