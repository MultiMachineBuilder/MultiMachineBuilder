/**
 * 
 */
package mmb.WORLD.worlds.world;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Set;

import mmb.WORLD.Side;
import mmb.WORLD.block.BlockEntity;
import mmb.WORLD.block.BlockEntry;
import mmb.WORLD.block.BlockType;

/**
 * @author oskar
 *
 */
public interface BlockArrayProviderSupplier extends BlockArrayProvider {
	@Override
	default boolean inBounds(int x, int y) {
		return getMap().inBounds(x, y);
	}
	@Override
	default BlockEntry getAtSide(Side s, int x, int y) {
		return getMap().getAtSide(s, x, y);
	}
	/** @return current block map*/
	public BlockArrayProvider getMap();
	@Override
	default boolean isValid() {
		return getMap() != null && getMap().isValid();
	}
	@Override
	default Point start() {
		return getMap().start();
	}
	@Override
	default Dimension size() {
		return getMap().size();
	}
	@Override
	default Rectangle bounds() {
		return getMap().bounds();
	}
	@Override
	default BlockEntry get(int x, int y) {
		return getMap().get(x, y);
	}
	@Override
	default BlockEntry set(BlockEntry block, int x, int y) {
		return getMap().set(block, x, y);
	}
	@Override
	default BlockEntry place(BlockType block, int x, int y) {
		return getMap().place(block, x, y);
	}
	@Override
	default Set<BlockEntity> getBlockEntities() {
		return getMap().getBlockEntities();
	}
	@Override
	default int startX() {
		return getMap().startX();
	}
	@Override
	default int startY() {
		return getMap().startY();
	}
	@Override
	default int sizeX() {
		return getMap().sizeX();
	}
	@Override
	default int sizeY() {
		return getMap().sizeY();
	}
}
