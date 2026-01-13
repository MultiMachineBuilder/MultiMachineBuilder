/**
 * 
 */
package mmb.content.electric.recipes;

import java.util.Set;

import mmb.annotations.NN;
import mmb.engine.item.ItemEntry;
import mmb.engine.recipe.Recipe;
import mmb.engine.recipe.ItemList;
import monniasza.collects.Identifiable;

/**
 * An implementation aid for uncatalyzed recipe groups
 * @author oskar
 * @param <Tlist> the item selection type
 * @param <Trecipe> type of recipes
 */
public abstract class AbstractRecipeGroupUncatalyzed<@NN Tlist extends ItemList, @NN Trecipe extends Recipe<@NN Trecipe>&Identifiable<Tlist>>
extends AbstractRecipeGroup<Tlist, Trecipe> {
	protected AbstractRecipeGroupUncatalyzed(String id, Class<@NN Trecipe> rtype) {
		super(id, rtype);
	}
	@Override
	public Set<ItemEntry> items4id(@NN Tlist id) {
		return id.items();
	}
	@Override
	public final boolean isCatalyzed() {
		return false;
	}
	
}
