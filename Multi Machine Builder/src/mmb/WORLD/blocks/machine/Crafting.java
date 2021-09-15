/**
 * 
 */
package mmb.WORLD.blocks.machine;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import it.unimi.dsi.fastutil.ints.IntList;
import mmb.BEANS.BlockActivateListener;
import mmb.WORLD.block.Block;
import mmb.WORLD.blocks.ContentsBlocks;
import mmb.WORLD.crafting.RecipeOutput;
import mmb.WORLD.gui.inv.CraftGUI;
import mmb.WORLD.gui.window.WorldWindow;
import mmb.WORLD.inventory.ItemStack;
import mmb.WORLD.items.ItemEntry;
import mmb.WORLD.worlds.world.World;
import monniasza.collects.grid.FixedGrid;
import monniasza.collects.grid.Grid;

/**
 * @author oskar
 *
 */
public class Crafting extends Block implements BlockActivateListener {
	/** The size of this crafting table */
	public final int size;
	/** The tier of the crafting table */
	public final int tier;
	public Crafting(int tier) {
		size = sizes.getInt(tier);
		this.tier = tier;
	}
	
	//Crafing recipes
	/**
	 * Gets the recipe for given item grid
	 * @param grid the item grid
	 * @return recipe output for given grid
	 */
	public static RecipeOutput findRecipe(Grid<ItemEntry> grid) {
		Grid<ItemEntry> trim = grid.trim();
		return recipes.get(trim);
	}
	/** A map of all recipes */
	public static final Map<Grid<ItemEntry>, RecipeOutput> recipes = new HashMap<>();
			
	//Tiers
	public static final IntList sizes = IntList.of(2, 3, 4, 5, 7, 9);
	public static final List<Block> types;
	static {
		int n = sizes.size();
		Block[] ttypes = new Block[n];
		
		for(int i = 0; i < n; i++) {
			ttypes[i] = initCraftingType(i);
		}
		types = Collections.unmodifiableList(Arrays.asList(ttypes));
		
		//Generate recipes
		recipes.put(new FixedGrid<>(1, ContentsBlocks.logs), new ItemStack(ContentsBlocks.plank, 16));
	}
	public static void init() {
		//initialization
	}
	static Block initCraftingType(int tier) {
		int increased = tier + 1;
		String textureName = "machine/assembly "+increased+".png";
		String idName = "crafting."+increased;
		String title = "Crafter Mk"+increased;
		
		Block type = new Crafting(tier);
		type.title(title);
		type.texture(textureName);
		type.register(idName);
		return type;
	}
	
	
	
	private CraftGUI component;
	@Override
	public void click(int blockX, int blockY, World map, @Nullable WorldWindow window) {
		if(window == null) return;
		if(component != null) return;
		component = new CraftGUI(size, window.getPlayer().inv, this, window);
		window.openAndShowWindow(component, "Tier "+tier+" crafting");
	}
	
	public void closeWindow(WorldWindow window) {
		if(component == null) return;
		window.closeWindow(component);
		component = null;
	}
}
