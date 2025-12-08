/**
 * 
 */
package mmb.content.electric.recipes;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

import javax.swing.JComponent;
import javax.swing.ListCellRenderer;

import io.vavr.Tuple2;
import mmb.annotations.NN;
import mmb.engine.item.ItemEntry;
import mmb.engine.recipe.GlobalRecipeRegistrar;
import mmb.engine.recipe.PlugAndPlayRecipeCellRenderer;
import mmb.engine.recipe.Recipe;
import mmb.engine.recipe.RecipeGroup;
import mmb.engine.recipe.RecipeList;
import mmb.engine.settings.GlobalSettings;
import mmb.menu.world.window.TabRecipes;
import monniasza.collects.Collects;
import monniasza.collects.Identifiable;
import monniasza.collects.selfset.HashSelfSet;
import monniasza.collects.selfset.SelfSet;

/**
 * An implementation aid for recipe groups.
 * @dontextend This class should not be extended directly.
 * Extend {@link AbstractRecipeGroupCatalyzed} or {@link AbstractRecipeGroupUncatalyzed} instead
 * @author oskar
 * @param <Tbackend> the backend input identification type
 * @param <Trecipe> type of recipes
 */
public abstract class AbstractRecipeGroup<@NN Tbackend, @NN Trecipe extends Recipe<@NN Trecipe>&Identifiable<Tbackend>> implements RecipeGroup<@NN Trecipe>{
	private final ListCellRenderer<Trecipe> cellRenderer;
	
	private final String title;
	private final String id;
	/**
	 * Create a recipe tab. Invoked once per recipe group
	 * @return
	 */
	protected final Tuple2<String, JComponent> createTab(){
		return new Tuple2<String, JComponent>(title, new RecipeList<>(this));
	}
	protected AbstractRecipeGroup(String id, Class<Trecipe> rtype) {
		//Backend generation
		this.rtype = rtype;
		recipesBackend = HashSelfSet.createNonnull(rtype);
		recipesView = Collects.unmodifiableSelfSet(recipesBackend);
		
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
	public ListCellRenderer<? super Trecipe> cellRenderer() {
		return cellRenderer;
	}
	
	//Recipe listing backend
	/** Type of recipes */
	public final Class<Trecipe> rtype;
	private final SelfSet<Tbackend, Trecipe> recipesBackend;
	private final SelfSet<Tbackend, Trecipe> recipesView;
	@Override
	public @NN SelfSet<Tbackend, Trecipe> recipes() {
		return recipesView;
	}
	
	//Supported items
	private final Set<ItemEntry> supportedBackend = new HashSet<>();
	private final Set<ItemEntry> supportedView = Collections.unmodifiableSet(supportedBackend);
	@Override
	public Set<? extends ItemEntry> supportedItems() {
		return supportedView;
	}
	
	//Recipe insertion
	protected void insert(Trecipe recipe) {
		Tbackend recipeid = recipe.id();
		Set<ItemEntry> items = items4id(recipeid);
		
		//Supported items addition
		for(ItemEntry item: items) {
			supportedBackend.add(item);
		}
		
		//Recipe addition
		recipesBackend.add(recipe);
		
		//GlobalRecipeRegistrar
		GlobalRecipeRegistrar.addRecipe(recipe);
	}
	
	//Backend conversion
	/**
	 * Gets the ID for a backend type
	 * @param id items to identify
	 * @return item ID
	 */
	public abstract Set<ItemEntry> items4id(Tbackend id);
	
}
