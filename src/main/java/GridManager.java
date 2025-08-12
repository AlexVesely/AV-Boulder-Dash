import java.util.ArrayList;

/**
 * The GridManager is responsible for managing the grid of elements in the game.
 * It initializes the grid based on a template, provides access to individual elements,
 * manages lists of specific element types, and supports adding, removing, and updating elements.
 * @author Omar Sanad
 */
public class GridManager {
    private final Element[][] elementGrid;
    private final ArrayList<Path> paths = new ArrayList<>();
    final ArrayList<Dirt> dirts = new ArrayList<>();
    private final ArrayList<Player> players = new ArrayList<>();
    private final ArrayList<NormalWall> walls = new ArrayList<>();
    final ArrayList<Boulder> boulders = new ArrayList<>();
    final ArrayList<Diamond> diamonds = new ArrayList<>();
    final ArrayList<Fly> flies = new ArrayList<>();
    final ArrayList<Frog> frogs = new ArrayList<>();
    final ArrayList<Amoeba> amoebas = new ArrayList<>();
    private final ArrayList<AmoebaGroup> amoebaGroups = new ArrayList<>();
    private  Player player;


    /**
     * Constructs a GridManager from a grid template.
     * Initializes the grid of elements based on the provided template.
     * @param gridTemplate the 2D array representing the initial grid setup
     */
    public GridManager(String[][] gridTemplate) {
        this.elementGrid = new Element[gridTemplate.length][gridTemplate[0].length];
        initializeGrid(gridTemplate);

    }

    /**
     * Initializes the player's position based on the grid template.
     * @param gridTemplate the 2D array representing the grid layout
     */
    public void initializePlayer(String[][] gridTemplate) {
        Element[][] elementGrid = this.getElementGrid();
        for (int row = 0; row < gridTemplate.length; row++) {
            for (int col = 0; col < gridTemplate[row].length; col++) {
                if (elementGrid[row][col] instanceof Player) {
                    player.setRow(row);
                    player.setColumn(col);
                    break;
                }
            }
        }
    }

    /**
     * Initializes the grid and categorizes elements into appropriate lists.
     * @param gridTemplate the 2D array representing the initial grid setup
     */
    public void reinitializeGrid(String[][] gridTemplate) {
        GameController.waitingForExplosion = false;
        GameController.waitingForExplosionAfterMath = false;

        initializePlayer(gridTemplate);
        Exit.toggleFalseExitExists();
        getBoulders().clear();
        getDiamonds().clear();
        getFrogs().clear();
        getAmoebas().clear();
        getFlies().clear();
        GameController.gameStart();

        // Clear specific references
        player.resetDiamondCountStatus();
        player.resetKeyInventory();

        for (int row = 0; row < gridTemplate.length; row++) {
            for (int col = 0; col < gridTemplate[row].length; col++) {
                Element element = createElement(this, gridTemplate[row][col], row, col); // i removed false here
                elementGrid[row][col] = element;
                addToList(element);
            }
        }
        identifyAmoebaGroups();
    }

    /**
     * Initializes the grid and categorizes elements into appropriate lists.
     * Clears any existing lists before initializing.
     * @param gridTemplate the 2D array representing the initial grid setup
     */
    public void initializeGrid(String[][] gridTemplate) {
        // Clear all memory of existing lists
        Exit.toggleFalseExitExists();
        getBoulders().clear();
        getDiamonds().clear();
        getFrogs().clear();
        getAmoebas().clear();
        getFlies().clear();
        GameController.gameStart();
        // follows LeftEdge is true by default
        for (int row = 0; row < gridTemplate.length; row++) {
            for (int col = 0; col < gridTemplate[row].length; col++) {
                Element element = createElement(this,gridTemplate[row][col], row, col); //I removed false here
                elementGrid[row][col] = element;
                addToList(element);
            }
        }
        identifyAmoebaGroups();
    }
    /**
     * Creates an element based on the provided code and its position in the grid.
     * @param gridManager the gridManager
     * @param code        the String code representing the type of element
     * @param row         the row position of the element
     * @param col         the column position of the element
     * @return the created Element object
     * @throws IllegalArgumentException if the code does not correspond to a known element type
     */
    private Element createElement(GridManager gridManager, String code, int row, int col) {
        return switch (code) {
            case "*" -> player = new Player(row, col);

            case "P" -> new Path(row, col);
            case "DT" -> new Dirt(row, col);
            case "E" -> new Exit(row, col);

            case "NW" -> new NormalWall(row, col);
            case "TW" -> new TitaniumWall(row, col);
            case "MW" -> new MagicWall(row, col);

            case "B" -> new Boulder(row, col);
            case "DD" -> new Diamond(row, col);

            case "F" -> new Frog(row, col);
            case "A" -> new Amoeba(row, col);

            case "FFL" -> new Firefly(row,col,true);
            case "FFR" -> new Firefly(row,col,false);
            case "BFL" -> new Butterfly(row,col,true);
            case "BFR" -> new Butterfly(row,col,false);

            case "RLD" -> new LockedDoor(row, col, KeyColour.RED);
            case "RK" -> new Key(row, col, KeyColour.RED);
            case "GLD" -> new LockedDoor(row, col, KeyColour.GREEN);
            case "GK" -> new Key(row, col, KeyColour.GREEN);
            case "YLD" -> new LockedDoor(row, col, KeyColour.YELLOW);
            case "YK" -> new Key(row, col, KeyColour.YELLOW);
            case "BLD" -> new LockedDoor(row, col, KeyColour.BLUE);
            case "BK" -> new Key(row, col, KeyColour.BLUE);
            case "RBK" -> new Key(row, col, KeyColour.RAINBOW);

            default -> throw new IllegalArgumentException("Unknown element: " + code);
        };
    }

