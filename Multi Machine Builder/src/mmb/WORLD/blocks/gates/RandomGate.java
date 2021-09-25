/**
 * 
 */
package mmb.WORLD.blocks.gates;

import mmb.DATA.contents.texture.Textures;
import mmb.WORLD.block.BlockType;
import mmb.WORLD.blocks.ContentsBlocks;
import mmb.WORLD.rotate.RotatedImageGroup;

/**
 * @author oskar
 *
 */
public class RandomGate extends AbstractUnaryGateBase {

	@Override
	public BlockType type() {
		return ContentsBlocks.RANDOMCTRL;
	}

	private static final RotatedImageGroup texture = RotatedImageGroup.create(Textures.get("logic/randomctrl.png"));
	@Override
	public RotatedImageGroup getImage() {
		return texture;
	}

	@Override
	protected boolean run(boolean a) {
		return  a && Math.random() < 0.5;
	}

}
