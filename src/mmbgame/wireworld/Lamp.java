/**
 * 
 */
package mmbgame.wireworld;

import java.awt.Graphics;

import mmbeng.block.BlockEntityDataless;
import mmbeng.block.BlockType;
import mmbeng.texture.BlockDrawer;
import mmbeng.texture.Textures;
import mmbeng.worlds.world.WorldUtils;
import mmbgame.ContentsBlocks;

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
