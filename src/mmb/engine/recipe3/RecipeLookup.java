package mmb.engine.recipe3;

import java.util.*;

/**
 * High-performance flattened recipe lookup.
 */
public final class RecipeLookup {

    /**
     * Finds all matching recipes for the machine contents.
     * 
     * @param flattenedGroup the flattened recipe group to search
     * @param machineSlotGroups list of sets of group ordinals for each machine slot
     *                          Empty slots should be a set containing only Group.NONE.ordinal
     * @return set of plausible recipes that match the contents
     */
    public static Set<Recipe> lookup(
            FlattenedRecipeGroup flattenedGroup,
            List<Set<Group>> machineSlotGroups) {

        Set<Recipe> plausibleRecipes = new HashSet<>();

        // Step 1: Collect plausible recipes by checking the group index
        for (int slotIndex = 0; slotIndex < machineSlotGroups.size(); slotIndex++) {
            Set<Group> itemGroups = machineSlotGroups.get(slotIndex);

            Set<Recipe> recipesForSlot = new HashSet<>();
            for (Group group : itemGroups) {
            	int groupOrdinal = group.ordinal;
                recipesForSlot.addAll(flattenedGroup.findPlausible(groupOrdinal));
            }

            if (slotIndex == 0) {
                plausibleRecipes.addAll(recipesForSlot);
            } else {
                plausibleRecipes.retainAll(recipesForSlot);
                if (plausibleRecipes.isEmpty()) return Collections.emptySet(); // early exit
            }
        }

        // Step 2: Filter out recipes with prohibited items
        plausibleRecipes.removeIf(recipe -> {
        	FlattenedRecipe flattened = recipe.flattened;
            int[] prohibited = flattened.prohibitedItems();
            for (Set<Group> itemGroups : machineSlotGroups) {
                for (Group group : itemGroups) {
                	int groupOrdinal = group.ordinal;
                    if (Arrays.binarySearch(prohibited, groupOrdinal) >= 0) return true;
                }
            }
            return false;
        });

        // Step 3: Check shaped recipes
        plausibleRecipes.removeIf(recipe -> {
        	FlattenedRecipe flattened = recipe.flattened;
            FlattenedSlot[] slots = flattened.slots();
            if (slots.length != machineSlotGroups.size()) return true;

            if(flattened.shapeless()) return false;
                        
            for (FlattenedSlot slot : slots) {
                Set<Group> itemGroups = machineSlotGroups.get(slot.position());
                Group groupForSlot = Group.groups.get(slot.groupOrdinal());
                if (!itemGroups.contains(groupForSlot) && !slot.isAny() && !slot.isNone()) {
                    return true; // slot mismatch
                }
            }
            
            return false;
        });

        return plausibleRecipes;
    }

}