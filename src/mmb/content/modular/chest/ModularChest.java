/**
 * 
 */
package mmb.content.modular.chest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.NN;
import mmb.Nil;
import mmb.content.modular.ModularBlock;
import mmb.content.modular.Slot;
import mmb.content.modular.Slot.CoreSlot;
import mmb.content.modular.Slot.SidedSlotHelper;
import mmb.content.modular.gui.ModularChestGUI;
import mmb.engine.MMBUtils;
import mmb.engine.block.BlockEntityData;
import mmb.engine.block.BlockEntry;
import mmb.engine.block.BlockType;
import mmb.engine.inv.Inventory;
import mmb.engine.inv.NoSuchInventory;
import mmb.engine.inv.SaveInventory;
import mmb.engine.rotate.Side;
import mmb.engine.worlds.MapProxy;

/**
 * A chest which accepts modules
 * @author oskar
 */
public final class ModularChest extends BlockEntityData implements
ModularBlock<ModularChest, BlockModuleUniversal, ChestCore<? extends SaveInventory>, Object> {
	//Basic stuff
	@Override
	public BlockType type() {
		return ModularChests.chest;
	}
	@Override
	public BlockEntry blockCopy() {
		ModularChest copy = new ModularChest();
		copy.setCore(core());
		copy.setModule(module(Side.U), Side.U);
		copy.setModule(module(Side.D), Side.D);
		copy.setModule(module(Side.L), Side.L);
		copy.setModule(module(Side.R), Side.R);
		return copy;
	}

	//Core
	@NN private static Class<ChestCore<? extends SaveInventory>> cls1 = MMBUtils.classcast(ChestCore.class);
	@NN private final CoreSlot<ModularChest, ChestCore<? extends SaveInventory>> core = new CoreSlot<>(that(), cls1);
	@Override
	public @NN Slot<@Nil ChestCore<?>> slotC() {
		return core;
	}
	
	//Modules
	@Override
	public boolean isModular() {
		return true;
	}
	@NN private final SidedSlotHelper<ModularChest, BlockModuleUniversal> ssh = new SidedSlotHelper<>(BlockModuleUniversal.class, that());
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

	@Override
	public void onTick(MapProxy map) {
		runAllModules(owner(), posX(), posY());
	}
	
	//Inventory access
	@Override
	public Inventory i_inv(Side s) {
		ChestCore<?> c = core();
		if(c == null) return NoSuchInventory.INSTANCE;
		return c.inventory;
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
	
	//Serialization
	@Override
	public void load(@Nil JsonNode data) {
		loadModularHelper(data);
	}
	@Override
	protected void save0(ObjectNode node) {
		saveModularHelper(node);
	}
}
