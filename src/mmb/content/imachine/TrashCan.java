package mmb.content.imachine;

import mmb.PropertyExtension;
import mmb.annotations.NN;
import mmb.engine.block.Block;
import mmb.engine.inv.Inventory;
import mmb.engine.inv.special.TrashInventory;
import mmb.engine.rotate.Side;

public class TrashCan extends Block {
	public TrashCan(String id, PropertyExtension... properties) {
		super(id, properties);
	}

	@Override
	public @NN Inventory getInventory(Side s) {
		return TrashInventory.INSTANCE;
	}
}
