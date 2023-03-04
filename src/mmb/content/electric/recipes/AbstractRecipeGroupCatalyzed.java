/**
 * 
 */
package mmb.content.electric.recipes;

import java.util.Set;

import io.vavr.Tuple2;
import mmb.NN;
import mmb.engine.item.ItemEntry;
import mmb.engine.recipe.Recipe;
import mmb.engine.recipe.RecipeOutput;
import monniasza.collects.Identifiable;

/**
 * An implementation aid for catalyzed recipe groups
 * @author oskar
 * @param <Tlist> the item selection type
 * @param <Trecipe> type of recipes
 */
public abstract class AbstractRecipeGroupCatalyzed<@NN Tlist extends RecipeOutput, 
@NN Trecipe extends Recipe<@NN Trecipe>&Identifiable<@NN Tuple2<Tlist, ItemEntry>>>
extends AbstractRecipeGroup<@NN Tuple2<Tlist, ItemEntry>, Trecipe> {
	protected AbstractRecipeGroupCatalyzed(String id, Class<@NN Trecipe> rtype) {
		super(id, rtype);
	}
	@Override
	public Set<ItemEntry> items4id(@NN Tuple2<Tlist, ItemEntry> id) {
		return id._1.items();
	}
	@Override
	public final boolean isCatalyzed() {
		return true;
	}
	
}
