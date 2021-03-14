/**
 * 
 */
package mmb.WORLD.block;

import java.awt.image.BufferedImage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import mmb.WORLD.gui.Placer;
import mmb.WORLD.item.ItemType;
import mmb.WORLD.worlds.world.World.BlockMap;

/**
 * @author oskar
 *
 */
public interface BlockType extends Placer, ItemType {
	
	@Nonnull public BlockEntry create(int x, int y, BlockMap blockMap);
	
	public boolean isBlockEntity();
	@Nonnull public BlockEntityType asBlockEntityType();
	
	public default void register() {
		Blocks.register(this);
	}
	public default void register(String id) {
		setID(id);
		Blocks.register(this);
	}

	/** set which block is left behind */
	public void setLeaveBehind(BlockType block);
	/** @return a block which is left behind*/
	@Nonnull public BlockType leaveBehind();

	@Override
	default @Nullable BufferedImage getIcon() {
		return getTexture().img;
	}
	
	
}
