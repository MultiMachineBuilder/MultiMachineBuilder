/**
 * 
 */
package mmb.content.imachine.extractor;

import java.util.Collection;
import java.util.Iterator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.NN;
import mmb.Nil;
import mmb.cgui.BlockActivateListener;
import mmb.content.ContentsBlocks;
import mmb.data.variables.ListenableInt;
import mmb.engine.block.BlockEntityData;
import mmb.engine.block.BlockEntry;
import mmb.engine.block.BlockType;
import mmb.engine.inv.Inventory;
import mmb.engine.inv.storage.SimpleInventory;
import mmb.engine.item.ItemEntry;
import mmb.engine.json.JsonTool;
import mmb.engine.rotate.Side;
import mmb.engine.worlds.MapProxy;
import mmb.engine.worlds.world.World;
import mmb.menu.world.window.GUITab;
import mmb.menu.world.window.WorldWindow;

/**
 * Collects dropped items in range
 * @author oskar
 */
public class BlockCollector extends BlockEntityData implements BlockActivateListener, ToItemUnifiedCollector {
	//Constructors
	/** Creates a dropped item collector */
	public BlockCollector() {
		period.add(value -> {
			if(value < 1) period.set(value);
		});
	}
	
	//Timing
	private int timer = 0;
	/** Time between collections in ticks */
	@NN public final ListenableInt period = new ListenableInt(10);
	
	//Inventory
	/** The inventory, in which collected items go */
	@NN private SimpleInventory inv0 = new SimpleInventory();
	@NN private Inventory inv = inv0.lockInsertions();
	@Override
	public Inventory inv() {
		return inv;
	}
	
	//Range
	private int rangeX = 4;
	private int rangeY = 4;
	/**
	 * @return the rangeX
	 */
	@Override
	public int getRangeX() {
		return rangeX;
	}
	/**
	 * @param rangeX the rangeX to set
	 */
	@Override
	public void setRangeX(int rangeX) {
		this.rangeX = clamp(1, rangeX, 16);
	}
	/**
	 * @return the rangeY
	 */
	@Override
	public int getRangeY() {
		return rangeY;
	}
	/**
	 * @param rangeY the rangeY to set
	 */
	@Override
	public void setRangeY(int rangeY) {
		this.rangeY = clamp(1, rangeY, 16);
	}
	@Override
	public int maxSize() {
		return 16;
	}
	
	//Block methods
	@Override
	public Inventory getInventory(Side s) {
		return inv;
	}
	@Override
	public BlockType type() {
		return ContentsBlocks.COLLECTOR;
	}
	@Override
	public BlockEntry blockCopy() {
		BlockCollector copy = new BlockCollector();
		copy.inv0.set(inv);
		copy.rangeX = rangeX;
		copy.rangeY = rangeY;
		return copy;
	}
	@Override
	public void onTick(MapProxy map) {
		timer++;
		if(timer < period.getInt()) return;
		timer = 0;
		for(int i = 0, x = posX(); i < rangeX; i++, x++) {
			for(int j = 0, y = posY(); j < rangeY; j++, y++) {
				Collection<ItemEntry> collect = owner().getDrops(x, y);
				Iterator<ItemEntry> iterator = collect.iterator();
				while(iterator.hasNext()) {
					ItemEntry item = iterator.next();
					if(item == null) {
						iterator.remove();
						continue;
					}
					int tf = inv0.insert(item, 1);
					if(tf == 1) {
						iterator.remove();
					}
				}
			}
		}
	}
	@Nil BlockCollectorGUI gui;
	@Override
	public void click(int blockX, int blockY, World map, @Nil WorldWindow window, double partX, double partY) {
		if(window == null) return;
		if(gui != null) return;
		gui = new BlockCollectorGUI(this, window);
		window.openAndShowWindow(gui, "Item collector");
	}
	@Override
	public void destroyTab(GUITab filterGUI) {
		if(filterGUI != gui) throw new IllegalStateException("Wrong GUI");
		gui = null;
	}
	
	//Serialization
	@Override
	public void load(@Nil JsonNode data) {
		if(data == null) return;
		inv0.load(data.get("inv"));
		rangeX = data.get("rangeX").asInt(4);
		rangeY = data.get("rangeY").asInt(4);
		int period0 = JsonTool.getInt(data, "period", 10);
		period.set(period0);
		timer = JsonTool.getInt(data, "timer", 0);
	}
	@Override
	protected void save0(ObjectNode node) {
		node.set("inv", inv0.save());
		node.put("rangeX", rangeX);
		node.put("rangeY", rangeY);
		node.put("period", period.getInt());
		node.put("timer", timer);
	}
	
	//Utils
	/**
	 * Clamps a value to the range
	 * @param min minimmum value
	 * @param value value to be constrained
	 * @param max maximum value
	 * @return constrained value
	 */
	public static int clamp(int min, int value, int max) {
		if(value < min) return min;
		if(value > max) return max;
		return value;
	}
}
