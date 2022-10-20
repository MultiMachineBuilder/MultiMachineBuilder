/**
 * 
 */
package mmb.world.electromachine;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.cgui.BlockActivateListener;
import mmb.menu.world.machine.GUIMachine;
import mmb.menu.world.window.WorldWindow;
import mmb.world.contentgen.ElectricMachineGroup.ElectroMachineType;
import mmb.world.crafting.helper.ComplexItemProcessHelper;
import mmb.world.electric.Electricity;
import mmb.world.inventory.io.InventoryReader;
import mmb.world.inventory.io.InventoryWriter;
import mmb.world.recipes.ComplexRecipeGroup;
import mmb.world.rotate.RotatedImageGroup;
import mmb.world.rotate.Side;
import mmb.world.worlds.MapProxy;
import mmb.world.worlds.world.World;

/**
 * @author oskar
 *
 */
public class BlockProcessorComplex extends BlockProcessorAbstract implements BlockActivateListener{
	@Override
	public RotatedImageGroup getImage() {
		return type.rig;
	}

	//Containers
	@Nonnull private final ComplexItemProcessHelper helper;
	@Nonnull public final ComplexRecipeGroup group;
	
	//Constructor
	/**
	 * Creates an alloy smelter
	 * @param type
	 * @param group
	 */
	public BlockProcessorComplex(ElectroMachineType type, ComplexRecipeGroup group) {
		super(type);
		this.group = group;
		helper = new ComplexItemProcessHelper(group, in, out0, 1000, elec, type.volt);
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

	//GUI
	@Override
	public void click(int blockX, int blockY, World map, @Nullable WorldWindow window, double partX, double partY) {
		if(window == null) return;
		if(tab != null) return;
		tab = new GUIMachine(this, window);
		window.openAndShowWindow(tab, group.title()+' '+type.volt.name);
		helper.refreshable = tab;
		tab.refreshProgress(0, null);
	}
	
	@Override protected void onTick0(MapProxy map) {
		helper.cycle();
	}

	@Override
	public ComplexRecipeGroup recipes() {
		return group;
	}

	@Override
	protected BlockProcessorAbstract copy0() {
		BlockProcessorComplex copy = new BlockProcessorComplex(type, group);
		copy.helper.set(helper);
		return copy;
	}

	@Override
	public String machineName() {
		return group.title();
	}

}
