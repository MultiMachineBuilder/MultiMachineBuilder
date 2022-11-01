/**
 * 
 */
package mmb.world.modulars.chest;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.MMBUtils;
import mmb.world.block.BlockEntityData;
import mmb.world.block.BlockEntry;
import mmb.world.block.BlockType;
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
public class ModularChest extends BlockEntityData implements ModularBlock<ModularChest, BlockModuleUniversal, ChestCore<?>, Object> {

	@Override
	public BlockType type() {
		return ModularChests.chest;
	}

	@Override
	public BlockEntry blockCopy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void load(JsonNode data) {
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
	
	//TODO settings

	@Override
	public void loadSettings(JsonNode node, Object settings) {
		// TODO Auto-generated method stub
	}

	@Override
	public JsonNode saveSettings(Object node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getSettings() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSettings(Object settings) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onTick(MapProxy map) {
		runAllModules(owner(), posX(), posY());
	}
	

}
