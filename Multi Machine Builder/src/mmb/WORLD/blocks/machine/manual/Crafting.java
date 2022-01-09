/**
 * 
 */
package mmb.WORLD.blocks.machine.manual;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import it.unimi.dsi.fastutil.ints.IntList;
import mmb.BEANS.BlockActivateListener;
import mmb.WORLD.block.Block;
import mmb.WORLD.contentgen.Materials;
import mmb.WORLD.crafting.RecipeOutput;
import mmb.WORLD.gui.inv.CraftGUI;
import mmb.WORLD.gui.window.WorldWindow;
import mmb.WORLD.inventory.ItemStack;
import mmb.WORLD.item.Item;
import mmb.WORLD.items.ItemEntry;
import mmb.WORLD.worlds.world.World;
import monniasza.collects.grid.FixedGrid;
import monniasza.collects.grid.Grid;
import static mmb.WORLD.blocks.ContentsBlocks.*;
import static mmb.WORLD.blocks.ipipe.Pipes.*;
import static mmb.WORLD.items.ContentsItems.*;

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
	
	//internal recipe mao
	private static final Map<Grid<ItemEntry>, RecipeOutput> _recipes = new HashMap<>();
	/** A read-only map of all recipes */
	public static final Map<Grid<ItemEntry>, RecipeOutput> recipes = Collections.unmodifiableMap(_recipes);
	
	public static void addRecipe(ItemEntry in, RecipeOutput out) {
		addRecipe(new FixedGrid<>(1, in), out);
	}
	public static void addRecipe(ItemEntry in, ItemEntry out, int amount) {
		addRecipe(in, new ItemStack(out, amount));
	}
	public static void addRecipe(Grid<ItemEntry> in, ItemEntry out, int amount) {
		addRecipe(in, new ItemStack(out, amount));
	}
	public static void addRecipe(Grid<ItemEntry> in, RecipeOutput out) {
		/*SimpleInventory outgoing = new SimpleInventory();
		outgoing.setCapacity(Double.POSITIVE_INFINITY);
		out.produceResults(outgoing.createWriter());
		
		SimpleInventory incoming = new SimpleInventory();
		incoming.setCapacity(Double.POSITIVE_INFINITY);
		for(ItemEntry ent: in) {
			if(ent == null) continue;
			incoming.insert(ent, 1);
		}*/
		_recipes.put(in, out);
		
	}
	public static void addRecipeGrid(ItemEntry in, int w, int h, RecipeOutput out) {
		addRecipe(FixedGrid.fill(w, h, in), out);
	}
	public static void addRecipeGrid(ItemEntry in, int w, int h, ItemEntry out, int amount) {
		addRecipe(FixedGrid.fill(w, h, in), out, amount);
	}
	public static void addRecipeGrid(ItemEntry[] in, int w, int h, RecipeOutput out) {
		addRecipe(new FixedGrid<>(w, h, in), out);
	}
	public static void addRecipeGrid(ItemEntry[] in, int w, int h, ItemEntry out, int amount) {
		addRecipe(new FixedGrid<>(w, h, in), out, amount);
	}
	
	//Tiers
	public static final IntList sizes = IntList.of(3, 4, 5, 7, 9, 10);
	public static final List<@Nonnull Block> types;
	static {
		int n = sizes.size();
		Block[] ttypes = new Block[n];
		
		for(int i = 0; i < n; i++) {
			ttypes[i] = initCraftingType(i);
		}
		types = Collections.unmodifiableList(Arrays.asList(ttypes));
	}
	private static boolean inited = false;
	public static void init(){
		if(inited) return;
		inited = true;
		//Generate recipes
		//Crafting ingredients
		addRecipe(logs, plank, 16); // wood → planks
		addRecipeGrid(plank, 2, 2, types.get(0), 1); //4*wood plank → crafting table 1
		addRecipe(new FixedGrid<>(3,
				plank, logs,  plank,
				null,  plank, null,
				null,  plank, null),
				pickHeadWood, 1);
		
		//Pipes
		addRecipe(new FixedGrid<>(3,
				Materials.iron.base,  null, Materials.iron.base,
				Materials.iron.base,  null, Materials.iron.base,
				Materials.iron.base,  null, Materials.iron.base),
				STRAIGHT, 24);
		addRecipe(new FixedGrid<>(3,
				Materials.iron.base, Materials.iron.base,  Materials.iron.base,
				Materials.iron.base,  null, null,
				Materials.iron.base,  null, Materials.iron.base),
				ELBOW, 24);
		addRecipe(new FixedGrid<>(3,
				ELBOW, null,       null,
				null,  Materials.iron.nugget, null,
				null,  null,       ELBOW),
				DUALTURN, 1);
		addRecipe(new FixedGrid<>(3,
				STRAIGHT, null,       null,
				null,  Materials.iron.nugget, null,
				null,  null,       STRAIGHT),
				CROSS, 1);
		addRecipeGrid(new ItemEntry[]{ELBOW, Materials.iron.nugget, STRAIGHT}, 3, 1, TOLEFT, 1);
		addRecipeGrid(new ItemEntry[]{STRAIGHT, Materials.iron.nugget, ELBOW}, 3, 1, TORIGHT, 1);
		
		//Actuator blocks
		//No recipe for Creative Block Placer
		addRecipeGrid(new ItemEntry[]{rod1, bearing1, frame1}, 1, 3, ROTATOR, 1); //Block Rotator
		
		//WireWorld blocks
		addRecipeGrid(new ItemEntry[]{Materials.silicon.base, Materials.copper.base}, 1, 2, ww_wire, 24); //WireWorld cell
		
		//WireWorld gates - 2 inputs
		addRecipe(new FixedGrid<ItemEntry>(3, 2,
				null, YES, null,
				YES, ww_wire, YES),
				OR, 4); //OR
		addRecipe(new FixedGrid<ItemEntry>(3, 2,
				null, NOT, null,
				YES, ww_wire, YES),
				XOR, 4); //XOR
		addRecipe(new FixedGrid<ItemEntry>(3, 2,
				null, NOT, null,
				NOT, ww_wire, NOT),
				AND, 4); //AND
		
		//WireWorld gates - 1 input
		addRecipeGrid(ww_wire, 1, 2, YES); //YES
		addRecipeGrid(new ItemEntry[]{ww_wire, YES}, 1, 2, NOT); //NOT
		
		ItemEntry silicon = Materials.silicon.base;
		ItemEntry iron = Materials.iron.base;
		ItemEntry steel = Materials.steel.base;
		ItemEntry nuggetSteel = Materials.steel.nugget;
		//WireWorld signallers
		addRecipeGrid(new ItemEntry[]{
				silicon , ww_wire, silicon,
				ww_wire, ww_wire, ww_wire,
				silicon, ww_wire, silicon
				}, 3, 3, TRUE, 4); //Always true
		addRecipeGrid(new ItemEntry[]{
				null,  null,  steel,
				null,  steel, null,
				steel, null,  null
				}, 3, 3, RANDOM, 4);
		addRecipeGrid(new ItemEntry[]{
				silicon, silicon, silicon,
				silicon, ww_wire, silicon,
				silicon, silicon, silicon
				}, 3, 3, URANDOM, 4);
		
		//Machine parts
		addRecipeGrid(new ItemEntry[]{
				steel, iron, steel,
				iron,  null, iron,
				steel, iron, steel
				}, 3, 3, frame1);
		addRecipeGrid(new ItemEntry[]{
				null,  null,  steel,
				null,  steel, null,
				steel, null,  null
				}, 3, 3, rod1);
		addRecipeGrid(new ItemEntry[]{
				nuggetSteel,  nuggetSteel,  nuggetSteel,
				nuggetSteel,  null,         nuggetSteel,
				nuggetSteel,  nuggetSteel,  nuggetSteel,
				}, 3, 3, bearing1);
	}
	
	public static void ingotNugget(Item ingot, Item nugget) {
		addRecipeGrid(nugget, 4, 4, ingot, 1);
		addRecipeGrid(ingot, 1, 1, nugget, 16);
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
		CraftGUI component0 = component;
		if(component0 == null) return;
		window.closeWindow(component0);
		component = null;
	}
}
