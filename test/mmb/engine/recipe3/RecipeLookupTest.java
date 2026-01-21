package mmb.engine.recipe3;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class RecipeLookupTest {
	public static final Group iron = Group.of("iron");
	public static final Group copper = Group.of("copper");
	public static final Group tin = Group.of("tin");
	public static final VoltageTier ulv;
	static {
		ulv = new VoltageTier(1, "ulv", "Ultra Low Voltage");
		VoltageTier.addVoltageTier(ulv);
	}
	
	public static int NUMBER_GROUPS = 0;
	public static FlattenedRecipeGroup newRecipeGroup() {
		String groupName = Integer.toString(NUMBER_GROUPS++);
		return new FlattenedRecipeGroup(true, groupName, groupName);
	}

    @Test
    void testSingleSlotAny() {
        RecipeInput input = new RecipeInput(iron, 1, 1, 1);
        RecipeSpec spec = new RecipeSpec(
                List.of(input),
                Set.of(),
                RecipeOutput.EMPTY,
                ctx -> RecipeOutput.EMPTY,
                false,
                ulv,
                ulv,
                10, 5
        );
        FlattenedRecipeGroup group = newRecipeGroup();
        Recipe recipe = group.addRecipe(spec);

        // Machine slot has the same item
        Set<Group> machineSlot = Set.of(iron, Group.ANY);
        List<Set<Group>> machineContents = List.of(machineSlot);

        Set<Recipe> results = RecipeLookup.lookup(group, machineContents);

        assertTrue(results.contains(recipe), "Recipe should match machine slot with iron");
    }

    @Test
    void testProhibitedItems() {
        RecipeInput input = new RecipeInput(copper, 1, 1, 1);
        RecipeSpec spec = new RecipeSpec(
                List.of(input),
                Set.of(tin), // prohibited tin
                RecipeOutput.EMPTY,
                ctx -> RecipeOutput.EMPTY,
                false,
                ulv,
                ulv,
                10, 5
        );

        FlattenedRecipeGroup group = newRecipeGroup();
        Recipe recipe = group.addRecipe(spec);

        // Machine contains prohibited item tin
        Set<Group> machineSlot = Set.of(copper, tin);
        List<Set<Group>> machineContents = List.of(machineSlot);

        Set<Recipe> results = RecipeLookup.lookup(group, machineContents);

        assertFalse(results.contains(recipe), "Recipe should be filtered out due to prohibited item");
    }

    @Test
    void testShapedRecipeMismatch() {
        RecipeInput slot1 = new RecipeInput(iron, 1, 1, 1);
        RecipeInput slot2 = new RecipeInput(copper, 1, 1, 1);

        RecipeSpec spec = new RecipeSpec(
                List.of(slot1, slot2),
                Set.of(),
                RecipeOutput.EMPTY,
                ctx -> RecipeOutput.EMPTY,
                false, // shaped recipe
                ulv,
                ulv,
                10, 5
        );

        FlattenedRecipeGroup group = newRecipeGroup();
        Recipe recipe = group.addRecipe(spec);

        // Swapped slots should not match shaped recipe
        List<Set<Group>> machineContents = List.of(Set.of(copper), Set.of(iron));

        Set<Recipe> results = RecipeLookup.lookup(group, machineContents);

        assertFalse(results.contains(recipe), "Shaped recipe should not match swapped slots");
    }

    @Test
    void testShapelessRecipeMatch() {
        RecipeInput slot1 = new RecipeInput(iron, 1, 1, 1);
        RecipeInput slot2 = new RecipeInput(copper, 1, 1, 1);

        RecipeSpec spec = new RecipeSpec(
                List.of(slot1, slot2),
                Set.of(),
                RecipeOutput.EMPTY,
                ctx -> RecipeOutput.EMPTY,
                true, // shapeless
                ulv,
                ulv,
                10, 5
        );

        FlattenedRecipeGroup group = newRecipeGroup();
        Recipe recipe = group.addRecipe(spec);

        // Swapped slots should match shapeless recipe
        List<Set<Group>> machineContents = List.of(Set.of(copper), Set.of(iron));

        Set<Recipe> results = RecipeLookup.lookup(group, machineContents);

        assertTrue(results.contains(recipe), "Shapeless recipe should match slots in any order");
    }

    @Test
    void testAnyAndNoneSlots() {
        RecipeInput anySlot = RecipeInput.ANY;
        RecipeInput noneSlot = RecipeInput.NONE;

        RecipeSpec spec = new RecipeSpec(
                List.of(anySlot, noneSlot),
                Set.of(),
                RecipeOutput.EMPTY,
                ctx -> RecipeOutput.EMPTY,
                false,
                ulv,
                ulv,
                0, 0
        );

        FlattenedRecipeGroup group = newRecipeGroup();
        Recipe recipe = group.addRecipe(spec);

        List<Set<Group>> machineContents = List.of(
                Set.of(iron, Group.ANY), // any slot filled
                Set.of(Group.NONE)                   // empty slot
        );

        Set<Recipe> results = RecipeLookup.lookup(group, machineContents);

        assertTrue(results.contains(recipe), "ANY and NONE slots should match correctly");
    }
}
