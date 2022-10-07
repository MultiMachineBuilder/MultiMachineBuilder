/**
 * 
 */
package mmb.world.blocks.wireworld;

import java.awt.Graphics;

import mmb.data.contents.Textures;
import mmb.graphics.texture.BlockDrawer;
import mmb.world.block.BlockEntityDataless;
import mmb.world.block.BlockType;
import mmb.world.blocks.ContentsBlocks;
import mmb.world.worlds.world.WorldUtils;

/**
 * @author oskar
 *
 */
public class Lamp extends BlockEntityDataless {
	private static final BlockDrawer on = BlockDrawer.ofImage(Textures.get("logic/on lamp.png"));
	private static final BlockDrawer off = BlockDrawer.ofImage(Textures.get("logic/off lamp.png"));

	@Override
	public BlockType type() {
		return ContentsBlocks.LAMP;
	}

	@Override
	public void render(int xx, int yy, Graphics g, int side) {
		boolean active = WorldUtils.hasIncomingSignal(this);
		if(active) {
			on.draw(this, xx, yy, g, side);
		}else {
			off.draw(this, xx, yy, g, side);
		}
	}
	

}
