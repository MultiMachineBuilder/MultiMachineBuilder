/**
 * 
 */
package mmb.WORLD.block;

import java.awt.image.BufferedImage;

import mmb.BEANS.Identifiable;
import mmb.WORLD.BlockDrawer;
import mmb.WORLD.gui.Placer;
import mmb.WORLD.item.ItemType;
import mmb.WORLD.worlds.map.BlockMap;

/**
 * @author oskar
 *
 */
public interface BlockType extends Identifiable<String>, Placer, ItemType {
	
	public BlockEntry create(int x, int y, BlockMap map);
	
	public boolean isBlockEntity();
	public BlockEntityType asBlockEntityType();
	
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
	public BlockType leaveBehind();

	@Override
	default BufferedImage getIcon() {
		return getTexture().img;
	}
	
	
}
