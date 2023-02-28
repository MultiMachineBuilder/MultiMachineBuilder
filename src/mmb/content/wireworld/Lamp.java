/**
 * 
 */
package mmb.content.wireworld;

import java.awt.Graphics;

import mmb.content.ContentsBlocks;
import mmb.engine.block.BlockEntityDataless;
import mmb.engine.block.BlockType;
import mmb.engine.texture.BlockDrawer;
import mmb.engine.texture.Textures;
import mmb.engine.worlds.world.WorldUtils;

/**
 * Turns green when receiving any signal, or red else
 * @author oskar
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
