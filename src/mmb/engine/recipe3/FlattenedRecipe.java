package mmb.engine.recipe3;

import java.util.Arrays;
import java.util.Comparator;

/** Flattened recipe */
record FlattenedRecipe(
        FlattenedSlot[] slots, //recipe ingredient slots
        int[] prohibitedItems, //item group ordinals that are prohibited
        boolean shapeless
) {
	public static FlattenedRecipe flatten(RecipeSpec spec) {
		int nslots = spec.inputs().size();
		FlattenedSlot[] flattenedSlots = new FlattenedSlot[nslots];
		for(int i = 0; i < nslots; i++) 
			flattenedSlots[i] = new FlattenedSlot(i, spec.inputs().get(i));
		Arrays.sort(flattenedSlots, Comparator.comparingInt(x -> x.groupOrdinal()));
		int nprohibited = spec.prohibited().size();
		var flattenedProhibited = new int[nprohibited];
		int i = 0;
		for(var prohibited: spec.prohibited()) 
			flattenedProhibited[i++] = prohibited.ordinal;
		Arrays.sort(flattenedProhibited);
		
		return new FlattenedRecipe(flattenedSlots, flattenedProhibited, spec.shapeless());
	}
}
