/**
 * 
 */
package mmb.WORLD.block;

import javax.annotation.Nonnull;
import javax.swing.Icon;

import mmb.WORLD.blocks.ContentsBlocks;
import mmb.WORLD.gui.Placer;
import mmb.WORLD.item.ItemType;

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
	 * @return newly created block
	 */
	@Nonnull public BlockEntry createBlock();
	
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
	 * @return required pickaxe level
	 */
	public int getRequiredPickaxe();
	/**
	 * @param level new required pickaxe level
	 */
	public void setRequiredPickaxe(int level);
	
	/**
	 * @return is given block a surface block?
	 */
	public default boolean isSurface() {
		return false;
	}
}
