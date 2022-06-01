/**
 * 
 */
package mmb.WORLD.crafting.recipes;

import java.util.function.Supplier;

import javax.swing.JComponent;

import io.vavr.Tuple2;
import mmb.GlobalSettings;
import mmb.WORLD.crafting.RecipeGroup;
import mmb.WORLD.gui.window.TabRecipes;

/**
 * @author oskar
 *
 */
public abstract class AbstractRecipeGroup implements RecipeGroup{
	public final String title;
	public final String id;
	/**
	 * Create a recipe tab. Invoked once per recipe group
	 * @return
	 */
	protected abstract Tuple2<String, JComponent> createTab();
	protected AbstractRecipeGroup(String id) {
		this.title = GlobalSettings.$res("machine-"+id);
		this.id=id;
		Supplier<Tuple2<String, JComponent>> sup = () -> createTab();
		TabRecipes.add(sup);
		GlobalRecipeRegistrar.addRecipeGroup(this);
	}
	@Override
	public String title() {
		return title;
	}
	@Override
	public String id() {
		return id;
	}
}
