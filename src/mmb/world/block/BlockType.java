/**
 * 
 */
package mmb.world.block;

import javax.annotation.Nonnull;
import javax.swing.Icon;

import mmb.menu.world.Placer;
import mmb.world.blocks.ContentsBlocks;
import mmb.world.chance.Chance;
import mmb.world.item.ItemType;
import mmb.world.item.Items;

/**
 * @author oskar
 *
 */
public interface BlockType extends Placer, ItemType {
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
		Items.register(this);
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
	
	public Chance getDrop();
	public void setDrop(Chance drop);
	
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
