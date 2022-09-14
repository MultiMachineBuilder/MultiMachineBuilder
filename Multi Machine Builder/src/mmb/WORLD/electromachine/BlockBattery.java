/**
 * 
 */
package mmb.WORLD.electromachine;

import java.awt.Graphics;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.BEANS.BlockActivateListener;
import mmb.BEANS.Electric;
import mmb.WORLD.block.SkeletalBlockEntityRotary;
import mmb.WORLD.contentgen.ElectricMachineGroup.ElectroMachineType;
import mmb.WORLD.electric.Battery;
import mmb.WORLD.electric.Electricity;
import mmb.WORLD.gui.window.WorldWindow;
import mmb.WORLD.inventory.storage.SingleItemInventory;
import mmb.WORLD.items.ItemEntry;
import mmb.WORLD.items.pickaxe.Pickaxe;
import mmb.WORLD.rotate.RotatedImageGroup;
import mmb.WORLD.rotate.Side;
import mmb.WORLD.worlds.MapProxy;
import mmb.WORLD.worlds.world.World;

/**
 * @author oskar
 *
 */
public final class BlockBattery extends SkeletalBlockEntityRotary implements BlockActivateListener, Electric {

	@Nonnull private final ElectroMachineType type;
	@Nonnull public final Battery battery;
	@Nonnull public final SingleItemInventory charger = new SingleItemInventory();
	@Nonnull public final SingleItemInventory discharger = new SingleItemInventory();
	BatteryTab tab;
	
	public BlockBattery(ElectroMachineType type) {
		this.type = type;
		this.battery = new Battery(type.powermul * 1000, type.powermul * 400_000, this, type.volt);
	}
	
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
		Pickaxe.renderDurability(battery.amt/battery.capacity, g, x, y, ss, ss);
	}

	
	@Override
	public Electricity getElectricalConnection(Side s) {
		return Electricity.insertOnly(battery);
	}
	@Override
	public Electricity getElectricity() {
		return battery;
	}

	@Override
	public void click(int blockX, int blockY, World map, @Nullable WorldWindow window, double partX, double partY) {
		if(window == null) return;
		if(tab != null) return;
		tab = new BatteryTab(this, window);
		window.openAndShowWindow(tab, type().title());
		tab.refresh();
	}

}
