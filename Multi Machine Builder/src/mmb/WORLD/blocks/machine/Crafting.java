/**
 * 
 */
package mmb.WORLD.blocks.machine;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.BEANS.BlockActivateListener;
import mmb.DATA.contents.texture.Textures;
import mmb.WORLD.block.Block;
import mmb.WORLD.block.BlockEntityType;
import mmb.WORLD.block.BlockFactory;
import mmb.WORLD.block.BlockType;
import mmb.WORLD.block.SkeletalBlockEntityData;
import mmb.WORLD.blocks.ContentsBlocks;
import mmb.WORLD.crafting.RecipeOutput;
import mmb.WORLD.gui.inv.CraftGUI;
import mmb.WORLD.gui.window.WorldWindow;
import mmb.WORLD.inventory.ItemEntry;
import mmb.WORLD.inventory.ItemStack;
import mmb.WORLD.texture.BlockDrawer;
import mmb.WORLD.worlds.world.World;
import mmb.WORLD.worlds.world.World.BlockMap;
import monniasza.collects.grid.FixedGrid;
import monniasza.collects.grid.Grid;

/**
 * @author oskar
 *
 */
public class Crafting extends Block implements BlockActivateListener {
	public final int size;
	public final int tier;
	public final Map<Grid<ItemEntry>, RecipeOutput> outs = new HashMap<>();
	public Crafting(int tier) {
		size = sizes[tier];
		this.tier = tier;
		setupRefsTable();
	}
	
	//Tiers
	static int[] sizes = {2, 3, 4, 5, 7, 9};
	static Block[] types; //initialized below
	public static void setupRefsTable() {
		if(types == null) {
			int n = 6;
			types = new Block[n];
			for(int i = 0; i < n; i++) {
				types[i] = initCraftingType(i);
			}
		}
		
		//Generate recipes
		recipes.put(new FixedGrid<>(1, ContentsBlocks.logs), new ItemStack(ContentsBlocks.plank, 16));
	}
	static Block initCraftingType(int tier) {
		int increased = tier + 1;
		String textureName = "machine/assembly "+increased+".png";
		String idName = "crafting."+increased;
		BlockDrawer drawer = BlockDrawer.ofImage(Textures.get(textureName));
		String title = "Crafter Mk"+increased;
		
		
		Block type = new Crafting(tier);
		type.title = title;
		type.drawer = drawer;
		type.register(idName);
		return type;
	}
	
	//Crafing recipes
	/**
	 * A map of all recipes
	 */
	public static final Map<Grid<ItemEntry>, RecipeOutput> recipes = new HashMap<>();
	public static RecipeOutput findRecipe(Grid<ItemEntry> grid) {
		Grid<ItemEntry> trim = grid.trim();
		return recipes.get(trim);
	}
	
	private CraftGUI component;
	@Override
	public void click(int blockX, int blockY, World map, WorldWindow window) {
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