    /**
     * Adds an element to its corresponding list based on its type.
     * @param element the Element to be added
     */
    public void addToList(Element element) {
        if (element instanceof Path path) {
            paths.add(path);
        } else if (element instanceof Dirt dirt) {
            dirts.add(dirt);
        } else if (element instanceof Player player) {
            players.add(player);
        } else if (element instanceof NormalWall wall) {
            walls.add(wall);
        } else if (element instanceof Boulder boulder) {
            boulders.add(boulder);
        } else if (element instanceof Butterfly butterfly) {
            flies.add(butterfly);
        } else if (element instanceof Firefly firefly) {
            flies.add(firefly);
        } else if (element instanceof Frog frog) {
            frogs.add(frog);
        } else if (element instanceof Amoeba amoeba) {
            amoebas.add(amoeba);
        } else if (element instanceof Diamond diamond) {
            diamonds.add(diamond);
        }
    }

    /**
     * Removes an element from its corresponding list based on its type.
     * Explodes if it is an enemy
     * @param element the Element to be removed
     */
    public void removeFromList(Element element) {
        if (element instanceof Path path) {
            paths.remove(path);
        } else if (element instanceof Dirt dirt) {
            dirts.remove(dirt);
        } else if (element instanceof Player player) {
            players.remove(player);
            GameController.gameOver();
            GameController.applyExplosion(element.row, element.column, Player.dropDiamond);
        } else if (element instanceof NormalWall wall) {
            walls.remove(wall);
        } else if (element instanceof Boulder boulder) {
            boulders.remove(boulder);
        } else if (element instanceof Frog frog) {
            frogs.remove(frog);
            GameController.applyExplosion(element.row, element.column, Frog.dropDiamond);
        } else if (element instanceof Amoeba amoeba) {
            amoebas.remove(amoeba);
        } else if (element instanceof Diamond diamond) {
            diamonds.remove(diamond);
        } else if (element instanceof Butterfly butterfly) {
            flies.remove(butterfly);
            GameController.applyExplosion(element.row, element.column, Butterfly.dropDiamond);
        } else if (element instanceof Firefly firefly) {
            flies.remove(firefly);
            GameController.applyExplosion(element.row, element.column, Firefly.dropDiamond);
        }
    }

    /**
     * Removes an element from its corresponding list based on its type.
     * Objects here only get destroyed, thus avoiding an explosion and chain reaction.
     * @param element the Element to be removed
     */
    public void destroyRemoveFromList(Element element) {
        if (element instanceof Path path) {
            paths.remove(path);
        } else if (element instanceof Dirt dirt) {
            dirts.remove(dirt);
        } else if (element instanceof Player player) {
            players.remove(player);
            GameController.gameOver();
        } else if (element instanceof NormalWall wall) {
            walls.remove(wall);
        } else if (element instanceof Boulder boulder) {
            boulders.remove(boulder);
        } else if (element instanceof Frog frog) {
            frogs.remove(frog);
        } else if (element instanceof Amoeba amoeba) {
            amoebas.remove(amoeba);
        } else if (element instanceof Diamond diamond) {
            diamonds.remove(diamond);
        } else if (element instanceof Butterfly butterfly) {
            flies.remove(butterfly);
        } else if (element instanceof Firefly firefly) {
            flies.remove(firefly);
        }
    }

