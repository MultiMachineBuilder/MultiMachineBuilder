/**
 * 
 */
package mmb.content.modular.part;

import com.fasterxml.jackson.databind.JsonNode;

import mmb.NN;
import mmb.Nil;
import mmb.engine.chance.Chance;
import mmb.engine.item.ItemEntry;
import mmb.engine.item.ItemType;
import mmb.engine.recipe.RecipeOutput;

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
	@NN public PartEntry createPart();
	/**
	 * Loads a part entry using JSON payload
	 * @param node data to load from
	 * @return a new part entry with data
	 */
	@NN public default PartEntry loadPart(@Nil JsonNode node) {
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
	@Nil public default <T extends PartEntry> PartEntry loadPartExpectType(@Nil JsonNode node, @Nil Class<T> cls) {
		PartEntry item = createPart();
		if(cls != null && !cls.isInstance(item)) return null;
		item.load(node);
		return item;
	}
	
	/** @return items dropped when module is removed */
	@NN public Chance dropItems();
	/** @return items returned to the player */
	@NN public RecipeOutput returnToPlayer();
	
	
}
