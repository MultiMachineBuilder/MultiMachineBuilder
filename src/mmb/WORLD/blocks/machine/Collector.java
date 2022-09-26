/**
 * 
 */
package mmb.WORLD.blocks.machine;

import java.util.Collection;
import java.util.Iterator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.BEANS.BlockActivateListener;
import mmb.BEANS.Resizable;
import mmb.BEANS.ToItemUnifiedCollector;
import mmb.DATA.json.JsonTool;
import mmb.WORLD.block.BlockEntity;
import mmb.WORLD.block.BlockEntityData;
import mmb.WORLD.block.BlockEntry;
import mmb.WORLD.block.BlockType;
import mmb.WORLD.blocks.ContentsBlocks;
import mmb.WORLD.gui.window.GUITab;
import mmb.WORLD.gui.window.WorldWindow;
import mmb.WORLD.inventory.Inventory;
import mmb.WORLD.inventory.storage.SimpleInventory;
import mmb.WORLD.items.ItemEntry;
import mmb.WORLD.rotate.Side;
import mmb.WORLD.worlds.MapProxy;
import mmb.WORLD.worlds.world.World;

/**
 * @author oskar
 *
 */
public class Collector extends BlockEntityData implements BlockActivateListener, ToItemUnifiedCollector {
	/**
	 * The inventory, in which collected items go
	 */
	@Nonnull private SimpleInventory inv0 = new SimpleInventory();
	@Nonnull private Inventory inv = inv0.lockInsertions();
	@Override
	public Inventory inv() {
		return inv;
	}
	
	private int rangeX = 4;
	private int rangeY = 4;
	private int period = 10;
	private int timer = 0;
	@Override
	public BlockType type() {
		return ContentsBlocks.COLLECTOR;
	}

	@Override
	public void load(@Nullable JsonNode data) {
		if(data == null) return;
		inv0.load(data.get("inv"));
		rangeX = data.get("rangeX").asInt(4);
		rangeY = data.get("rangeY").asInt(4);
		period = JsonTool.getInt(data, "period", 10);
		timer = JsonTool.getInt(data, "timer", 0);
	}

	@Override
	protected void save0(ObjectNode node) {
		node.set("inv", inv0.save());
		node.put("rangeX", rangeX);
		node.put("rangeY", rangeY);
		node.put("period", period);
		node.put("timer", timer);
	}

	/**
	 * @return the rangeX
	 */
	public int getRangeX() {
		return rangeX;
	}

	/**
	 * @param rangeX the rangeX to set
	 */
	public void setRangeX(int rangeX) {
		this.rangeX = clamp(1, rangeX, 16);
	}

	/**
	 * @return the rangeY
	 */
	public int getRangeY() {
		return rangeY;
	}

	/**
	 * @param rangeY the rangeY to set
	 */
	public void setRangeY(int rangeY) {
		this.rangeY = clamp(1, rangeY, 16);
	}
	
	/** @return current collection period*/
	public int getPeriod() {
		return period;
	}
	/** @param period new collection period. Clamped above 0*/
	public void setPeriod(int period) {
		this.period = clamp(1, period, Integer.MAX_VALUE);
	}

	@Override
	public void onTick(MapProxy map) {
		timer++;
		if(timer < period) return;
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

	public static int clamp(int min, int value, int max) {
		if(value < min) return min;
		if(value > max) return max;
		return value;
	}

	CollectorGUI gui;
	@Override
	public void click(int blockX, int blockY, World map, @Nullable WorldWindow window, double partX, double partY) {
		if(window == null) return;
		if(gui != null) return;
		gui = new CollectorGUI(this, window);
		window.openAndShowWindow(gui, "Item collector");
	}

	@Override
	public Inventory getInventory(Side s) {
		return inv;
	}

	@Override
	public BlockEntry blockCopy() {
		Collector copy = new Collector();
		copy.inv0.set(inv);
		copy.rangeX = rangeX;
		copy.rangeY = rangeY;
		return copy;
	}

	@Override
	public int maxSize() {
		return 16;
	}

	@Override
	public void destroyTab(GUITab filterGUI) {
		if(filterGUI != gui) throw new IllegalStateException("Wrong GUI");
		gui = null;
	}

	
}
