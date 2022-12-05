/**
 * 
 */
package mmbmods.stn.block;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import mmbeng.block.BlockEntry;
import mmbeng.block.BlockType;
import mmbeng.inv.Inventory;
import mmbeng.rotate.RotatedImageGroup;
import mmbeng.worlds.MapProxy;
import mmbmods.stn.STN;
import mmbmods.stn.network.STNNetworkProcessing.STNRGroupTag;

/**
 * @author oskar
 *
 */
public class STNStorageAttachment extends STNBaseMachine {
	@Nonnull private static final RotatedImageGroup rig = RotatedImageGroup.create("stn/storage.png");

	@Override
	public BlockType type() {
		return STN.STN_storage;
	}

	//Recipes - usupported
	@Override
	public STNRGroupTag recipes() {
		return null;
	}
	@Override
	public STNRGroupTag oldrecipes() {
		return null;
	}

	@Override
	public RotatedImageGroup getImage() {
		return rig;
	}

	@Nullable private Inventory storage;
	@Nullable private Inventory old;
	@Override
	public Inventory storage() {
		return storage;
	}
	@Override
	public Inventory oldstorage() {
		return old;
	}

	@Override
	public void onTick(MapProxy map) {
		old = storage;
		storage = getAtSide(getRotation().U()).getInventory(getRotation().D());
		if(storage != old) network().revalidate(this);
	}

	@Override
	protected STNBaseMachine blockCopy0() {
		return new STNStorageAttachment();
	}
}
