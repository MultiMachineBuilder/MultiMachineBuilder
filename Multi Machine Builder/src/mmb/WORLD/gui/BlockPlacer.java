/**
 * 
 */
package mmb.WORLD.gui;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

import mmb.WORLD.block.BlockType;
import mmb.WORLD.worlds.map.BlockMap;

/**
 * @author oskar
 *
 */
public class BlockPlacer implements Placer {
	public final BlockType block;
	/**
	 * Create a {@link BlockPlacer} for a given {@link BlockType}
	 * @param block the {@link BlockType} for the {@link BlockPlacer}
	 */
	public BlockPlacer(BlockType block) {
		this.block = block;
	}
	@Override
	public BufferedImage getIcon() {
		return block.drawer.img;
	}

	@Override
	public String getTitle() {
		return block.title;
	}
	@Override
	public void place(int x, int y, BlockMap map) {
		map.place(x, y, block);
	}
	@Override
	public void openGUI() {
		//unused
	}
	@Override
	public void closeGUI() {
		//unused
	}
	@Override
	public void preview(Graphics g, Point renderStartPos, BlockMap map, Point targetLocation) {
		g.drawImage(getIcon(), renderStartPos.x, renderStartPos.y, null);
	}

}
