/**
 * 
 */
package mmb.world.modulars.chest;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.MMBUtils;
import mmb.menu.world.window.WorldWindow;
import mmb.world.block.BlockEntry;
import mmb.world.block.BlockType;
import mmb.world.blocks.BlockEntityData;
import mmb.world.inventory.Inventory;
import mmb.world.inventory.NoSuchInventory;
import mmb.world.modulars.ModularBlock;
import mmb.world.modulars.Slot;
import mmb.world.modulars.Slot.CoreSlot;
import mmb.world.modulars.Slot.SidedSlotHelper;
import mmb.world.rotate.Side;
import mmb.world.worlds.MapProxy;

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
	@Override
	public void openGUI(WorldWindow window) {
		// TODO Auto-generated method stub
		
	}
	
}
