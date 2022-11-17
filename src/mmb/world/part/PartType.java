/**
 * 
 */
package mmb.world.part;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;

import mmb.world.chance.Chance;
import mmb.world.crafting.RecipeOutput;
import mmb.world.item.ItemEntry;
import mmb.world.item.ItemType;

/**
 *
 * @author oskar
 *
 */
public interface PartType extends ItemType, ItemEntry {
	@Override String title();
	@Override String description();
	
	/**
	 * Creates a block entry for this type.
	 * @return newly created block
	 */
	@Nonnull public PartEntry createPart();
	/**
	 * Loads a part entry using JSON payload
	 * @param node data to load from
	 * @return a new part entry with data
	 */
	@Nonnull public default PartEntry loadPart(@Nullable JsonNode node) {
		PartEntry item = createPart();
		item.load(node);
		return item;
	}
	/**
	 * Loads a part entry using JSON payload, restricting the output type
	 * @param <T> expected type
	 * @param node data to load from
	 * @param cls expected type
	 * @return a new part entry with data, or null if failed
	 */
	@Nullable public default <T extends PartEntry> PartEntry loadPartExpectType(@Nullable JsonNode node, @Nullable Class<T> cls) {
		PartEntry item = createPart();
		if(cls != null && !cls.isInstance(item)) return null;
		item.load(node);
		return item;
	}
	
	/** @return items dropped when module is removed */
	@Nonnull public Chance dropItems();
	/** @return items returned to the player */
	@Nonnull public RecipeOutput returnToPlayer();
	
	
}
