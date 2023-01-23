/**
 * 
 */
package mmb.content.electric.machines;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.NN;
import mmb.Nil;
import mmb.cgui.BlockActivateListener;
import mmb.content.electric.Battery;
import mmb.content.electric.Electricity;
import mmb.content.electric.VoltageTier;
import mmb.content.electric.helper.Helper;
import mmb.data.variables.ListenableBoolean;
import mmb.content.electric.ElectricMachineGroup.ElectroMachineType;
import mmb.engine.block.BlockEntityRotary;
import mmb.engine.block.BlockEntry;
import mmb.engine.debug.Debugger;
import mmb.engine.inv.Inventories;
import mmb.engine.inv.Inventory;
import mmb.engine.inv.io.InventoryReader;
import mmb.engine.inv.io.InventoryWriter;
import mmb.engine.inv.storage.SimpleInventory;
import mmb.engine.inv.storage.SingleItemInventory;
import mmb.engine.rotate.RotatedImageGroup;
import mmb.engine.rotate.Side;
import mmb.engine.worlds.MapProxy;
import mmb.engine.worlds.world.World;
import mmb.menu.world.window.WorldWindow;

//FIXME items disappear on craft
/**
 * @author oskar
 * A class with many common utilities for machines
 */
public abstract class ProcessorAbstractBlock extends BlockEntityRotary implements ElectroMachine, BlockActivateListener{
	//Constructors
	/** Constructs base components of a machine */
	protected ProcessorAbstractBlock(ElectroMachineType type) {
		super();
		elec = new Battery(200, 400, this, type.volt);
		this.type = type;
	}
	
	//Contents
	/** The input inventory */
	@NN public final SimpleInventory in = new SimpleInventory();
	/** Internal output inventory for recipe processors */
	@NN protected final SimpleInventory out0 = new SimpleInventory();
	/** The output inventory */
	@NN public final Inventory out = out0.lockInsertions();
	/** The electrical buffer */
	@NN public final Battery elec;
	/** The block type */
	@NN protected final ElectroMachineType type;
	
	/** Should unsupported items be passed on?*/
	@NN public final ListenableBoolean pass = new ListenableBoolean();
	@Override
	public boolean isPass() {
		return pass.getValue();
	}
	@Override
	public void setPass(boolean pass) {
		this.pass.setValue(pass);
	}
	
	/** Should items be auto-extracted?*/
	@NN public final ListenableBoolean autoExtract = new ListenableBoolean();
	@Override
	public boolean isAutoExtract() {
		return autoExtract.getValue();
	}
	@Override
	public void setAutoExtract(boolean autoExtract) {
		this.autoExtract.setValue(autoExtract);
	}
		
	//Block methods
	/**
	 * Performs partial copy of this machine. The copies items may include recipe helper and catalyst.
	 * For implementers: Do not copy input, output, pass on, auto-extract and battery., as copying these is redundant
	 * @return the partial copy
	 */
	@NN protected abstract ProcessorAbstractBlock copy0();
	@Override
	public final BlockEntry blockCopy() {
		ProcessorAbstractBlock copy = copy0();
		copy.autoExtract.setValue(autoExtract.getValue());
		copy.pass.setValue(pass.getValue());
		copy.in.set(in);
		copy.out0.set(out0);
		copy.elec.set(elec);
		return copy;
	}
	@Override
	public ElectroMachineType type() {
		return type;
	}
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
	@Override
	public RotatedImageGroup getImage() {
		return type.rig;
	}
	
	//GUI
	protected GUIMachine tab;
	@Override
	public void click(int blockX, int blockY, World map, @Nil WorldWindow window, double partX, double partY) {
		if(window == null) return;
		if(tab != null) return;
		tab = new GUIMachine(this, window);
		window.openAndShowWindow(tab, recipes().title()+' '+type.volt.name);
		helper().setRefreshable(tab);
		tab.refreshProgress(0, helper().currentRecipe());
	}
	@SuppressWarnings("javadoc") //internal use only
	public void close(GUIMachine tbb) {
		if(tab == tbb) tab = null;
	}
	
	//Machine methods
	@Override
	public VoltageTier voltage() {
		return elec.voltage;
	}
	@Override
	public Inventory input() {
		return in;
	}
	@Override
	public Inventory output() {
		return out;
	}
	@Override
	public Battery energy() {
		return elec;
	}
	/** @return the machine helper */
	public abstract Helper<?> helper();
	@Override
	public String machineName() {
		return recipes().title();
	}
	/**
	 * @apiNote This method may be overridden if catalysts are supported
	 * @return the item catalyst inventory, or {@code null} if catalysts are unsupported
	 */
	@Nil public SingleItemInventory catalyst() {
		return null;
	}
	
	//Serialization
	@Override
	protected void save1(ObjectNode node) {
		helper().save(node);
		JsonNode bat = elec.save();
		node.set("energy", bat);
		node.set("in", in.save());
		node.set("out", out0.save());
		node.put("pass", pass.getValue());
		node.put("autoex", autoExtract.getValue());
	}
	@Override
	protected void load1(ObjectNode node) {
		helper().load(node);
		JsonNode bat = node.get("energy");
		elec.load(bat);
		in.load(node.get("in"));
		in.setCapacity(2);
		out0.load(node.get("out"));
		out0.setCapacity(2);
		JsonNode passNode = node.get("pass");
		if(passNode != null) pass.setValue(passNode.asBoolean());
		JsonNode autoNode = node.get("autoex");
		if(autoNode != null) autoExtract.setValue(autoNode.asBoolean());
	}
	
	@Override
	public final void onTick(MapProxy proxy) {
		elec.capacity = 400;
		elec.maxPower = 200;
		if(autoExtract.getValue()) {
			InventoryWriter writer = getAtSide(getRotation().R()).getInput(getRotation().L());
			Inventories.transferAll(out, writer); //FIXME stuck
		}
		//Pass on items
		if(pass.getValue() && Math.random() < 0.1) { //10% chance to pass on unsupported items
			//Pass on items
			Inventories.transfer(in, out0, r -> !recipes().supportedItems().contains(r.item()));
			if(tab != null) {
				tab.refreshInputs();
				tab.refreshOutputs();
			}
		}
		Electricity.equatePPs(this, proxy, elec, 0.9);
		helper().cycle();
		onTick0(proxy);
	}
		
	/**
	 * This method may be overridden to run something on a tick
	 * @param proxy map proxy
	 */
	protected void onTick0(MapProxy proxy) {
		//overridable
	}
	
}