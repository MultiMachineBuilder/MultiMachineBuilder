/**
 * 
 */
package mmb.content.stn.block;

import mmb.NN;
import mmb.Nil;
import mmb.content.stn.STN;
import mmb.content.stn.network.STNNetworkInventory;
import mmb.content.stn.network.STNNetworkProcessing.STNRGroupTag;
import mmb.engine.block.BlockType;
import mmb.engine.inv.Inventory;
import mmb.engine.rotate.RotatedImageGroup;
import mmb.engine.worlds.MapProxy;

/**
 * @author oskar
 *
 */
public class STNStorageAttachment extends STNBaseMachine {
	@NN private static final RotatedImageGroup rig = RotatedImageGroup.create("stn/storage.png");

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

	@Nil private Inventory storage;
	@Nil private Inventory old;
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
		if(storage instanceof STNNetworkInventory) storage = null;
		if(storage != old) network().revalidate(this);
	}

	@Override
	protected STNBaseMachine blockCopy0() {
		return new STNStorageAttachment();
	}
}
