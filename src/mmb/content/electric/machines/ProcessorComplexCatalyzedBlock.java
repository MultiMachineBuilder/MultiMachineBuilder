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
import mmbeng.craft.rgroups.ComplexCatalyzedRecipeGroup;
import mmbeng.inv.io.InventoryReader;
import mmbeng.inv.io.InventoryWriter;
import mmbeng.inv.storage.SingleItemInventory;
import mmbeng.item.ItemEntry;
import mmbeng.rotate.RotatedImageGroup;
import mmbeng.rotate.Side;
import mmbeng.worlds.MapProxy;
import mmbeng.worlds.world.World;
import mmbgame.electric.Electricity;
import mmbgame.electric.GUIMachine;
import mmbgame.electric.ElectricMachineGroup.ElectroMachineType;
import mmbgame.electric.helper.ComplexCatalyzedItemProcessHelper;

/**
 * @author oskar
 *
 */
public class ProcessorComplexCatalyzedBlock extends ProcessorAbstractBlock implements BlockActivateListener{
	@Override
	public RotatedImageGroup getImage() {
		return type.rig;
	}

	@Nonnull private final ComplexCatalyzedItemProcessHelper helper;
	@Nonnull public final ComplexCatalyzedRecipeGroup group;
	/**
	 * The current catalyst
	 */
	@Nonnull public final SingleItemInventory catalyst = new SingleItemInventory();
	
	//Constructor
	public ProcessorComplexCatalyzedBlock(ElectroMachineType type, ComplexCatalyzedRecipeGroup group) {
		super(type);
		this.recipes = group;
		helper = new ComplexCatalyzedItemProcessHelper(group, in, out0, 1000, elec, type.volt, () -> catalyst.getContents());
		this.group = group;
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
		node.set("catalyst", ItemEntry.saveItem(catalyst.getContents()));
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
		catalyst.setContents(ItemEntry.loadFromJson(node.get("catalyst")));
	}

	public final ComplexCatalyzedRecipeGroup recipes;
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
	public ComplexCatalyzedRecipeGroup recipes() {
		return group;
	}

	@Override
	protected ProcessorAbstractBlock copy0() {
		ProcessorComplexCatalyzedBlock copy = new ProcessorComplexCatalyzedBlock(type, group);
		copy.catalyst.setContents(catalyst.getContents());
		copy.helper.set(helper);
		return copy;
	}

	@Override
	public String machineName() {
		return group.title();
	}

	@Override
	public SingleItemInventory catalyst() {
		return catalyst;
	}
	
	
}
