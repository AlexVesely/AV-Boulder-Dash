import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;


/**
 * This class handles reading and writing information from level files.
 * @author Alex Vesely
 */
public class FileHandler {
    private static final int DIAMOND_COUNT_INDEX = 0;
    private static final int DIAMONDS_REQUIRED_INDEX = 1;
    private static final int AMOEBA_GROWTH_RATE_INDEX = 0;
    private static final int AMOEBA_SIZE_LIMIT_INDEX = 1;

    /**
     * Reads a grid of elements from a level file.
     * @param fileName the name of the level file containing the grid data.
     * @return a 2D array of strings representing the grid.
     */
    public static String[][] readElementGridFromLevelFile(String fileName) {
        File readFile = new File(fileName);
        try {
            Scanner in = new Scanner(readFile);

            String[] splitGridDimensions = in.nextLine().split(" ");
            int width = Integer.parseInt(splitGridDimensions[0]);
            int height = Integer.parseInt(splitGridDimensions[1]);

            in.nextLine(); //Skip line containing seconds left
            in.nextLine(); //Skip line containing diamond information
            in.nextLine(); //Skip line containing amoeba information
            in.nextLine(); //skip line containing key inventory information

            String[][] initialGrid = new String[height][width];
            int i = 0;
            while (in.hasNextLine()) {
                String[] elements = in.nextLine().split(" ");
                for (int j = 0; j < elements.length; j++) {
                    initialGrid[i][j] = elements[j];
                }
                i++;
            }
            in.close();
            return initialGrid;
        } catch (FileNotFoundException exception) {
            System.out.println("Error in finding file");
        }
        return null;
    }

    /**
     * Saves the game state to a save file associated to current player playing.
     * @param gameController the game controller.
     * @param currentProfile the profile of the current player.
     * @param secondsRemaining the remaining seconds of the level.
     * @param keyInventory the keys currently held by the player.
     */
    public static void writeFile(GameController gameController, PlayerProfile currentProfile, int secondsRemaining,
                                 ArrayList<KeyColour> keyInventory) {

        Element[][] currentGrid = gameController.getGridManager().getElementGrid();
        int diamondCount = gameController.getGridManager().getPlayer().getDiamondCount();
        int currentLevel = currentProfile.getMaxLevelReached();

        int id = currentProfile.getPlayerId();
        int amoebaGrowthRate = readAmoebaGrowthRateFromLevelFile("txt/Level" + currentLevel + ".txt");
        int amoebaSizeLimit = readAmoebaSizeLimitFromLevelFile("txt/Level" + currentLevel + ".txt");

        String fileName = "Save" + id + ".txt";

        try {
            String outputFile = "txt/" + fileName;
            PrintWriter out = new PrintWriter(outputFile);
            out.println(currentGrid[0].length + " " + currentGrid.length);
            out.println(secondsRemaining);

            out.println(diamondCount + " " + gameController.getDiamondsRequired());
            out.println(amoebaGrowthRate + " " + amoebaSizeLimit);

            out.println(createKeyInventoryString(keyInventory));

            for (int i = 0; i < currentGrid.length; i++) {
                for (int j = 0; j < currentGrid[i].length; j++) {
                    switch (gameController.getGridManager().getElement(i, j).getName()) {
                        case "Player" -> out.print("*");

                        case "Path" -> out.print("P");
                        case "Dirt" -> out.print("DT");
                        case "Exit" -> out.print("E");

                        case "NormalWall" -> out.print("NW");
                        case "TitaniumWall" -> out.print("TW");
                        case "MagicWall" -> out.print("MW");

                        case "Boulder" -> out.print("B");
                        case "Diamond" -> out.print("DD");

                        case "Frog" -> out.print("F");
                        case "Amoeba" -> out.print("A");

                        case "REDKey" -> out.print("RK");
                        case "REDLockedDoor" -> out.print("RLD");
                        case "GREENKey" -> out.print("GK");
                        case "GREENLockedDoor" -> out.print("GLD");
                        case "YELLOWKey" -> out.print("YK");
                        case "YELLOWLockedDoor" -> out.print("YLD");
                        case "BLUEKey" -> out.print("BK");
                        case "BLUELockedDoor" -> out.print("BLD");
                        case "RAINBOWKey" -> out.print("RBK");

                        // If there is an explosion, load a path in its place when the save is loaded
                        case "Explosion" -> out.print("P");

                        case "FireflyLeft" -> out.print("FFL");
                        case "FireflyRight" -> out.print("FFR");
                        case "ButterflyLeft" -> out.print("BFL");
                        case "ButterflyRight" -> out.print("BFR");
                        default -> System.out.println("Error in reading Symbol");
                    }
                    if (!(j == currentGrid[i].length - 1)) {
                        out.print(" ");
                    }
                }
                if (i != currentGrid.length - 1) {
                    out.println();
                }
            }
            out.close();
        } catch (IOException e) {
            System.out.println("Cannot write file");
        }
    }

    /**
     * Reads the remaining time in seconds from a level file.
     * @param fileName the name of the file containing the level data.
     * @return the remaining time in seconds.
     * @throws RuntimeException if the file is not found.
     */
    public static int readSecondsFromLevelFile(String fileName) {
        File readFile = new File(fileName);
        try {
            Scanner in = new Scanner(readFile);
            in.nextLine(); // Skip first line about grid dimensions
            return Integer.parseInt(in.nextLine()); // Return seconds
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found: " + fileName, e);
        }
    }

