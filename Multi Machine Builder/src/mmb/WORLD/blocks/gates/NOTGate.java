/**
 * 
 */
package mmb.WORLD.blocks.gates;

import mmb.DATA.contents.texture.Textures;
import mmb.WORLD.RotatedImageGroup;
import mmb.WORLD.block.BlockType;
import mmb.WORLD.blocks.ContentsBlocks;

/**
 * @author oskar
 *
 */
public class NOTGate extends AbstractUnaryGateBase {

	@Override
	public BlockType type() {
		return ContentsBlocks.NOT;
	}

	private static final RotatedImageGroup texture = RotatedImageGroup.create(Textures.get("logic/NOT.png"));
	@Override
	public RotatedImageGroup getImage() {
		return texture;
	}

	@Override
	protected boolean run(boolean a) {
		return !a;
	}

}
