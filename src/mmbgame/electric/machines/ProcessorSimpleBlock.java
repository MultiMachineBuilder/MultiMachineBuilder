/**
 * 
 */
package mmbgame.electric.machines;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.cgui.BlockActivateListener;
import mmb.menu.world.window.WorldWindow;
import mmbeng.craft.singles.SimpleRecipeGroup;
import mmbeng.inv.io.InventoryReader;
import mmbeng.inv.io.InventoryWriter;
import mmbeng.rotate.RotatedImageGroup;
import mmbeng.rotate.Side;
import mmbeng.worlds.MapProxy;
import mmbeng.worlds.world.World;
import mmbgame.electric.Electricity;
import mmbgame.electric.GUIMachine;
import mmbgame.electric.ElectricMachineGroup.ElectroMachineType;
import mmbgame.electric.helper.SimpleProcessHelper;

/**
 * @author oskar
 * A machine capable of obtaining resources out of nothing
 */
public class ProcessorSimpleBlock extends ProcessorAbstractBlock implements BlockActivateListener{
	@Override
	protected ProcessorAbstractBlock copy0() {
		return new ProcessorSimpleBlock(type, recipes);
	}
	
	@Override
	public RotatedImageGroup getImage() {
		return type.rig;
	}

	//Containers
	@Nonnull private final SimpleProcessHelper helper;
	
	//Constructor
	public ProcessorSimpleBlock(ElectroMachineType type, SimpleRecipeGroup<?> group) {
		super(type);
		this.recipes = group;
		helper = new SimpleProcessHelper(group, in, out0, 1000, elec, null, type.volt);
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
		node.put("pass", pass);
		node.put("autoex", autoExtract);
	}

	@Override
	protected void load1(ObjectNode node) {
		helper.load(node);
		JsonNode bat = node.get("energy");
		elec.load(bat);
		in.load(node.get("in"));
		in.setCapacity(2);
		out0.load(node.get("out"));
		out0.setCapacity(2);
		JsonNode passNode = node.get("pass");
		if(passNode != null) pass = passNode.asBoolean();
		JsonNode autoNode = node.get("autoex");
		if(autoNode != null) autoExtract = autoNode.asBoolean();
	}

	
	//Crafting
	/** The recipe group used for this machine */
	public final SimpleRecipeGroup<?> recipes;
	@Override
	public String machineName() {
		return "Quarry";
	}

	@Override
	public SimpleRecipeGroup<?> recipes() {
		return recipes;
	}
	
	
	//GUI
	@Override
	public void click(int blockX, int blockY, World map, @Nullable WorldWindow window, double partX, double partY) {
		if(window == null) return;
		if(tab != null) return;
		tab = new GUIMachine(this, window);
		window.openAndShowWindow(tab, recipes.title()+' '+type.volt.name);
		helper.refreshable = tab;
		tab.refreshProgress(0, null);
	}

	@Override protected void onTick0(MapProxy map) {
		helper.cycle();
	}
}