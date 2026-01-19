package mmb.engine.recipe2;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import mmb.annotations.NN;
import mmb.annotations.Nil;
import mmb.engine.item.ItemEntry;
import mmb.engine.recipe.RecipeView;
import monniasza.collects.indexar.Database;
import monniasza.collects.indexar.ManyToManyIndex;
import monniasza.collects.indexar.OneToManyIndex;
import monniasza.collects.indexar.OneToOneIndex;

/**
 * A base class for all recipe groups. Each recipe must have unique combination of input items
 * @param <Trecipe> type of recipe entries
 * @param <Tconfig> type of recipe configurations
 * @param <Tkey> type of unique identifiers of recipes
 */
public abstract class RecipeGroup<@NN Trecipe extends Recipe<Tconfig>, Tconfig, Tkey> {
	public final Database<Trecipe> database;
	public final OneToOneIndex<Trecipe, Tkey> recipeKeyIndex;
	public final ManyToManyIndex<Trecipe, ItemEntry> inputItemsIndex;
	public final ManyToManyIndex<Trecipe, ItemEntry> outputItemsIndex;
	public final OneToManyIndex<Trecipe, Tconfig> recipeConfigIndex;
	public final OneToManyIndex<Trecipe, Set<ItemEntry>> exactItemsIndex;
	public final OneToOneIndex<Trecipe, ConfigItemPair<Tconfig>> configItemIndex;
	
	public record ConfigItemPair<Tconfig>(Tconfig config, Set<ItemEntry> items) {}
	
	/**
	 * Abstract constructor for use in recipe groups
	 * @param recipeClass Java class of recipes
	 * @param id the ID of the recipe group for indexing
	 */
	public RecipeGroup(Class<Trecipe> recipeClass, String id) {
		database = new Database<>(recipeClass);
		recipeKeyIndex = new OneToOneIndex<>(x -> findID(x));
		database.addIndex(recipeKeyIndex);
		inputItemsIndex = new ManyToManyIndex<>(x -> x.getProcessData().inputs().items());
		database.addIndex(inputItemsIndex);
		outputItemsIndex = new ManyToManyIndex<>(x -> x.getProcessData().out().items);
		database.addIndex(outputItemsIndex);
		recipeConfigIndex = new OneToManyIndex<>(x -> x.getConfiguration());
		database.addIndex(recipeConfigIndex);
		exactItemsIndex = new OneToManyIndex<>(x -> x.getProcessData().inputs().items());
		database.addIndex(exactItemsIndex);
		configItemIndex = new OneToOneIndex<>(x -> new ConfigItemPair<>(x.getConfiguration(), x.getProcessData().inputs().items()));
		database.addIndex(configItemIndex);
	}
	public abstract Tkey findID(Trecipe recipe);
	public abstract RecipeView<Trecipe> getRecipeViewComponent();
	
	public Set<Trecipe> findRecipes(@Nil Tconfig machineConfig, Set<@NN ItemEntry> requiredItems, boolean exact){
		//Find all candidate sets
		List<Set<Trecipe>> sets = new ArrayList<>();
		
		if(exact) {
			sets.add(exactItemsIndex.multimap().get(requiredItems));
		}else{
			for(ItemEntry item: requiredItems) sets.add(inputItemsIndex.multimap().get(item));
		}
		if(machineConfig != null) sets.add(recipeConfigIndex.multimap().get(machineConfig));
		
		//Find the smallest set
		Set<Trecipe> builder = null;
		for(var set: sets) {
			if(set == null || set.size() == 0) return Set.of(); //Empty set found
			if(builder == null) {
				builder = set;
			}else {
				builder.retainAll(set);
			}
		}
		return (builder == null) ? database : builder;
	}
	
	/**
	 * Finds all recipes for use in autocrafters and then calls the handler {@code onRecipeHit} with every hit.
	 * @param stencilOrCatalyst
	 * @param craftword
	 * @param craftcode
	 * @param items
	 * @param onRecipeHit invoked for each matching recipe
	 * @param exact has the match to be exact?
	 */
	public abstract void findForAutocrafters(ItemEntry stencilOrCatalyst, String craftword, int craftcode, Set<@NN ItemEntry> items, Consumer<Trecipe> onRecipeHit, boolean exact);
}
