/**
 * 
 */
package mmb.content.ditems;

import java.util.Objects;
import org.ainslec.picocog.PicoWriter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import mmb.annotations.NN;
import mmb.annotations.Nil;
import mmb.content.ContentsItems;
import mmb.content.CraftingGroups;
import mmb.content.craft.CraftingRecipeGroup.CraftingRecipe;
import mmb.engine.debug.Debugger;
import mmb.engine.item.ItemEntity;
import mmb.engine.item.ItemEntry;
import mmb.engine.item.ItemType;
import mmb.engine.json.JsonTool;
import mmb.engine.recipe.Recipe;
import mmb.engine.recipe.ItemList;
import monniasza.collects.Collects;
import monniasza.collects.grid.FixedGrid;
import monniasza.collects.grid.Grid;

/**
 * @author oskar
 * Represents a crafting grid recipe
 */
public class Stencil extends ItemEntity{
	private static final Debugger debug = new Debugger("STENCIL");
	
	//Constructors
	/** Creates an empty stencil. */
	public Stencil() {
		//empty
	}
	/** @param items */
	public Stencil(Grid<@Nil ItemEntry> items) {
		this();
		doReplaceTable(items);
	}
	
	//Contents
	@NN private Grid<@Nil ItemEntry> grid = new FixedGrid<>(0);
	/** @return the unmodifiable item grid of this stencil*/
	public Grid<@Nil ItemEntry> grid(){
		return Collects.unmodifiableGrid(grid);
	}
	
	//Item methods
	private String title = "Crafting stencil - none";
	@Override
	public String title() {
		return title;
	}
	@Override
	public int hash0() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Objects.hashCode(grid);
		return result;
	}
	@Override
	public boolean equal0(@Nil ItemEntity obj) {
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
	@Override
	public ItemEntry itemClone() {
		return this;
	}
	@Override
	public ItemType type() {
		return ContentsItems.stencil;
	}
	
	//Serialization
	@Override
	public void load(@Nil JsonNode array) {
		if(array == null) return;
		if(array.isArray()) {
			Grid<@Nil ItemEntry> grid1 = JsonTool.loadGrid(ItemEntry::loadFromJson, (ArrayNode)array);
			doReplaceTable(grid1);
		}else{
			debug.printl("Unsupported JsonNode: "+array.getNodeType());
		}
	}
	@Override
	public JsonNode save() {
		return JsonTool.saveGrid(ItemEntry::saveItem, grid);
	}
	private void doReplaceTable(Grid<@Nil ItemEntry> items) {
		Grid<@Nil ItemEntry> trim = items.trim();
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
		ItemList results = recipe.out;
		PicoWriter writer = new PicoWriter();
		writer.write("Crafting stencil - ");
		results.represent(writer);
		title = writer.toString();
	}

	//Crafting methods
	private CraftingRecipe recipe0;
	/**
	 * @return recipe for this stencil
	 */
	@Nil public CraftingRecipe recipe() {
		if(recipe0 == null) 
			recipe0 = CraftingGroups.crafting.findRecipe(grid);
		return recipe0;
	}
	/** @return items consumed by the recipe */
	@NN public ItemList in() {
		return Recipe.in(recipe());
	}
	/** @return items produced by the recipe */
	@NN public ItemList out() {
		return Recipe.out(recipe());
	}
}
