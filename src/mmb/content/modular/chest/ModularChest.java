/**
 * 
 */
package mmbgame.modular.chest;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.menu.world.window.GUITab;
import mmb.menu.world.window.WorldWindow;
import mmbeng.MMBUtils;
import mmbeng.block.BlockEntityData;
import mmbeng.block.BlockEntry;
import mmbeng.block.BlockType;
import mmbeng.inv.Inventory;
import mmbeng.inv.NoSuchInventory;
import mmbeng.rotate.Side;
import mmbeng.worlds.MapProxy;
import mmbgame.modular.ModularBlock;
import mmbgame.modular.Slot;
import mmbgame.modular.Slot.CoreSlot;
import mmbgame.modular.Slot.SidedSlotHelper;
import mmbgame.modular.gui.ModularChestGUI;

/**
 * A chest which accepts modules
 * @author oskar
 */
public final class ModularChest extends BlockEntityData implements ModularBlock<ModularChest, BlockModuleUniversal, ChestCore<?>, Object> {	
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

	//Serialization
	@Override
	public void load(@Nullable JsonNode data) {
		loadModularHelper(data);
	}
	@Override
	protected void save0(ObjectNode node) {
		saveModularHelper(node);
	}

	//Core
	@Nonnull private static Class<ChestCore<?>> cls1 = MMBUtils.classcast(ChestCore.class);
	@Nonnull private final CoreSlot<ModularChest, ChestCore<?>> core = new CoreSlot<>(this, cls1);
	@Override
	public Slot<ChestCore<?>> slotC() {
		return core;
	}
	
	//Modules
	@Override
	public boolean isModular() {
		return true;
	}
	@Nonnull private final SidedSlotHelper<ModularChest, BlockModuleUniversal> ssh = new SidedSlotHelper<>(BlockModuleUniversal.class, this);
	@Override
	public Slot<BlockModuleUniversal> slotInternal(Side s) {
		return ssh.get(s);
	}
	
	//No settings support
	@Override
	public void loadSettings(@Nullable JsonNode node, Object settings) {
		//unused
	}
	@Override
	public JsonNode saveSettings(Object node) {
		return null;
	}
	@Override
	public Object getSettings() {
		return null;
	}
	@Override
	public void setSettings(Object settings) {
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
	
}
