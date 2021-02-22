/**
 * 
 */
package mmb.WORLD.blocks.gates;

import mmb.DATA.contents.texture.Textures;
import mmb.WORLD.RotatedImageGroup;
import mmb.WORLD.block.BlockType;
import mmb.WORLD.blocks.ContentsBlocks;
import mmb.WORLD.worlds.map.BlockMap;

/**
 * @author oskar
 *
 */
public class YESGate extends AbstractUnaryGateBase {
	public YESGate(int x, int y, BlockMap owner2) {
		super(x, y, owner2);
	}

	@Override
	public BlockType type() {
		return ContentsBlocks.YES;
	}

	private static final RotatedImageGroup texture = RotatedImageGroup.create(Textures.get("logic/YES.png"));
	@Override
	public RotatedImageGroup getImage() {
		return texture;
	}

	@Override
	protected boolean run(boolean a) {
		return a;
	}

}
