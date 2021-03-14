/**
 * 
 */
package mmb.WORLD.blocks.gates;

import mmb.DATA.contents.texture.Textures;
import mmb.WORLD.RotatedImageGroup;
import mmb.WORLD.block.BlockType;
import mmb.WORLD.blocks.ContentsBlocks;
import mmb.WORLD.worlds.world.World.BlockMap;

/**
 * @author oskar
 *
 */
public class XORGate extends AbstractBiGateBase {
	public XORGate(int x, int y, BlockMap owner2) {
		super(x, y, owner2);
	}

	@Override
	public BlockType type() {
		return ContentsBlocks.XOR;
	}

	@Override
	protected boolean run(boolean a, boolean b) {
		return a ^ b;
	}

	private static final RotatedImageGroup texture = RotatedImageGroup.create(Textures.get("logic/XOR.png"));
	@Override
	public RotatedImageGroup getImage() {
		return texture;
	}

}
