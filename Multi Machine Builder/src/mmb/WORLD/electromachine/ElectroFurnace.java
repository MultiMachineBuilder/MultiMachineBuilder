/**
 * 
 */
package mmb.WORLD.electromachine;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.BEANS.BlockActivateListener;
import mmb.WORLD.block.SkeletalBlockEntityRotary;
import mmb.WORLD.blocks.machine.line.FurnaceGUI;
import mmb.WORLD.contentgen.ElectricMachineGroup.ElectroMachineType;
import mmb.WORLD.crafting.Craftings;
import mmb.WORLD.crafting.ElectroItemProcessHelper;
import mmb.WORLD.electric.Battery;
import mmb.WORLD.electric.Electricity;
import mmb.WORLD.electric.VoltageTier;
import mmb.WORLD.gui.window.WorldWindow;
import mmb.WORLD.inventory.Inventory;
import mmb.WORLD.inventory.io.InventoryReader;
import mmb.WORLD.inventory.io.InventoryWriter;
import mmb.WORLD.inventory.storage.SimpleInventory;
import mmb.WORLD.rotate.RotatedImageGroup;
import mmb.WORLD.rotate.Side;
import mmb.WORLD.worlds.MapProxy;
import mmb.WORLD.worlds.world.World;
import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class ElectroFurnace extends SkeletalBlockEntityRotary implements BlockActivateListener{
	@Override
	public ElectroMachineType type() {
		return type;
	}

	@Override
	public RotatedImageGroup getImage() {
		return type.rig;
	}

	//Containers
	@Nonnull public final SimpleInventory in = new SimpleInventory();
	@Nonnull private final SimpleInventory out0 = new SimpleInventory();
	@Nonnull public final Inventory out = out0.lockInsertions();
	@Nonnull final Battery elec;
	@Nonnull private final ElectroItemProcessHelper helper;
	@Nonnull private final ElectroMachineType type;
	
	//Constructor
	public ElectroFurnace(ElectroMachineType type) {
		elec = new Battery(20_000, 40_000, this, type.volt);
		helper = new ElectroItemProcessHelper(Craftings.smelting, in, out0, 1000, elec, type.volt);
		this.type = type;
	}
	
	//Block I/O
	@Override
	public InventoryReader getOutput(Side s) {
		return out0.createReader();
	}
	@Override
	public InventoryWriter getInput(Side s) {
		return in.createWriter();
	}

	@Override
	public Electricity getElectricalConnection(Side s) {
		return Electricity.insertOnly(elec);
	}

	//Save/load
	@Override
	protected void save1(ObjectNode node) {
		helper.save(node);
		JsonNode bat = elec.save();
		node.set("energy", bat);
		node.set("in", in.save());
		node.set("out", out0.save());
		super.save1(node);
	}

	@Override
	protected void load1(ObjectNode node) {
		helper.load(node);
		JsonNode bat = node.get("energy");
		elec.load(bat);
		in.load(node.get("in"));
		in.setCapacity(2);
		out0.load(node.get("in"));
		out0.setCapacity(2);
	}

	
	ElectroFurnaceTab tab;
	//GUI
	@Override
	public void click(int blockX, int blockY, World map, @Nullable WorldWindow window, double partX, double partY) {
		if(window == null) return;
		if(tab != null) return;
		tab = new ElectroFurnaceTab(this, window);
		window.openAndShowWindow(tab, "Electro-Furnace "+type.volt.name);
		helper.refreshable = tab;
		tab.refreshProgress(0, null);
	}

	@Nonnull private static final Debugger debug = new Debugger("ELECTRIC FURNACE");
	@Override
	public void onTick(MapProxy map) {
		helper.cycle();
		Electricity.equatePPs(this, map, elec, 0.9);
		//elec.pressure = 0.9*elec.pressure; //Vent power pressure to get electricity
		debug.printl("Energy: "+elec.amt+", Power pressure: "+elec.pressure+" at ["+posX()+","+posY()+"]");
	}
}
