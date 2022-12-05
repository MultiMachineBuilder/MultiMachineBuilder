/**
 * 
 */
package mmbmods.stn.block;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import mmbeng.block.BlockEntityRotary;
import mmbeng.inv.Inventory;
import mmbeng.worlds.world.World;
import mmbmods.stn.STN;
import mmbmods.stn.network.DataLayerSTN;
import mmbmods.stn.network.STNNetworkProcessing.STNRGroupTag;

/**
 * Base class for all STN machines
 * @author oskar
 */
public abstract class STNBaseMachine extends BlockEntityRotary {
	@Nullable private DataLayerSTN network;
	/**
	 * @return the network used by a block
	 * @throws IllegalStateException if block is not placed
	 */
	@SuppressWarnings("null")
	@Nonnull public DataLayerSTN network() {
		if(network == null) throw new IllegalStateException("There is no network");
		return network;
	}
	/**
	 * @return the network used by a block or null if not placed
	 */
	@Nullable public DataLayerSTN nnetwork() {
		return network;
	}
	
	/** Base contructor for STN machines */
	protected STNBaseMachine() {
		eventDemolition.addListener(w -> resetNetwork(null, posX(), posY()));
		
	}
	private void resetNetwork(@Nullable World w, int x, int y) {
		if(network != null) network.disembark(this, x, y);
		network = w==null?null:STN.STN_datalayer.get(w);
		if(network != null) network.embark(this, x, y);
	}
	
	@Override
	public void onStartup(World map, int x, int y) {
		resetNetwork(map, x, y);
	}
	@Override
	public void onPlace(World map, int x, int y) {
		resetNetwork(map, x, y);
	}
	
	/**
	 * Returns all supported recipes (applicable for processors)
	 * @return supported recipes, or null if unsupported or unconfigured (used for re-validation)
	 */
	@Nullable public abstract STNRGroupTag recipes();
	/**
	 * Returns the storage inventory for this unit
	 * @return storage inventory, or null if absent
	 */
	@Nullable public abstract Inventory storage();
	/**
	 * Returns previously supported recipes (applicable for processors)
	 * @return supported recipes, or null if unsupported or unconfigured (used for re-validation)
	 */
	@Nullable public abstract STNRGroupTag oldrecipes();
	/**
	 * Returns the previous storage inventory for this unit
	 * @return storage inventory, or null if absent
	 */
	@Nullable public abstract Inventory oldstorage();
	
	@Override
	public final STNBaseMachine blockCopy() {
		STNBaseMachine copy = blockCopy0();
		copy.setChirotation(getChirotation());
		return copy;
	}
	protected abstract STNBaseMachine blockCopy0();
	
}
