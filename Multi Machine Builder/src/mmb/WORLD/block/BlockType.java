/**
 * 
 */
package mmb.WORLD.block;

import javax.annotation.Nonnull;
import javax.swing.Icon;

import mmb.WORLD.blocks.ContentsBlocks;
import mmb.WORLD.gui.Placer;
import mmb.WORLD.item.ItemType;
import mmb.WORLD.worlds.world.World;

/**
 * @author oskar
 *
 */
public interface BlockType extends Placer, ItemType {
	
	/*@Override
	default void preview(Graphics g, Point renderStartPos, BlockMap map, Point targetLocation, int side) {
		getI*con().paintIcon(null, g, renderStartPos.x, renderStartPos.y);
	}*/

	/**
	 * Creates a block entry for this type.
	 * @param x X coordinate of the block
	 * @param y Y coordinate of the block
	 * @param map map which will contain the block
	 * @return newly created block
	 */
	@Nonnull public BlockEntry create(int x, int y, World map);
	
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

	/**
	 * Sets which block is left behind 
	 * @param block leave behind
	 */
	public void setLeaveBehind(BlockType block);
	/** @return a block which is left behind*/
	@Nonnull public BlockType leaveBehind();

	@Override
	default Icon getIcon() {
		return getTexture().toIcon();
	}
	
	public Drop getDrop();
	public void setDrop(Drop drop);
	
	/**
	 * @return is given block a surface block?
	 */
	public default boolean isSurface() {
		return false;
	}
}
