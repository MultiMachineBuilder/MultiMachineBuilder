/**
 * 
 */
package mmb.world.recipes;

import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.swing.JComponent;
import javax.swing.ListCellRenderer;

import io.vavr.Tuple2;
import mmb.GlobalSettings;
import mmb.menu.world.craft.RecipeList;
import mmb.menu.world.window.TabRecipes;
import mmb.world.crafting.GlobalRecipeRegistrar;
import mmb.world.crafting.PlugAndPlayRecipeCellRenderer;
import mmb.world.crafting.Recipe;
import mmb.world.crafting.RecipeGroup;

/**
 * An implementation aid for recipe groups
 * @author oskar
 * @param <T> type of recipes
 */
public abstract class AbstractRecipeGroup<T extends Recipe<T>> implements RecipeGroup<T>{
	@Nonnull private final ListCellRenderer<T> cellRenderer;
	
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
		cellRenderer = new PlugAndPlayRecipeCellRenderer<>(createView());
		Supplier<Tuple2<String, JComponent>> sup = this::createTab;
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
	@Override
	public ListCellRenderer<? super T> cellRenderer() {
		return cellRenderer;
	}
}
