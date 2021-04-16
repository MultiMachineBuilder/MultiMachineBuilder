/**
 * 
 */
package mmb.WORLD.block;

import java.awt.Graphics;
import java.awt.Point;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.Icon;

import mmb.WORLD.blocks.ContentsBlocks;
import mmb.WORLD.gui.Placer;
import mmb.WORLD.item.ItemType;
import mmb.WORLD.worlds.world.World.BlockMap;

/**
 * @author oskar
 *
 */
public interface BlockType extends Placer, ItemType {
	
	@Override
	default void preview(Graphics g, Point renderStartPos, BlockMap map, Point targetLocation) {
		getIcon().paintIcon(null, g, renderStartPos.x, renderStartPos.y);
	}

	@Nonnull public BlockEntry create(int x, int y, BlockMap blockMap);
	
	public boolean isBlockEntity();
	@Nonnull public BlockEntityType asBlockEntityType();
	public BlockEntityType nasBlockEntityType();
	
	@SuppressWarnings({ "unused", "null" })
	public default void register() {
		if(leaveBehind() == null) setLeaveBehind(ContentsBlocks.grass); //NOSONAR
		Blocks.register(this);
	}
	public default void register(String id) {
		setID(id);
		register();
	}

	/** set which block is left behind */
	public void setLeaveBehind(BlockType block);
	/** @return a block which is left behind*/
	@Nonnull public BlockType leaveBehind();

	@Override
	default Icon getIcon() {
		return getTexture().toIcon();
	}
	
	
	public Drop getDrop();
	public void setDrop(Drop drop);
}
