/**
 * 
 */
package mmb.WORLD.block;

import mmb.WORLD.texture.BlockDrawer;

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
	public String id() {
		return id;
	}

	@Override
	public String title() {
		return title;
	}

	@Override
	public String description() {
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
