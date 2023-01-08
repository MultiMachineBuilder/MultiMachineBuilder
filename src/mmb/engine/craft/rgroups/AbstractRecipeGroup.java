/**
 * 
 */
package mmb.engine.craft.rgroups;

import java.util.function.Supplier;

import javax.swing.JComponent;
import javax.swing.ListCellRenderer;

import io.vavr.Tuple2;
import mmb.NN;
import mmb.engine.craft.GlobalRecipeRegistrar;
import mmb.engine.craft.PlugAndPlayRecipeCellRenderer;
import mmb.engine.craft.Recipe;
import mmb.engine.craft.RecipeGroup;
import mmb.engine.settings.GlobalSettings;
import mmb.menu.world.craft.RecipeList;
import mmb.menu.world.window.TabRecipes;

/**
 * An implementation aid for recipe groups
 * @author oskar
 * @param <T> type of recipes
 */
public abstract class AbstractRecipeGroup<@NN T extends Recipe<@NN T>> implements RecipeGroup<@NN T>{
	@NN private final ListCellRenderer<T> cellRenderer;
	
	@NN private final String title;
	@NN private final String id;
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