    /**
     * Retrieves the 2D array of elements in the grid.
     * @return the element grid
     */
    public Element[][] getElementGrid() {
        return elementGrid;
    }

    /**
     * Retrieves the element at the specified position in the grid.
     *
     * @param row the row position of the element
     * @param col the column position of the element
     * @return the Element at the specified position
     */
    public Element getElement(int row, int col) {
        return elementGrid[row][col];
    }

    /**
     * Sets an element at the specified position in the grid.
     * @param row     the row position of the element
     * @param col     the column position of the element
     * @param element the Element to set at the specified position
     */
    public void setElement(int row, int col, Element element) {
        elementGrid[row][col] = element;
    }

    /**
     * Removes an element from the grid at the specified position.
     * Replaces the removed element with a Path and removes it from its corresponding list.
     * @param row the row position of the element to remove
     * @param col the column position of the element to remove
     */
    public void removeElement(int row, int col) {
        removeFromList(elementGrid[row][col]);
        Path p = new Path(row, col);
        elementGrid[row][col] = p;
    }

    /**
     * Retrieves the list of Boulder elements in the grid.
     * @return the ArrayList of Boulder elements
     */
    public ArrayList<Boulder> getBoulders() {
        return boulders;
    }

    /**
     * Retrieves the list of Diamond elements in the grid.
     * @return the ArrayList of Boulder elements
     */
    public ArrayList<Diamond> getDiamonds() {
        return diamonds;
    }

    /**
     * Retrieves the list of Butterfly and Fireflies elements in the grid.
     * @return the ArrayList of Butterfly and Fireflies elements
     */
    public ArrayList<Fly> getFlies() {
        return flies;
    }

    /**
     * Retrieves the list of Frog elements in the grid.
     * @return the ArrayList of Frog elements
     */
    public ArrayList<Frog> getFrogs() {
        return frogs;
    }

    /**
     * Retrieves the current Player object in the grid.
     * @return the Player object representing the player character in the grid
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Retrieves the list of Amoeba elements in the grid.
     * @return the ArrayList of Amoeba elements
     */
    public ArrayList<Amoeba> getAmoebas() {
        return amoebas;
    }

    /**
     * Method to kill the player and stop the player from continuing to interact with the game.
     */
    public void killPlayer() {
        removeFromList(player);
        Path path = new Path(player.getRow(), player.getColumn());
        elementGrid[player.row][player.column] = path;
    }

    /**
     * Returns the list of AmoebaGroup objects, each representing a group of connected
     * Amoeba elements in the grid.
     * @return the list of AmoebaGroup objects
     */
    public ArrayList<AmoebaGroup> getAmoebaGroups() {
        return amoebaGroups;
    }
    /**
     * Identifies all the groups of connected amoebas in the grid and
     * stores them in the local list and the global manager.
     */
    private void identifyAmoebaGroups() {
        boolean[][] visited = new boolean[elementGrid.length][elementGrid[0].length];
        AmoebaManager.clearGroups(); // Clear previous groups

        for (int row = 0; row < elementGrid.length; row++) {
            for (int col = 0; col < elementGrid[row].length; col++) {
                if (elementGrid[row][col] instanceof Amoeba && !visited[row][col]) {
                    // Start a new group if an unvisited amoeba is found
                    AmoebaGroup group = new AmoebaGroup();
                    exploreAmoebaGroup(row, col, group, visited);
                    amoebaGroups.add(group); // Add to the local list
                    AmoebaManager.addAmoebaGroup(group); // Add to the global manager
                }
            }
        }
    }

    /**
     * Explores a group of connected amoebas by performing a depth-first search.
     * @param row the row of the cell to explore
     * @param col the column of the cell to explore
     * @param group the group of amoebas to add to
     * @param visited a 2D boolean array to mark visited cells
     */
    private void exploreAmoebaGroup(int row, int col, AmoebaGroup group, boolean[][] visited) {
        // Boundary check
        if (row < 0 || row >= elementGrid.length || col < 0 || col >= elementGrid[0].length) return;

        // Check if the cell is already visited or not an amoeba
        if (visited[row][col] || !(elementGrid[row][col] instanceof Amoeba amoeba)) return;

        // Mark the cell as visited and add the amoeba to the group
        visited[row][col] = true;
        group.addAmoeba(amoeba);

        // Explore all four directions
        exploreAmoebaGroup(row - 1, col, group, visited); // Up
        exploreAmoebaGroup(row + 1, col, group, visited); // Down
        exploreAmoebaGroup(row, col - 1, group, visited); // Left
        exploreAmoebaGroup(row, col + 1, group, visited); // Right
    }

}
