/**
 * 
 */
package mmb.WORLD.items;

import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Consumer;

import javax.annotation.Nullable;

import org.ainslec.picocog.PicoWriter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import mmb.DATA.Save;
import mmb.WORLD.blocks.machine.manual.Crafting;
import mmb.WORLD.crafting.RecipeOutput;
import mmb.WORLD.inventory.Inventory;
import mmb.WORLD.inventory.storage.SimpleInventory;
import mmb.WORLD.item.ItemEntity;
import mmb.WORLD.recipe_old.Recipe;
import mmb.debug.Debugger;
import monniasza.collects.grid.FixedGrid;
import monniasza.collects.grid.Grid;

/**
 * @author oskar
 * Represents a crafting grid recipe
 */
public class Stencil extends ItemEntity implements Grid<@Nullable ItemEntry>, Recipe {
	
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
	public void forEach(@SuppressWarnings("null") Consumer<? super ItemEntry> c) {
		grid.forEach(c);
	}

	private Grid<ItemEntry> grid = new FixedGrid<>(0);
	
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
		super(ContentsItems.stencil);
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
		results = Crafting.recipes.get(trim);
		if(trim.size() == 0) {
			title = "Crafting stencil - none";
		}else if(results == null) {
			title = "Crafting stencil - invalid";
		}else {
			PicoWriter writer = new PicoWriter();
			writer.write("Crafting stencil - ");
			results.represent(writer);
			title = writer.toString();
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Objects.hashCode(grid);
		return result;
	}
	@Override
	public boolean equals(@Nullable Object obj) {
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
	@Override
	public int maxCraftable(Inventory src, int amount) {
		Inventory ins = inputs();
		debug.printl(ins+" in "+src);
		return Math.min(amount, Inventory.howManyTimesThisContainsThat(src, ins));
	}
	@Override
	public int craft(Inventory src, Inventory tgt, int amount) {
		return Recipe.transact(inputs(), output(), tgt, src, amount);
	}
	
	private RecipeOutput results;
	@SuppressWarnings("null")
	@Override
	public RecipeOutput output() {
		if(results == null) return RecipeOutput.NONE;
		return results;
	}
	
	private Inventory ins;
	@SuppressWarnings("null")
	@Override
	public Inventory inputs() { //FIXME the inventory contains null records
		if(ins == null) {
			SimpleInventory inv = new SimpleInventory();
			inv.setCapacity(Double.POSITIVE_INFINITY);
			for(ItemEntry entry: grid) {
				if(entry != null) inv.insert(entry, 1);
			}
			ins = inv.readOnly();
			debug.printl("Ingredients: "+inv);
		}
		return ins;
	}
	
}
