/**
 * 
 */
package mmb.WORLD_new.block;

import java.util.List;

import mmb.WORLD_new.World;
import mmb.WORLD_new.block.properties.BlockProperty;

/**
 * @author oskar
 *
 */
public class BlockEntryReserved extends BlockEntry {

	/**
	 * @param x
	 * @param y
	 */
	public BlockEntryReserved(int x, int y, World owner) {
		super(x, y, owner);
	}

	@Override
	public List<BlockProperty> getProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BlockType getType() {
		return original().getType();
	}

	@Override
	public BlockEntry original() {
		return owner.
	}

}
