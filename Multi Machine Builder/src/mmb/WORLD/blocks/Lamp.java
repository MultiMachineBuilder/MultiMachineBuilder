/**
 * 
 */
package mmb.WORLD.blocks;

import java.awt.Graphics;

import mmb.DATA.contents.texture.Textures;
import mmb.WORLD.block.BlockType;
import mmb.WORLD.block.SkeletalBlockEntityDataless;
import mmb.WORLD.texture.BlockDrawer;
import mmb.WORLD.worlds.SignalUtils;
import mmb.WORLD.worlds.world.BlockMap;

/**
 * @author oskar
 *
 */
public class Lamp extends SkeletalBlockEntityDataless {
	private static final BlockDrawer on = BlockDrawer.ofImage(Textures.get("logic/on lamp.png"));
	private static final BlockDrawer off = BlockDrawer.ofImage(Textures.get("logic/off lamp.png"));
	public Lamp(int x, int y, BlockMap owner2) {
		super(x, y, owner2);
	}

	@Override
	public BlockType type() {
		return ContentsBlocks.LAMP;
	}

	@Override
	public void render(int xx, int yy, Graphics g, int side) {
		boolean active = SignalUtils.hasIncomingSignal(x, y, owner);
		if(active) {
			on.draw(this, xx, yy, g, side);
		}else {
			off.draw(this, xx, yy, g, side);
		}
	}
	

}
