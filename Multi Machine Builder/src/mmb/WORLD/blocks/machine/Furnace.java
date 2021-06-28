/**
 * 
 */
package mmb.WORLD.blocks.machine;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.BEANS.BlockActivateListener;
import mmb.DATA.contents.texture.Textures;
import mmb.DATA.json.JsonTool;
import mmb.WORLD.RotatedImageGroup;
import mmb.WORLD.Side;
import mmb.WORLD.block.BlockType;
import mmb.WORLD.block.SkeletalBlockEntityRotary;
import mmb.WORLD.blocks.ContentsBlocks;
import mmb.WORLD.crafting.RecipeOutput;
import mmb.WORLD.gui.machine.Smelting;
import mmb.WORLD.gui.window.WorldWindow;
import mmb.WORLD.inventory.Inventory;
import mmb.WORLD.inventory.ItemRecord;
import mmb.WORLD.inventory.NoSuchInventory;
import mmb.WORLD.inventory.storage.SimpleInventory;
import mmb.WORLD.items.ContentsItems;
import mmb.WORLD.items.ItemEntry;
import mmb.WORLD.worlds.MapProxy;
import mmb.WORLD.worlds.world.BlockMap;
import mmb.WORLD.worlds.world.World;

/**
 * @author oskar
 *
 */
public class Furnace extends SkeletalBlockEntityRotary implements BlockActivateListener {
	/**
	 * This is a list of smelting recipes
	 */
	@Nonnull public static final Map<ItemEntry, RecipeOutput> recipes = new HashMap<>();
	@Nonnull public static final BufferedImage TEXTURE_INERT = Textures.get("machine/smelter inert.png");
	@Nonnull public static final BufferedImage TEXTURE_ACTIVE = Textures.get("machine/smelter active.png");
	@Nonnull public static final RotatedImageGroup IMAGE_INERT = RotatedImageGroup.create(TEXTURE_INERT);
	@Nonnull public static final RotatedImageGroup IMAGE_ACTIVE = RotatedImageGroup.create(TEXTURE_ACTIVE);
	@Nonnull public final SimpleInventory in = new SimpleInventory();
	@Nonnull private final SimpleInventory _out = new SimpleInventory();
	@Nonnull public final Inventory out = _out.lockInsertions();
	private int remaining;
	private ItemEntry smeltingUnderway;
	private Smelting openWindow;
	
	private static boolean inited = false;
	public static void init() {
		if(inited) return;
		
		recipes.put(ContentsBlocks.logs, ContentsItems.coal);//Wood log => coal
		recipes.put(ContentsBlocks.iron_ore, ContentsItems.iron);
		recipes.put(ContentsBlocks.copper_ore, ContentsItems.copper);
		recipes.put(ContentsBlocks.silicon_ore, ContentsItems.silicon);
		recipes.put(ContentsBlocks.gold_ore, ContentsItems.gold);
		recipes.put(ContentsBlocks.silver_ore, ContentsItems.silver);
		inited = true;
	}
	
	/**
	 * @param x
	 * @param y
	 * @param owner2
	 */
	public Furnace(int x, int y, @Nonnull BlockMap owner2) {
		super(x, y, owner2);
	}

	@Override
	protected void save1(ObjectNode node) {
		node.set("in", in.save());
		node.set("out", _out.save());
		JsonNode smeltData = ItemEntry.saveItem(smeltingUnderway);
		node.set("smelt", smeltData);
		node.set("remain", new IntNode(remaining));
	}

	@Override
	protected void load1(ObjectNode node) {
		ArrayNode invin = JsonTool.requestArray("in", node);
		in.load(invin);
		ArrayNode invout = JsonTool.requestArray("out", node);
		_out.load(invout);
		JsonNode itemUnderWay = node.get("smelt");
		smeltingUnderway = ItemEntry.loadFromJson(itemUnderWay);
		JsonNode remainNode = node.get("remain");
		if(remainNode != null) remaining = remainNode.asInt();
	}

	@Override
	public Inventory getInventory(Side s) {
		if(s == side.U()) {
			return out;
		}
		if(s == side.D()) {
			return in;
		}
		return NoSuchInventory.INSTANCE;
	}

	@Override
	public BlockType type() {
		return ContentsBlocks.FURNACE;
	}

	@Override
	public RotatedImageGroup getImage() {
		if(smeltingUnderway == null) return IMAGE_INERT;
		return IMAGE_ACTIVE;
	}

	/**
	 * @return item, which is currently smelting
	 */
	public ItemEntry getSmeltingUnderway() {
		return smeltingUnderway;
	}

	/**
	 * Sets the current item which is smelting
	 * @param smeltingUnderway new item to smelt
	 */
	public void setSmeltingUnderway(ItemEntry smeltingUnderway) {
		this.smeltingUnderway = smeltingUnderway;
	}

	@Override
	public void onTick(MapProxy map) {
		if(remaining == 0) {
			//Time to take a new item
			for(ItemRecord ir: in) {
				if(ir.amount() == 0) continue;
				//Item exists
				if(recipes.containsKey(ir.item())) {
					//Item is smeltable, take it
					int extracted = ir.extract(1);
					if(extracted == 1) {
						//Extracted
						remaining = 100;
						smeltingUnderway = ir.item();
						if(openWindow != null) openWindow.invInput.refresh();
						return;
					}
				}//else item is not smeltable, do not take it
			}
		}else if(remaining < 0) {
			remaining = 0;
		}else{
			//Continue smelting
			remaining--;
			if(remaining == 0) {
				//Time to eject an item
				RecipeOutput result = recipes.get(smeltingUnderway);
				if(result == null) {
					//Unsmeltable, return original
					if(smeltingUnderway != null) 
						_out.insert(smeltingUnderway, 1);
					
				}else{
					//Smeltable, eject expected item
					result.produceResults(_out.createWriter());
				}
				smeltingUnderway = null;
				if(openWindow != null) openWindow.invOutput.refresh();
			}// else continue smelting
		}
		if(openWindow != null) openWindow.refresh();
	}

	/**
	 * @return the remaining progress of smelting this item
	 */
	public int getRemaining() {
		return remaining;
	}

	/**
	 * @param remaining new remaining progress
	 */
	public void setRemaining(int remaining) {
		this.remaining = remaining;
	}

	@Override
	public void click(int blockX, int blockY, World map, WorldWindow window) {
		if(window == null) return;
		if(openWindow != null) return;
		openWindow = new Smelting(this, window);
		window.openAndShowWindow(openWindow, "Furnace");
	}
	
	public void closeWindow() {
		Throwable checker = new Throwable();
		StackTraceElement[] trace = checker.getStackTrace();
		String expectedName = Smelting.class.getName();
		for(StackTraceElement ste: trace) {
			if(ste.getClassName().equals(expectedName)) {
				openWindow = null;
				return;
			}
		}
		throw new IllegalArgumentException("Only for Smelting GUI.");
	}
		
}

