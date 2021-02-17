/**
 * 
 */
package mmb.WORLD.block;

import mmb.Identifiable;
import mmb.WORLD.BlockDrawer;
import mmb.WORLD.gui.Placer;
import mmb.WORLD.worlds.map.BlockMap;

/**
 * @author oskar
 *
 */
public interface BlockType extends Identifiable<String>, Placer {
	public String getDescription();
	public BlockEntry create(int x, int y, BlockMap map);
	public BlockDrawer getTexture();
	/**
	 * @return
	 */
	public BlockType leaveBehind();
	public boolean isBlockEntity();
	
	public void setID(String id);
	
	public default void register() {
		Blocks.register(this);
	}

	public default void register(String id) {
		setID(id);
		Blocks.register(this);
	}
}
