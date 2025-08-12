import java.util.ArrayList;
import java.util.List;

/**
 * This class manages all the Amoeba on the game's grid.
 * @author Karamveer Singh
 */
public class AmoebaManager {
    private static final List<AmoebaGroup> amoebaGroups = new ArrayList<>();

    /**
     * Returns true if there are no amoeba groups being managed,
     * false otherwise.
     * @return true if there are no amoeba groups, false otherwise
     */
    public static boolean isEmpty() {
        return amoebaGroups.isEmpty();
    }

    /**
     * Adds an AmoebaGroup to the list of managed amoeba groups.
     * @param group the AmoebaGroup to add
     */
    public static void addAmoebaGroup(AmoebaGroup group) {
        amoebaGroups.add(group);
    }


    /**
     * Clears all amoeba groups from the list being managed.
     * This operation removes all references to the amoeba groups,
     * effectively resetting the manager to an empty state.
     */
    public static void clearGroups() {
        amoebaGroups.clear();
    }

    /**
     * Updates all amoeba groups in the provided GridManager by spreading them.
     * @param gridManager the GridManager containing the amoeba groups to update
     */
    public static void updateAll(GridManager gridManager) {
        for (AmoebaGroup group : gridManager.getAmoebaGroups()) {
            group.spread(gridManager);
        }
    }
}
