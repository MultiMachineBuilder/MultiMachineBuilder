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
public class ORGate extends AbstractBiGateBase {

	@Override
	public BlockType type() {
		return ContentsBlocks.OR;
	}

	@Override
	protected boolean run(boolean a, boolean b) {
		return a || b;
	}

	private static final RotatedImageGroup texture = RotatedImageGroup.create(Textures.get("logic/OR.png"));
	@Override
	public RotatedImageGroup getImage() {
		return texture;
	}

}
