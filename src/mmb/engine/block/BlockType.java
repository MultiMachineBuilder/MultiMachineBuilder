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
 * @author oskar
 *
 */
public interface BlockType extends Placer, ItemType {
	/**
	 * Creates a block entry for this type.
	 * @return newly created block
	 */
	@NN public BlockEntry createBlock();
	
	public boolean isBlockEntity();
	@NN public BlockEntityType asBlockEntityType();
	public BlockEntityType nasBlockEntityType();
	
	@SuppressWarnings({ "unused", "null" })
	public default void register() {
		if(leaveBehind() == null) setLeaveBehind(Blocks.grass); //NOSONAR
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
	@NN public BlockType leaveBehind();

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
