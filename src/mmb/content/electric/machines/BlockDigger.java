/**
 * 
 */
package mmb.content.electric.machines;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.NN;
import mmb.Nil;
import mmb.beans.BlockActivateListener;
import mmb.content.electric.Battery;
import mmb.content.electric.Electric;
import mmb.content.electric.Electricity;
import mmb.content.electric.ElectricMachineGroup.ElectroMachineType;
import mmb.content.imachine.extractor.ToItemUnifiedCollector;
import mmb.engine.block.BlockEntityData;
import mmb.engine.block.BlockEntry;
import mmb.engine.block.BlockType;
import mmb.engine.chance.Chance;
import mmb.engine.inv.Inventory;
import mmb.engine.inv.io.InventoryWriter;
import mmb.engine.inv.storage.SimpleInventory;
import mmb.engine.rotate.Side;
import mmb.engine.worlds.MapProxy;
import mmb.engine.worlds.world.World;
import mmb.menu.world.window.GUITab;
import mmb.menu.world.window.WorldWindow;

/**
 * Digs blocks out using electricity
 * @author oskar
 */
public class BlockDigger extends BlockEntityData implements ToItemUnifiedCollector, BlockActivateListener, Electric {
	//Constructors
	public BlockDigger(ElectroMachineType type) {
		this.type = type;
		battery = new Battery(0.2, 500, this, type.volt);
		hammer = new Battery(Double.POSITIVE_INFINITY, ENERGY_PER_BLOCK/type.volt.volts, this, type.volt);
		range = (type.volt.ordinal()+1)*32;
		inv0 = new SimpleInventory();
		inv0.setCapacity(range*(double)range);
		inv = inv0.lockInsertions();
	}
	
	//Electricity
	/** Stores incoming electrical energy */
	@NN public final Battery battery;
	/** Stores energy to mine the next block */
	@NN public final Battery hammer;
	@Override
	public Electricity getElectricalConnection(Side s) {
		return battery;
	}
	@Override
	public Electricity getElectricity() {
		return battery;
	}
	
	//Inventory
	@NN private final SimpleInventory inv0;
	@NN public final Inventory inv;
	
	//Range
	private int rangeX = 4;
	private int rangeY = 4;
	private final int range;
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
	@Override
	public int getRangeY() {
		return rangeY;
	}
	@Override
	public int maxSize() {
		return range;
	}
	
	//Scan
	private int scanX;
	private int scanY;
	
	//Control
	private boolean active;
	/** @return is this digger active? */
	public boolean isActive() {
		return active;
	}
	/** @param active should the digger be active? */
	public void setActive(boolean active) {
		this.active = active;
	}
	
	//Block methods
	@NN private final ElectroMachineType type;
	@Override
	public BlockType type() {
		return type;
	}
	@Override
	public BlockEntry blockCopy() {
		BlockDigger result = new BlockDigger(type);
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
	public Inventory inv() {
		return inv;
	}
	@Override
	public Inventory getInventory(Side s) {
		return inv;
	}
	
	//Block logic
	/** Energy required to mine a block */
	public static final double ENERGY_PER_BLOCK = 2000;
	@Override
	public void onTick(MapProxy map) {
		if(gui != null) gui.refreshEnergy();
		if(!active) {
			scanX = 0;
			scanY = 0;
			return;
		}
		battery.extractTo(hammer);
		double reqcharge = ENERGY_PER_BLOCK/battery.voltage.volts;
		InventoryWriter writer0 = inv0.createWriter();
		while(hammer.stored >= reqcharge) {
			//Check battery
			if(hammer.energy() < ENERGY_PER_BLOCK) return;
			
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
			hammer.stored -= reqcharge;
			
			//Mine the block
			Chance drop = block.type().getDrop();
			owner().place(block.type().leaveBehind(), ox, oy);
			drop.drop(writer0, owner(), ox, oy);
		}
		Electricity.equatePPs(this, map, battery, 0.9, type.powermul * type.volt.volts);
	}	
	
	//GUI
	GUIDigger gui;
	@Override
	public void click(int blockX, int blockY, World map, @Nil WorldWindow window, double partX, double partY) {
		if(window == null) return;
		if(gui != null) return;
		gui = new GUIDigger(this, window);
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
}
