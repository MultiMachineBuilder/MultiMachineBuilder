/**
 * 
 */
package mmb.WORLD.blocks.machine;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.BEANS.BlockActivateListener;
import mmb.BEANS.ToItemUnifiedCollector;
import mmb.WORLD.block.BlockEntityData;
import mmb.WORLD.block.BlockEntry;
import mmb.WORLD.block.BlockType;
import mmb.WORLD.chance.Chance;
import mmb.WORLD.contentgen.ElectricMachineGroup.ElectroMachineType;
import mmb.WORLD.electric.Battery;
import mmb.WORLD.electric.Electricity;
import mmb.WORLD.gui.window.GUITab;
import mmb.WORLD.gui.window.WorldWindow;
import mmb.WORLD.inventory.Inventory;
import mmb.WORLD.inventory.io.InventoryWriter;
import mmb.WORLD.inventory.storage.SimpleInventory;
import mmb.WORLD.rotate.Side;
import mmb.WORLD.worlds.MapProxy;
import mmb.WORLD.worlds.world.World;

/**
 * @author oskar
 *
 */
public class Digger extends BlockEntityData implements ToItemUnifiedCollector, BlockActivateListener {

	@Override
	public BlockType type() {
		return type;
	}

	@Override
	public BlockEntry blockCopy() {
		Digger result = new Digger(type);
		result.battery.set(battery);
		result.inv0.set(inv);
		result.rangeX = rangeX;
		result.rangeY = rangeY;
		result.scanX = scanX;
		result.scanY = scanY;
		result.active = active;
		return result;
	}

	@Override
	public void load(@Nullable JsonNode data) {
		if(data == null)  return;
		JsonNode noderx = data.get("rangeX");
		if(noderx != null) rangeX = noderx.asInt();
		JsonNode nodery = data.get("rangeY");
		if(noderx != null) rangeY = nodery.asInt();
		JsonNode nodesx = data.get("scanX");
		if(noderx != null) scanX = nodesx.asInt();
		JsonNode nodesy = data.get("scanY");
		if(noderx != null) scanY = nodesy.asInt();
		JsonNode act = data.get("active");
		if(act != null) active = act.asBoolean();
		JsonNode ninv = data.get("inv");
		inv0.load(ninv);
		JsonNode nbat = data.get("energy");
		battery.load(nbat);
	}

	@Override
	protected void save0(ObjectNode node) {
		node.put("rangeX", rangeX);
		node.put("rangeY", rangeY);
		node.put("scanX", scanX);
		node.put("scanY", scanY);
		node.put("active", active);
		node.set("inv", inv0.save());
		node.set("energy", battery.save());
	}

	
	private int rangeX = 4;
	private int rangeY = 4;
	private int scanX;
	private int scanY;
	private boolean active;
	@Nonnull private final SimpleInventory inv0;
	@Nonnull public final Inventory inv;
	private final int range;
	@Nonnull private final ElectroMachineType type;
	@Nonnull public final Battery battery;
	
	public Digger(ElectroMachineType type) {
		this.type = type;
		battery = new Battery(type.volt.speedMul*2/type.volt.volts, 5000, this, type.volt);
		range = (type.volt.ordinal()+1)*32;
		inv0 = new SimpleInventory();
		inv0.setCapacity(range*(double)range);
		inv = inv0.lockInsertions();
	}

	@Override
	public void setRangeX(int range) {
		rangeX = range;
	}

	@Override
	public int getRangeX() {
		return rangeX;
	}

	@Override
	public void setRangeY(int range) {
		rangeY = range;
	}
	/**
	 * @return is this digger active?
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @param active should the digger be active?s
	 */
	public void setActive(boolean active) {
		this.active = active;
	}
	

	@Override
	public int getRangeY() {
		return rangeY;
	}

	@Override
	public int maxSize() {
		return range;
	}
	public static final double ENERGY_PER_BLOCK = 5000;

	@Override
	public void onTick(MapProxy map) {
		if(gui != null) gui.refreshEnergy();
		if(!active) {
			scanX = 0;
			scanY = 0;
			return;
		}
		int iters = (int)(battery.energy()/ENERGY_PER_BLOCK);
		InventoryWriter writer0 = inv0.createWriter();
		for(int i = 0; i < iters; i++) {
			//Check battery
			if(battery.energy() < ENERGY_PER_BLOCK) return;
			
			//Traverse
			scanX++;
			if(scanX >= rangeX) {
				scanX = 0;
				scanY++;
			}
			if(scanY >= rangeY) {
				active = false;
				return;
			}
			
			int ox = scanX + posX();
			int oy = scanY + posY();
			
			//Check the block
			BlockEntry block = map.get(ox, oy);
			if(block.isSurface()) continue;
			
			//Extract energy
			battery.extract(ENERGY_PER_BLOCK/battery.voltage.volts, type.volt, this::blow);
			
			//Mine the block
			Chance drop = block.type().getDrop();
			owner().place(block.type().leaveBehind(), ox, oy);
			drop.drop(writer0, owner(), ox, oy);
		}
	}

	@Override
	public Inventory inv() {
		return inv;
	}

	@Override
	public Inventory getInventory(Side s) {
		return inv;
	}
	
	DiggerGUI gui;
	@Override
	public void click(int blockX, int blockY, World map, @Nullable WorldWindow window, double partX, double partY) {
		if(window == null) return;
		if(gui != null) return;
		gui = new DiggerGUI(this, window);
		window.openAndShowWindow(gui, "Item collector");
	}
	@Override
	public void destroyTab(GUITab filterGUI) {
		if(filterGUI != gui) throw new IllegalStateException("Wrong GUI");
		gui = null;
	}
	@Override
	public Electricity getElectricalConnection(Side s) {
		return battery;
	}

	

}
