/**
 * 
 */
package mmb.WORLD.blocks.machine.line;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.BEANS.BlockActivateListener;
import mmb.DATA.contents.texture.Textures;
import mmb.WORLD.block.BlockType;
import mmb.WORLD.blocks.ContentsBlocks;
import mmb.WORLD.blocks.machine.SkeletalBlockLinear;
import mmb.WORLD.crafting.RecipeOutput;
import mmb.WORLD.gui.window.WorldWindow;
import mmb.WORLD.inventory.ItemStack;
import mmb.WORLD.items.ContentsItems;
import mmb.WORLD.items.ItemEntry;
import mmb.WORLD.rotate.RotatedImageGroup;
import mmb.WORLD.worlds.world.World;

/**
 * @author oskar
 *
 */
public class Furnace extends SkeletalBlockLinear implements BlockActivateListener {
	/**  This is a list of smelting recipes */
	@Nonnull public static final Map<ItemEntry, RecipeOutput> recipes = new HashMap<>();
	@Nonnull public static final BufferedImage TEXTURE_INERT = Textures.get("machine/smelter inert.png");
	@Nonnull public static final BufferedImage TEXTURE_ACTIVE = Textures.get("machine/smelter active.png");
	@Nonnull public static final RotatedImageGroup IMAGE_INERT = RotatedImageGroup.create(TEXTURE_INERT);
	@Nonnull public static final RotatedImageGroup IMAGE_ACTIVE = RotatedImageGroup.create(TEXTURE_ACTIVE);

	private FurnaceGUI openWindow;
	private SimpleItemProcessHelper processor = new SimpleItemProcessHelper(recipes, incoming, outgoing, 100);
	
	private static boolean inited = false;
	public static void init() {
		if(inited) return;
		
		recipes.put(ContentsBlocks.logs, ContentsItems.coal);//Wood log => coal
		recipes.put(ContentsBlocks.iron_ore, ContentsItems.iron);
		recipes.put(ContentsBlocks.copper_ore, ContentsItems.copper);
		recipes.put(ContentsBlocks.silicon_ore, ContentsItems.silicon);
		recipes.put(ContentsBlocks.gold_ore, ContentsItems.gold);
		recipes.put(ContentsBlocks.silver_ore, ContentsItems.silver);
		recipes.put(ContentsBlocks.coal_ore, new ItemStack(ContentsItems.coal, 3));
		
		inited = true;
	}

	@Override
	protected void save2(ObjectNode node) {
		processor.save(node);
	}
	@Override
	protected void load2(ObjectNode node) {
		processor.load(node);
	}

	@Override
	public BlockType type() {
		return ContentsBlocks.FURNACE;
	}

	@Override
	public RotatedImageGroup getImage() {
		if(processor.underway == null) return IMAGE_INERT;
		return IMAGE_ACTIVE;
	}

	/**
	 * @return item, which is currently smelting
	 */
	public ItemEntry getSmeltingUnderway() {
		return processor.underway;
	}

	/**
	 * Sets the current item which is smelting
	 * @param smeltingUnderway new item to smelt
	 */
	public void setSmeltingUnderway(ItemEntry smeltingUnderway) {
		processor.underway = smeltingUnderway;
	}

	@Override
	public void cycle() {
		processor.cycle();
	}

	/**
	 * @return the remaining progress of smelting this item
	 */
	public int getRemaining() {
		return processor.remaining;
	}

	/**
	 * @param remaining new remaining progress
	 */
	public void setRemaining(int remaining) {
		processor.remaining = remaining;
	}

	@Override
	public void click(int blockX, int blockY, World map, @Nullable WorldWindow window, double partX, double partY) {
		if(window == null) return;
		if(openWindow != null) return;
		openWindow = new FurnaceGUI(this, window);
		window.openAndShowWindow(openWindow, "Furnace");
		processor.refreshable = openWindow;
	}
	
	void closeWindow() {
		openWindow = null;
		processor.refreshable = null;
	}
		
}

