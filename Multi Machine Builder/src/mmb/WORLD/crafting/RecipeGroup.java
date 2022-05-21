/**
 * 
 */
package mmb.WORLD.crafting;

import java.util.Set;

import mmb.WORLD.items.ItemEntry;

/**
 * @author oskar
 *
 */
public interface RecipeGroup{
	/**@return a set with recipes*/
	public Set<? extends Recipe<?>> recipes();
	
	/**@return a set with all supported items*/
	public Set<? extends ItemEntry> supportedItems();
}
