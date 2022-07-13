/**
 * 
 */
package mmb.WORLD.crafting.recipes;

import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.swing.JComponent;

import io.vavr.Tuple2;
import mmb.GlobalSettings;
import mmb.WORLD.crafting.Recipe;
import mmb.WORLD.crafting.RecipeGroup;
import mmb.WORLD.gui.craft.RecipeList;
import mmb.WORLD.gui.window.TabRecipes;

/**
 * An implementation aid for recipe groups
 * @author oskar
 * @param <T> type of recipes
 */
public abstract class AbstractRecipeGroup<T extends Recipe<T>> implements RecipeGroup<T>{
	@Nonnull private final String title;
	@Nonnull private final String id;
	/**
	 * Create a recipe tab. Invoked once per recipe group
	 * @return
	 */
	protected final Tuple2<String, JComponent> createTab(){
		return new Tuple2<String, JComponent>(title, new RecipeList<>(this));
	}
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
