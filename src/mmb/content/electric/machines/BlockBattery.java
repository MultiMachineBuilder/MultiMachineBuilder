/**
 * 
 */
package mmb.content.electric.machines;

import java.awt.Graphics;

import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.NN;
import mmb.Nil;
import mmb.cgui.BlockActivateListener;
import mmb.content.electric.Battery;
import mmb.content.electric.Electric;
import mmb.content.electric.Electricity;
import mmb.content.electric.ElectricMachineGroup.ElectroMachineType;
import mmb.content.pickaxe.Pickaxe;
import mmb.engine.block.BlockEntityRotary;
import mmb.engine.inv.storage.SingleItemInventory;
import mmb.engine.item.ItemEntry;
import mmb.engine.rotate.RotatedImageGroup;
import mmb.engine.rotate.Side;
import mmb.engine.worlds.MapProxy;
import mmb.engine.worlds.world.World;
import mmb.menu.world.window.WorldWindow;

/**
 * Stores electricity
 * @author oskar
 */
public final class BlockBattery extends BlockEntityRotary implements BlockActivateListener, Electric {
	//Block definition
	@NN private final ElectroMachineType type;
	/** The energy storage for this block */
	@NN public final Battery battery;
	/** The charging slot for this block */
	@NN public final SingleItemInventory charger = new SingleItemInventory();
	/** The discharging slot for this block */
	@NN public final SingleItemInventory discharger = new SingleItemInventory();
	
	/**
	 * Creates a new battery block
	 * @param type this battery's type
	 */
	public BlockBattery(ElectroMachineType type) {
		this.type = type;
		this.battery = new Battery(type.powermul * 100, type.powermul * 4000, this, type.volt);
	}
	
	//GUI
	BatteryTab tab;
	@SuppressWarnings("javadoc") //internal use only
	public void close(BatteryTab tbb) {
		if(tab == tbb) tab = null;
	}
	@Override
	public void click(int blockX, int blockY, World map, @Nil WorldWindow window, double partX, double partY) {
		if(window == null) return;
		if(tab != null) return;
		tab = new BatteryTab(this, window);
		window.openAndShowWindow(tab, type().title());
		tab.refresh();
	}
	
	//Block attributes
	@Override
	public ElectroMachineType type() {
		return type;
	}
	@Override
	public BlockBattery blockCopy() {
		BlockBattery copy = new BlockBattery(type);
		battery.set(copy.battery);
		return copy;
	}
	@Override
	public RotatedImageGroup getImage() {
		return type.rig;
	}

	@Override
	public void onTick(MapProxy map) {
		Electricity.equatePPs(this, map, battery, 0.99, 0);
		
		//Update the progress bar
		if(tab != null) tab.refresh();
		
		//Extract the electricity
		Electricity elec = getAtSide(getRotation().R()).getElectricalConnection(getRotation().L());
		if(elec != null) battery.extractTo(elec);
		
		//Charge the item
		ItemEntry itemCharge = charger.getContents();
		if(itemCharge instanceof Electric) {
			Electricity elec1 = ((Electric) itemCharge).getElectricity();
			battery.extractTo(elec1);
		}
		
		//Discharge the item
		ItemEntry itemDischarge = discharger.getContents();
		if(itemDischarge instanceof Electric) {
			Electricity elec1 = ((Electric) itemDischarge).getElectricity();
			battery.takeFrom(elec1);
		}
	}
	@Override
	public void render(int x, int y, Graphics g, int ss) {
		super.render(x, y, g, ss);
		Pickaxe.renderDurability(battery.stored/battery.capacity, g, x, y, ss, ss);
	}

	//Electricity
	@Override
	public Electricity getElectricalConnection(Side s) {
		return Electricity.insertOnly(battery);
	}
	@Override
	public Electricity getElectricity() {
		return battery;
	}

	//Serialization
	@Override
	protected void save1(ObjectNode node) {
		node.set("charge", ItemEntry.saveItem(charger.getContents()));
		node.set("discharge", ItemEntry.saveItem(charger.getContents()));
		node.set("battery", battery.save());
	}
	@Override
	protected void load1(ObjectNode node) {
		charger.setContents(ItemEntry.loadFromJson(node.get("charge")));
		discharger.setContents(ItemEntry.loadFromJson(node.get("discharge")));
		battery.load(node.get("battery"));
	}
}
