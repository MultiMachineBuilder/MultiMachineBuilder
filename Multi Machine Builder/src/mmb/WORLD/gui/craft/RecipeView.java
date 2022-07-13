/**
 * 
 */
package mmb.WORLD.gui.craft;

import javax.swing.JPanel;

/**
 * @author oskar
 *
 */
public abstract class RecipeView<T> extends JPanel {
	private static final long serialVersionUID = -3618326216243742625L;
	public abstract void set(T recipe);
}
