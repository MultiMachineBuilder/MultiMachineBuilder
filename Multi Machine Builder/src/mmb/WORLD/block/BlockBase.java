/**
 * 
 */
package mmb.WORLD.block;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

import mmb.WORLD.BlockDrawer;
import mmb.WORLD.worlds.map.BlockMap;

/**
 * @author oskar
 *
 */
public abstract class BlockBase implements BlockType {
	
	public String id;
	public String title;
	public String description;
	public BlockType leaveBehind;
	public BlockDrawer texture;
	
	@Override
	public BufferedImage getIcon() {
		return texture.img;
	}

	@Override
	public void preview(Graphics g, Point renderStartPos, BlockMap map, Point targetLocation) {
		texture.draw(renderStartPos, g);
	}

	@Override
	public String identifier() {
		return id;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public BlockDrawer getTexture() {
		return texture;
	}

	@Override
	public BlockType leaveBehind() {
		return leaveBehind;
	}

}
