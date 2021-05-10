/**
 * 
 */
package mmb.WORLD.gui;

import java.awt.Graphics;
import java.awt.Point;

import javax.swing.Icon;

import mmb.WORLD.block.BlockEntityType;
import mmb.WORLD.block.BlockType;
import mmb.WORLD.gui.window.WorldWindow;
import mmb.WORLD.worlds.world.World;
import mmb.WORLD.worlds.world.World.BlockMap;

/**
 * @author oskar
 *
 */
public class BlockPlacer implements Placer {
	public final BlockType block;
	/**
	 * Create a {@link BlockPlacer} for a given {@link BlockEntityType}
	 * @param t the {@link BlockEntityType} for the {@link BlockPlacer}
	 */
	public BlockPlacer(BlockType t) {
		this.block = t;
	}
	@Override
	public Icon getIcon() {
		return block.getTexture().toIcon();
	}
	@Override
	public String title() {
		return block.title();
	}
	@Override
	public void place(int x, int y, World map) {
		block.place(x, y, map);
	}

	@Override
	public void openGUI(WorldWindow window) {
		//unused
	}

	@Override
	public void closeGUI(WorldWindow window) {
		//unused
	}
	@Override
	public void preview(Graphics g, Point renderStartPos, BlockMap map, Point targetLocation) {
		block.preview(g, renderStartPos, map, targetLocation);
	}

}
