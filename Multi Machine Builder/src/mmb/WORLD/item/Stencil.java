/**
 * 
 */
package mmb.WORLD.item;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

import javax.annotation.Nullable;

import org.ainslec.picocog.PicoWriter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import mmb.DATA.SaveGrids;
import mmb.DATA.json.JsonTool;
import mmb.WORLD.blocks.machine.Crafting;
import mmb.WORLD.crafting.RecipeOutput;
import mmb.WORLD.inventory.ItemEntry;
import mmb.debug.Debugger;
import monniasza.collects.grid.FixedGrid;
import monniasza.collects.grid.Grid;

/**
 * @author oskar
 *
 */
public class Stencil extends ItemEntity implements Grid<ItemEntry> {

	@Override
	public FixedGrid<ItemEntry> copy(int x, int y, int w, int h) {
		return grid.copy(x, y, w, h);
	}

	@Override
	public void forEach(@SuppressWarnings("null") Consumer<? super ItemEntry> c) {
		grid.forEach(c);
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

	private Grid<ItemEntry> grid = new FixedGrid<>(0);
	private String title = "Crafting stencil - none";
	private static final Debugger debug = new Debugger("STENCIL");
	@Override
	public String title() {
		return title;
	}

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
	public void load(JsonNode array) {
		if(array.isArray()) {
			Grid<ItemEntry> grid = SaveGrids.loadGrid(ItemEntry::loadFromJson, (ArrayNode)array);
			doReplaceTable(grid);
		}else{
			debug.printl("Unsupported JsonNode: "+array.getNodeType());
		}
	}
	@Override
	public JsonNode save() {
		return SaveGrids.saveGrid(ItemEntry::saveItem, grid);
	}

	private void doReplaceTable(Grid<ItemEntry> items) {
		Grid<ItemEntry> trim = items.trim();
		grid = trim;
		RecipeOutput rout = Crafting.recipes.get(trim);
		if(trim.size() == 0) {
			title = "Crafting stencil - none";
		}else if(rout == null) {
			title = "Crafting stencil - invalid";
		}else {
			PicoWriter writer = new PicoWriter();
			writer.write("Crafting stencil - ");
			rout.represent(writer);
			title = writer.toString();
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((grid == null) ? 0 : grid.hashCode());
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

	@Override
	public void set(int x, int y, @Nullable ItemEntry data) {
		throw new UnsupportedOperationException("Stencil is read-only");
	}
	
}
