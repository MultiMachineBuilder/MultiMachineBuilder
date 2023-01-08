/**
 * 
 */
package mmb.menu.world.craft;

import javax.swing.JPanel;

/**
 * Represents an abstraction for recipe views
 * @author oskar
 * @param <T> type of recipe (any Java reference type is valid)
 */
public abstract class RecipeView<T> extends JPanel {
	private static final long serialVersionUID = -3618326216243742625L;
	/**
	 * Replaces information in this recipe view for given recipe
	 * @param recipe recipe to use for this recipe view
	 */
	public abstract void set(T recipe);
}
