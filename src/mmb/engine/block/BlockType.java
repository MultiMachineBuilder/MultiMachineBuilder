/**
 * 
 */
package mmb.engine.block;

import javax.swing.Icon;

import mmb.NN;
import mmb.engine.chance.Chance;
import mmb.engine.item.ItemType;
import mmb.engine.item.Items;

/**
 * Describes a block, and what the block is.
 * Block entities have additional data
 * @author oskar
 * @see Block
 * @see BlockEntityType
 */
public interface BlockType extends Placer, ItemType {
	/**
	 * Creates a block entry for this type.
	 * @return newly created block
	 */
	@NN public BlockEntry createBlock();
	
	/**
	 * Registers a block with current ID
	 * @throws IllegalStateException if ID is not set
	 */
	@SuppressWarnings({ "unused", "null" })
	public default void register() {
		if(leaveBehind() == null) setLeaveBehind(Blocks.grass); //NOSONAR
		Items.register(this);
	}
	/**
	 * Registers a block
	 * @param id block ID
	 * @throws IllegalStateException if ID is not set
	 */
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
	@NN public BlockType leaveBehind();

	@Override
	default Icon getIcon() {
		return getTexture().toIcon();
	}
	
	/**
	 * Gets the item drops of this block
	 * @return items drops
	 */
	public Chance getDrop();
	/**
	 * Sets the item drops of this block
	 * @param drop item drops
	 */
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
