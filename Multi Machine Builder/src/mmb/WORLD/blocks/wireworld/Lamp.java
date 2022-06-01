/**
 * 
 */
package mmb.WORLD.blocks.wireworld;

import java.awt.Graphics;

import mmb.DATA.contents.texture.Textures;
import mmb.WORLD.block.BlockEntityDataless;
import mmb.WORLD.block.BlockType;
import mmb.WORLD.blocks.ContentsBlocks;
import mmb.WORLD.texture.BlockDrawer;
import mmb.WORLD.worlds.SignalUtils;

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
		boolean active = SignalUtils.hasIncomingSignal(this);
		if(active) {
			on.draw(this, xx, yy, g, side);
		}else {
			off.draw(this, xx, yy, g, side);
		}
	}
	

}
