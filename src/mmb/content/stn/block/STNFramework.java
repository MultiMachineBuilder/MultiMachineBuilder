/**
 * 
 */
package mmb.content.stn.block;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.NN;
import mmb.Nil;
import mmb.content.modular.ModularBlock;
import mmb.content.modular.Slot;
import mmb.content.modular.Slot.CoreSlot;
import mmb.content.modular.Slot.SidedSlotHelper;
import mmb.content.modular.chest.BlockModuleUniversal;
import mmb.content.modular.chest.ChestCore;
import mmb.content.modular.chest.ModularChests;
import mmb.content.modular.gui.ModularChestGUI;
import mmb.content.stn.network.STNNetworkInventory;
import mmb.content.stn.network.STNNetworkProcessing.STNRGroupTag;
import mmb.engine.MMBUtils;
import mmb.engine.block.BlockType;
import mmb.engine.inv.Inventory;
import mmb.engine.inv.NoSuchInventory;
import mmb.engine.rotate.RotatedImageGroup;
import mmb.engine.rotate.Side;
import mmb.engine.worlds.MapProxy;

/**
 *
 * @author oskar
 *
 */
public class STNFramework extends STNBaseMachine implements ModularBlock<STNFramework, BlockModuleUniversal, ChestCore<?>, Object> {
	@NN private static final RotatedImageGroup rig = RotatedImageGroup.create("stn/fw.png");
	
	//Basic stuff
	@Override
	public BlockType type() {
		return ModularChests.chest;
	}
	@Override
	public STNBaseMachine blockCopy0() {
		STNFramework copy = new STNFramework();
		copy.setCore(core());
		copy.setModule(module(Side.U), Side.U);
		copy.setModule(module(Side.D), Side.D);
		copy.setModule(module(Side.L), Side.L);
		copy.setModule(module(Side.R), Side.R);
		return copy;
	}

	//Serialization
	@Override
	public void load1(ObjectNode data) {
		loadModularHelper(data);
	}
	@Override
	protected void save1(ObjectNode node) {
		saveModularHelper(node);
	}

	//Core
	@NN private static Class<ChestCore<?>> cls1 = MMBUtils.classcast(ChestCore.class);
	@NN private final CoreSlot<STNFramework, ChestCore<?>> core = new CoreSlot<>(this, cls1);
	@Override
	public Slot<ChestCore<?>> slotC() {
		return core;
	}
	
	//Modules
	@Override
	public boolean isModular() {
		return true;
	}
	@NN private final SidedSlotHelper<STNFramework, BlockModuleUniversal> ssh = new SidedSlotHelper<>(BlockModuleUniversal.class, this);
	@Override
	public Slot<BlockModuleUniversal> slotInternal(Side s) {
		return ssh.get(s);
	}
	
	//No settings support
	@Override
	public void loadSettings(@Nil JsonNode node, @Nil Object settings) {
		//unused
	}
	@Override
	public JsonNode saveSettings(@Nil Object node) {
		return null;
	}
	@Override
	public Object getSettings() {
		return null;
	}
	@Override
	public void setSettings(@Nil Object settings) {
		//unused
	}

	@Nil private Inventory old;
	@Nil private Inventory storage;
	@Override
	public void onTick(MapProxy map) {
		runAllModules(owner(), posX(), posY());
		old = storage;
		storage = getAtSide(getRotation().U()).getInventory(getRotation().D());
		if(storage instanceof STNNetworkInventory) storage = null;
		if(storage != old) network().revalidate(this);
	}
	
	//Inventory access
	@Override
	public Inventory i_inv(Side s) {
		return network().inv;
	}
	/** @return inventory of the used module */
	public Inventory inv() {
		ChestCore<?> c = core();
		if(c == null) return NoSuchInventory.INSTANCE;
		return c.inventory;
	}

	@Override
	public @Nil STNRGroupTag recipes() {
		return null;
	}
	@Override
	public @Nil STNRGroupTag oldrecipes() {
		return null;
	}
	
	@Override
	public @Nil Inventory storage() {
		return storage;
	}

	@Override
	public @Nil Inventory oldstorage() {
		return old;
	}

	@Override
	public RotatedImageGroup getImage() {
		return rig;
	}
	
	//GUI
	private ModularChestGUI gui;
	@Override
	public void closeTab(ModularChestGUI gui0) {
		if(gui == gui0) gui = null;
	}
	@Override
	public boolean openTab(ModularChestGUI tab) {
		if(gui != null) return false;
		gui = tab;
		return true;
	}

}
