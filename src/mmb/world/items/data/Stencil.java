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
import mmb.world.crafting.RecipeOutput;
import mmb.world.item.ItemEntity;
import mmb.world.item.ItemType;
import mmb.world.items.ContentsItems;
import mmb.world.items.ItemEntry;
import mmb.world.recipes.CraftingGroups;
import mmb.world.recipes.CraftingRecipeGroup.CraftingRecipe;
import monniasza.collects.grid.FixedGrid;
import monniasza.collects.grid.Grid;

/**
 * @author oskar
 * Represents a crafting grid recipe
 */
public class Stencil extends ItemEntity implements Grid<@Nullable ItemEntry>{
	
	//Others
	private static final Debugger debug = new Debugger("STENCIL");
	
	//Collection methods
	@Override
	public FixedGrid<ItemEntry> copy(int x, int y, int w, int h) {
		return grid.copy(x, y, w, h);
	}

	@Override
	public void set(int x, int y, @Nullable ItemEntry data) {
		throw new UnsupportedOperationException("Stencil is read-only");
	}
	@Override
	public ItemEntry get(int x, int y) {
		return grid.get(x, y);
	}

	@Override
	public int width() {
		return grid.width();
	}
	@Override
	public int height() {
		return grid.height();
	}
	@Override
	public int size() {
		return grid.size();
	}

	@Override
	public FixedGrid<ItemEntry> trim() {
		return grid.trim();
	}

	@Override
	public Iterator<ItemEntry> iterator() {
		return grid.iterator();
	}
	@Override
	public Spliterator<ItemEntry> spliterator() {
		return grid.spliterator();
	}
	@Override
	public void forEach(Consumer<? super ItemEntry> c) {
		grid.forEach(c);
	}

	@Nonnull private Grid<ItemEntry> grid = new FixedGrid<>(0);
	
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
		if (grid == null) {
			if (other.grid != null)
				return false;
		} else if (!grid.equals(other.grid))
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

	@Override
	public ItemType type() {
		return ContentsItems.stencil;
	}
	
}
