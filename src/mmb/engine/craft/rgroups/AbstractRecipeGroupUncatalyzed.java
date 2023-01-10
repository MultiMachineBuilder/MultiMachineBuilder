/**
 * 
 */
package mmb.engine.craft.rgroups;

import java.util.Set;

import mmb.NN;
import mmb.engine.craft.Recipe;
import mmb.engine.craft.RecipeOutput;
import mmb.engine.item.ItemEntry;
import monniasza.collects.Identifiable;

/**
 * An implementation aid for uncatalyzed recipe groups
 * @author oskar
 * @param <Tlist> the item selection type
 * @param <Trecipe> type of recipes
 */
public abstract class AbstractRecipeGroupUncatalyzed<@NN Tlist extends RecipeOutput, @NN Trecipe extends Recipe<@NN Trecipe>&Identifiable<Tlist>>
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
