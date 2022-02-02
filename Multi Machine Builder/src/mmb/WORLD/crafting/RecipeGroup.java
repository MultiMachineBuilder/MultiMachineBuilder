/**
 * 
 */
package mmb.WORLD.crafting;

import java.util.Set;

/**
 * @author oskar
 *
 */
public interface RecipeGroup{
	/**
	 * @return a set with recipes
	 */
	public Set<? extends Recipe> recipes();
}
