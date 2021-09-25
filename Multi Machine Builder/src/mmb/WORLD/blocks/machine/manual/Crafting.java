/**
 * 
 */
package mmb.WORLD.blocks.machine.manual;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import it.unimi.dsi.fastutil.ints.IntList;
import mmb.BEANS.BlockActivateListener;
import mmb.WORLD.block.Block;
import mmb.WORLD.crafting.RecipeOutput;
import mmb.WORLD.gui.inv.CraftGUI;
import mmb.WORLD.gui.window.WorldWindow;
import mmb.WORLD.inventory.ItemStack;
import mmb.WORLD.item.Item;
import mmb.WORLD.items.ItemEntry;
import mmb.WORLD.worlds.world.World;
import mmb.debug.Debugger;
import monniasza.collects.grid.FixedGrid;
import monniasza.collects.grid.Grid;
import static mmb.WORLD.blocks.ContentsBlocks.*;
import static mmb.WORLD.items.ContentsItems.*;
import static mmb.WORLD.blocks.pipe.Pipes.*;

/**
 * @author oskar
 *
 */
public class Crafting extends Block implements BlockActivateListener {
	private static final Debugger debug = new Debugger("CRAFTING RECIPES");
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
	public static final IntList sizes = IntList.of(3, 4, 5, 7, 9, 10);
	public static final List<Block> types;
	static {
		int n = sizes.size();
		Block[] ttypes = new Block[n];
		
		for(int i = 0; i < n; i++) {
			ttypes[i] = initCraftingType(i);
		}
		types = Collections.unmodifiableList(Arrays.asList(ttypes));
		
		//Generate recipes
		recipes.put(new FixedGrid<>(1, logs), new ItemStack(plank, 16)); // wood → planks
		recipes.put(new FixedGrid<>(2,
				plank, plank,
				plank, plank),
				types.get(0)); //4*wood plank → crafting table 1
		recipes.put(new FixedGrid<>(3,
				plank, logs,  plank,
				null,  plank, null,
				null,  plank, null),
				pickHeadWood);
		
		//Pipes
		recipes.put(new FixedGrid<>(3,
				iron, null,  iron,
				iron,  null, iron,
				iron,  null, iron),
				new ItemStack(STRAIGHT, 24));
		recipes.put(new FixedGrid<>(3,
				iron, iron,  iron,
				iron,  null, null,
				iron,  null, iron),
				new ItemStack(ELBOW, 24));
		recipes.put(new FixedGrid<>(3,
				ELBOW, null,       null,
				null,  nuggetIron, null,
				null,  null,       ELBOW),
				DUALTURN);
		recipes.put(new FixedGrid<>(3,
				STRAIGHT, null,       null,
				null,  nuggetIron, null,
				null,  null,       STRAIGHT),
				CROSS);
		
		
		//Ingot <--> nugget
		ingotNugget(stainless, nuggetStainless);
		ingotNugget(gold, nuggetGold);
		ingotNugget(iron, nuggetIron);
		ingotNugget(copper, nuggetCopper);
		ingotNugget(silicon, nuggetSilicon);
		ingotNugget(uranium, nuggetUranium);
		ingotNugget(steel, nuggetSteel);
	}
	
	private static void ingotNugget(Item ingot, Item nugget) {
		recipes.put(new FixedGrid<>(4,
				nugget, nugget, nugget, nugget,
				nugget, nugget, nugget, nugget,
				nugget, nugget, nugget, nugget,
				nugget, nugget, nugget, nugget)
				, ingot);
		recipes.put(new FixedGrid<>(1, ingot), new ItemStack(nugget, 16));
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
	public void click(int blockX, int blockY, World map, @Nullable WorldWindow window, double partX, double partY) {
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
