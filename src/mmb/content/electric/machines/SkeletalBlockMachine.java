/**
 * 
 */
package mmb.content.electric.machines;

import java.awt.Component;
import java.util.function.Consumer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.NN;
import mmb.Nil;
import mmb.content.electric.Battery;
import mmb.content.electric.Electricity;
import mmb.content.electric.SideConfig;
import mmb.content.electric.VoltageTier;
import mmb.content.electric.SideConfig.SideBoolean;
import mmb.engine.block.BlockEntityData;
import mmb.engine.inv.io.InventoryReader;
import mmb.engine.inv.io.InventoryWriter;
import mmb.engine.inv.storage.SimpleInventory;
import mmb.engine.rotate.Side;
import mmb.engine.worlds.MapProxy;
import mmb.engine.worlds.world.World;
import mmbbase.beans.Titled;
import mmbbase.cgui.BlockActivateListener;
import mmbbase.menu.world.window.WorldWindow;

/**
 * @author oskar
 * A block which implements electricity and item I/O capabilities. It has helper indicators.
 */
public abstract class SkeletalBlockMachine extends BlockEntityData implements BlockActivateListener, Titled{
	
	//Electrical components
	@NN protected Battery inElec = new Battery(200, 20000, this, VoltageTier.V1);
	@NN SideConfig cfgInElec = new SideConfig();
	@NN protected Battery outElec  = new Battery(200, 20000, this, VoltageTier.V1);
	@NN SideConfig cfgOutElec = new SideConfig();
	
	//Item components
	@NN protected SimpleInventory inItems = new SimpleInventory();
	@NN SideConfig cfgInItems = new SideConfig();
	@NN protected SimpleInventory outItems = new SimpleInventory();
	@NN SideConfig cfgOutItems = new SideConfig();
	
	//Setting flags
	/** This constant contains active components. All flags begin with 'SETTING_FLAG_' */
	int flags;
	public static final int SETTING_FLAG_ELEC_INPUT = 1;
	public static final int SETTING_FLAG_ELEC_OUTPUT = 2;
	public static final int SETTING_FLAG_ITEM_INPUT = 4;
	public static final int SETTING_FLAG_ITEM_OUTPUT = 8;
	
	private final Consumer<@NN SideBoolean> SHOVE_ELECTRICITY = s -> {
		if(s.value){
			Electricity elec = getAtSide(s.side).getElectricalConnection(s.side.negate());
			if(elec == null) return;
			outElec.extractTo(elec);
		}
	};
	@Override
	public final void onTick(MapProxy map) {
		if(gui != null) gui.refresh();
		cfgOutElec.forEach(SHOVE_ELECTRICITY);
		onTick0(map);
	}
	/**
	 * This method should be overridden. Runs on every tick
	 * @param map  
	 */
	protected void onTick0(MapProxy map) {
		//to be overridden
	}

	
	
	/**
	 * @param x
	 * @param y
	 * @param owner2
	 * @param flags
	 */
	protected SkeletalBlockMachine(int flags) {
		this.flags = flags;
	}

	@Override
	public InventoryReader getOutput(Side s) {
		return cfgOutItems.get(s) ? outItems.createReader() : InventoryReader.NONE;
	}

	@Override
	public InventoryWriter getInput(Side s) {
		return cfgInItems.get(s) ? inItems.createWriter() : InventoryWriter.NONE;
	}

	@Override
	public Electricity getElectricalConnection(Side s) {
		if(cfgInElec.get(s)) return inElec;
		if(cfgOutElec.get(s)) return Electricity.NONE;
		return null;
	}

	@Override
	public final void load(@Nil JsonNode data) {
		if(data == null) return;
		load1((ObjectNode)data);
		if((flags & SETTING_FLAG_ELEC_INPUT) != 0) {
			inElec.load(data.get("inElec"));
			cfgInElec.load(data.get("inElecCfg"));
		}
		if((flags & SETTING_FLAG_ELEC_OUTPUT) != 0) {
			outElec.load(data.get("outElec"));
			cfgOutElec.load(data.get("outElecCfg"));
		}
		if((flags & SETTING_FLAG_ITEM_INPUT) != 0) {
			inItems.load(data.get("inItem"));
			cfgInItems.load(data.get("inItemCfg"));
		}
		if((flags & SETTING_FLAG_ITEM_OUTPUT) != 0) {
			outItems.load(data.get("outItem"));
			cfgOutItems.load(data.get("outItemCfg"));
		}
	}

	@Override
	protected final void save0(ObjectNode node) {
		save1(node);
		if((flags & SETTING_FLAG_ELEC_INPUT) != 0) {
			node.set("inElec", inElec.save());
			node.set("inElecCfg", cfgInElec.save());
		}
		if((flags & SETTING_FLAG_ELEC_OUTPUT) != 0) {
			node.set("outElec", outElec.save());
			node.set("outElecCfg", cfgOutElec.save());
		}
		if((flags & SETTING_FLAG_ITEM_INPUT) != 0) {
			node.set("inItem", inItems.save());
			node.set("inItemCfg", cfgInItems.save());
		}
		if((flags & SETTING_FLAG_ITEM_OUTPUT) != 0) {
			node.set("outItem", outItems.save());
			node.set("outItemCfg", cfgOutItems.save());
		}
	}
	
	/**
	 * Additional function used to save additional data
	 * @param node node, to which data can be saved
	 */
	protected void save1(ObjectNode node) {
		//optional
	}
	/**
	 * Additional function used to save additional data
	 * @param node node, to which data can be loaded
	 */
	protected void load1(ObjectNode node) {
		//optional
	}	
	
	/**
	 * Represents title of this machine
	 * @return the title of this machine
	 */
	@Override
	@NN public String title() {
		return type().title();
	}
	MachineGUI gui;
	@Override
	public void click(int blockX, int blockY, World map, @Nil WorldWindow window, double partX, double partY) {
		if(window == null) return;
		MachineGUI gui0 = gui;
		if(gui0 != null) return;
		gui0 = new MachineGUI(this, window);
		window.openAndShowWindow(gui0, title());
		gui = gui0;
	}
	
	/**
	 * Override to create custom GUI. If set to null, GUI is not created.
	 * If it implements AutoCloseable, it will be closed.
	 * If it implements Update, it will be updated every tick
	 * @return the custom GUI
	 */
	@SuppressWarnings("static-method") // the machine should override it
	protected Component createGUI() {
		return null;
	}
	
	/**
	 * @author oskar
	 * Declare this interface on MachineGUI to update it  on every tick
	 */
	public static interface Update{
		/** Updates the machine GUI */
		public void update();
	}
}
