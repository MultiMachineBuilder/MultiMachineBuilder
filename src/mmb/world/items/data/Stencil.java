/**
 * 
 */
package mmb.world.items.data;

import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Consumer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.ainslec.picocog.PicoWriter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import mmb.data.Save;
import mmb.debug.Debugger;
import mmb.world.crafting.Recipe;
import mmb.world.crafting.RecipeOutput;
import mmb.world.inventory.ItemStack;
import mmb.world.item.ItemEntity;
import mmb.world.item.ItemEntry;
import mmb.world.item.ItemType;
import mmb.world.items.ContentsItems;
import mmb.world.recipes.CraftingGroups;
import mmb.world.recipes.CraftingRecipeGroup.CraftingRecipe;
import monniasza.collects.Collects;
import monniasza.collects.grid.FixedGrid;
import monniasza.collects.grid.Grid;

/**
 * @author oskar
 * Represents a crafting grid recipe
 */
public class Stencil extends ItemEntity{
	
	//Others
	private static final Debugger debug = new Debugger("STENCIL");
	
	//Collection methods

	@Nonnull private Grid<ItemEntry> grid = new FixedGrid<>(0);
	
	/** @return the unmodifiable item grid of this stencil*/
	public Grid<ItemEntry> grid(){
		return Collects.unmodifiableGrid(grid);
	}
	
	//Item methods
	private String title = "Crafting stencil - none";
	@Override
	public String title() {
		return title;
	}

	/**
	 * Creates an empty stencil.
	 */
	public Stencil() {
		//empty
	}
	/**
	 * @param items
	 */
	public Stencil(Grid<ItemEntry> items) {
		this();
		doReplaceTable(items);
	}

	@Override
	public ItemEntry itemClone() {
		return this;
	}

	@Override
	public void load(@Nullable JsonNode array) {
		if(array == null) return;
		if(array.isArray()) {
			Grid<ItemEntry> grid = Save.loadGrid(ItemEntry::loadFromJson, (ArrayNode)array);
			doReplaceTable(grid);
		}else{
			debug.printl("Unsupported JsonNode: "+array.getNodeType());
		}
	}
	@Override
	public JsonNode save() {
		return Save.saveGrid(ItemEntry::saveItem, grid);
	}

	private void doReplaceTable(Grid<ItemEntry> items) {
		Grid<ItemEntry> trim = items.trim();
		grid = trim;
		if(trim.size() == 0) {
			title = "Crafting stencil - none";
			return;
		}
		CraftingRecipe recipe = recipe();
		if(recipe == null) {
			title = "Crafting stencil - invalid";
			return;
		}
		RecipeOutput results = recipe.out;
		PicoWriter writer = new PicoWriter();
		writer.write("Crafting stencil - ");
		results.represent(writer);
		title = writer.toString();
	}

	@Override
	public int hash0() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Objects.hashCode(grid);
		return result;
	}
	@Override
	public boolean equal0(@Nullable ItemEntity obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Stencil other = (Stencil) obj;
		if (!grid.equals(other.grid))
			return false;
		return true;
	}

	//Crafting methods
	private CraftingRecipe recipe0;
	/**
	 * @return recipe for this stencil
	 */
	@Nullable public CraftingRecipe recipe() {
		if(recipe0 == null) 
			recipe0 = CraftingGroups.crafting.findRecipe(grid);
		return recipe0;
	}
	/** @return items consumed by the recipe */
	@Nonnull public RecipeOutput in() {
		return Recipe.in(recipe());
	}
	/** @return items produced by the recipe */
	@Nonnull public RecipeOutput out() {
		return Recipe.out(recipe());
	}
	
	@Override
	public ItemType type() {
		return ContentsItems.stencil;
	}
	
}