    /**
     * Reads the number of diamonds collected so far from a level file.
     * If its from the base level file it is always 0
     * If its from a level save then it's the number of diamonds collected so far
     * @param fileName the level file.
     * @return the number of diamonds collected so far.
     */
    public static int readDiamondsCollectedFromLevelFile(String fileName) {
        return readDiamondsInformationFromLevelFile(fileName, DIAMOND_COUNT_INDEX);
    }

    /**
     * Reads the number of diamonds required to complete a specific level from the level file.
     * @param fileName the level file.
     * @return the number of diamonds required to complete the level.
     */
    public static int readRequiredDiamondsFromLevelFile(String fileName) {
        return readDiamondsInformationFromLevelFile(fileName, DIAMONDS_REQUIRED_INDEX);
    }

    /**
     * Reads information about diamonds from a level file.
     * Can read the diamonds collected or a level's required diamonds from here.
     * @param fileName the level file.
     * @param index the index specifying the type of diamond information (0 for collected, 1 for required).
     * @return the requested diamond information.
     */
    private static int readDiamondsInformationFromLevelFile(String fileName, int index) {
        File readFile = new File(fileName);
        try {
            Scanner in = new Scanner(readFile);
            in.nextLine(); // Skip first line
            in.nextLine(); // Skip second line about seconds left
            String[] splitDiamondInformation = in.nextLine().split(" ");
            return Integer.parseInt(splitDiamondInformation[index]);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found: " + fileName, e);
        }
    }

    /**
     * Reads the amoeba growth rate from a level file.
     * @param fileName the level file.
     * @return the level's amoeba growth rate.
     */
    public static int readAmoebaGrowthRateFromLevelFile(String fileName) {
        return readAmoebaInformationFromLevelFile(fileName, AMOEBA_GROWTH_RATE_INDEX);
    }

    /**
     * Reads the amoeba size limit from the level file.
     * @param fileName the level file.
     * @return the level's amoeba size limit.
     */
    public static int readAmoebaSizeLimitFromLevelFile(String fileName) {
        return readAmoebaInformationFromLevelFile(fileName, AMOEBA_SIZE_LIMIT_INDEX);
    }

    /**
     * Reads amoeba-related information from the level file.
     * @param fileName the level file.
     * @param index    the index specifying the type of amoeba information (0 for growthRate, 1 for sizeLimit).
     * @return the requested amoeba information as an integer.
     */
    private static int readAmoebaInformationFromLevelFile(String fileName, int index) {
        File readFile = new File(fileName);
        try (Scanner in = new Scanner(readFile)) {
            in.nextLine(); // Skip first line about grid dimensions
            in.nextLine(); // Skip second line about seconds left
            in.nextLine(); // Skip third line about diamonds information
            String[] splitAmoebaInfo = in.nextLine().split(" ");
            return Integer.parseInt(splitAmoebaInfo[index]);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found: " + fileName, e);
        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            throw new RuntimeException("Error parsing amoeba information from file: " + fileName, e);
        }
    }

    /**
     * Reads the key inventory from the level file.
     * @param fileName the level file.
     * @return the key inventory.
     */
    public static ArrayList<KeyColour> readKeyInventoryFromLevelFile(String fileName) {
        File readFile = new File(fileName);
        ArrayList<KeyColour> keyInventory = new ArrayList<>();
        try {
            Scanner in = new Scanner(readFile);
            in.nextLine(); // Skip first line
            in.nextLine(); // Skip second line about seconds left
            in.nextLine(); // Skip third line about diamond information
            in.nextLine(); // Skip fourth line about amoeba information
            String keyInfo = in.nextLine();
            if (!keyInfo.isBlank()) {
                String[] splitKeyInformation = keyInfo.split(" ");
                for (String key : splitKeyInformation) {
                    switch (key) {
                        case "RK":
                            keyInventory.add(KeyColour.RED);
                            break;
                        case "BK":
                            keyInventory.add(KeyColour.BLUE);
                            break;
                        case "YK":
                            keyInventory.add(KeyColour.YELLOW);
                            break;
                        case "GK":
                            keyInventory.add(KeyColour.GREEN);
                            break;
                        default:
                            System.out.println("Error: can't read key " + key);
                            break;
                    }
                }
            }
            return keyInventory;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found: " + fileName, e);
        }
    }

    /**
     * Creates a string representation of the key inventory.
     * @param keyInventory the key inventory.
     * @return a space-separated string of key codes.
     */
    public static String createKeyInventoryString(ArrayList<KeyColour> keyInventory) {
        StringBuilder keyInventoryString = new StringBuilder();
        for (int i = 0; i < keyInventory.size(); i++) {
            KeyColour key = keyInventory.get(i);
            switch (key) {
                case RED:
                    keyInventoryString.append("RK");
                    break;
                case BLUE:
                    keyInventoryString.append("BK");
                    break;
                case YELLOW:
                    keyInventoryString.append("YK");
                    break;
                case GREEN:
                    keyInventoryString.append("GK");
                    break;
                default:
                    System.out.println("Error in reading symbol: " + key);
            }
            if (i < keyInventory.size() - 1) {
                keyInventoryString.append(" ");
            }
        }
        return keyInventoryString.toString();
    }

    /**
     * Deletes a player's saveFile.
     * @param currentProfile the player whose saveFile is to be deleted.
     */
    public static void deleteSaveFile(PlayerProfile currentProfile) {
        int idToDelete = currentProfile.getPlayerId();
        String filePath = "txt/Save" + idToDelete + ".txt";
        Path path = Paths.get(filePath);

        if (!Files.exists(path)) {
            return;
        }

        try {
            Files.delete(path);
        } catch (IOException e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }

}
