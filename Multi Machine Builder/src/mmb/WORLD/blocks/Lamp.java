/**
 * 
 */
package mmb.WORLD.blocks;

import java.awt.Graphics;

import mmb.DATA.contents.texture.Textures;
import mmb.WORLD.BlockDrawer;
import mmb.WORLD.block.BlockType;
import mmb.WORLD.block.SkeletalBlockEntityDataless;
import mmb.WORLD.worlds.SignalUtils;
import mmb.WORLD.worlds.world.World.BlockMap;

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
	public void render(int xx, int yy, Graphics g) {
		boolean active = SignalUtils.hasIncomingSignal(x, y, owner);
		if(active) {
			on.draw(xx, yy, g);
		}else {
			off.draw(xx, yy, g);
		}
	}
	

}
