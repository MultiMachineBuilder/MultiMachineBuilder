/**
 * 
 */
package mmb.world.electromachine;

import java.awt.Graphics;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.beans.Electric;
import mmb.cgui.BlockActivateListener;
import mmb.menu.world.machine.BatteryTab;
import mmb.menu.world.window.WorldWindow;
import mmb.world.blocks.SkeletalBlockEntityRotary;
import mmb.world.contentgen.ElectricMachineGroup.ElectroMachineType;
import mmb.world.electric.Battery;
import mmb.world.electric.Electricity;
import mmb.world.inventory.storage.SingleItemInventory;
import mmb.world.item.ItemEntry;
import mmb.world.items.pickaxe.Pickaxe;
import mmb.world.rotate.RotatedImageGroup;
import mmb.world.rotate.Side;
import mmb.world.worlds.MapProxy;
import mmb.world.worlds.world.World;

/**
 * @author oskar
 *
 */
public final class BlockBattery extends SkeletalBlockEntityRotary implements BlockActivateListener, Electric {
	//Block definition
	@Nonnull private final ElectroMachineType type;
	/** The energy storage for this block */
	@Nonnull public final Battery battery;
	/** The charging slot for this block */
	@Nonnull public final SingleItemInventory charger = new SingleItemInventory();
	/** The discharging slot for this block */
	@Nonnull public final SingleItemInventory discharger = new SingleItemInventory();
	
	/**
	 * Creates a new battery block
	 * @param type
	 */
	public BlockBattery(ElectroMachineType type) {
		this.type = type;
		this.battery = new Battery(type.powermul * 1000, type.powermul * 400_000, this, type.volt);
	}
	
	//GUI
	BatteryTab tab;
	@SuppressWarnings("javadoc") //internal use only
	public void close(BatteryTab tbb) {
		if(tab == tbb) tab = null;
	}
	@Override
	public void click(int blockX, int blockY, World map, @Nullable WorldWindow window, double partX, double partY) {
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
		Electricity.equatePPs(this, map, battery, 0.99);
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
