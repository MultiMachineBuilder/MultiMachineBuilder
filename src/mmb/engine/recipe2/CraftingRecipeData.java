package mmb.engine.recipe2;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import mmb.content.electric.VoltageTier;
import mmb.engine.item.ItemEntry;
import mmb.engine.recipe.ItemList;
import mmb.engine.recipe.SimpleItemList;
import mmb.engine.recipe2.processing.ProcessingRecipeData;
import monniasza.collects.grid.Grid;

/** A crafting recipe. Not all crafting recipe approaches implement voltage tiers. */
public final class CraftingRecipeData implements Recipe<Grid<ItemEntry>>{
	/** The input recipe grid */
	public final Grid<ItemEntry> items;
	/** Required machine tier. Currently none*/
	public final VoltageTier voltage;
	/** Output items */
	public final RecipeOutput out;
	/** Energy required to complete the recipe */
	public final double energyCost;
	/** Energy granted after completion */
	public final double energyReward;
	/** Items counted from the input grid*/
	public final ItemList itemCounts;
	/**
	 * Creates a crafting recipe
	 * @param items grid of items to craft
	 * @param voltage required voltage tier. Not implemented for standard crafting recipes.
	 * @param out recipe outputs
	 * @param energyCost required energy
	 * @param energyReward energy reward
	 */
	public CraftingRecipeData(Grid<ItemEntry> items, VoltageTier voltage, RecipeOutput out, double energyCost,
			double energyReward) {
		super();
		this.items = items;
		this.voltage = voltage;
		this.out = out;
		this.energyCost = energyCost;
		this.energyReward = energyReward;
		Object2IntMap<ItemEntry> map = new Object2IntOpenHashMap<>();
		for(ItemEntry entry: items) {
			if(entry != null) map.computeInt(entry, (item, amt) -> amt == null?1:amt+1);
		}
		this.itemCounts = new SimpleItemList(map);
	}

	@Override
	public ProcessingRecipeData getProcessData() {
		return new ProcessingRecipeData(itemCounts, null, "", 0, voltage, out, energyCost, energyReward);
	}

	@Override
	public Grid<ItemEntry> getConfiguration() {
		return items;
	}
}
