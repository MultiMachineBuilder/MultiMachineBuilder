/**
 * 
 */
package mmb.WORLD.crafting;

import java.util.Set;

import mmb.BEANS.Titled;
import mmb.WORLD.items.ItemEntry;
import monniasza.collects.Identifiable;

/**
 * @author oskar
 *
 */
public interface RecipeGroup extends Identifiable<String>, Titled{
	/**@return a set with recipes*/
	public Set<? extends Recipe<?>> recipes();
	
	/**@return a set with all supported items*/
	public Set<? extends ItemEntry> supportedItems();
}
