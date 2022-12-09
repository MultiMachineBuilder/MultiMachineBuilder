/**
 * 
 */
package mmbbase.menu.world.craft;

import java.util.Vector;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import mmb.engine.craft.RecipeOutput;
import mmb.engine.inv.ItemStack;

/**
 * @author oskar
 *
 */
public class VectorUtils {
	private VectorUtils() {}
	/**
	 * Converts an item list into an array
	 * @param output
	 * @return
	 */
	@Nonnull public static Vector<ItemStack> list2vector(RecipeOutput output){
		return output
				.getContents()
				.object2IntEntrySet()
				.stream()
				.map(ent -> new ItemStack(ent.getKey(), ent.getIntValue()))
				.collect(Collectors.toCollection(() -> new Vector<ItemStack>()));
	}

	@Nonnull public static ItemStack[] list2arr(RecipeOutput output){
		return output
				.getContents()
				.object2IntEntrySet()
				.stream()
				.map(entry -> new ItemStack(entry.getKey(), entry.getIntValue()))
				.toArray(n -> new ItemStack[n]);
	}

}
