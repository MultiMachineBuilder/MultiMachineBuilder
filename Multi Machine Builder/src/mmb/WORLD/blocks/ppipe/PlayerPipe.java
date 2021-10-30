/**
 * 
 */
package mmb.WORLD.blocks.ppipe;

import javax.annotation.Nonnull;

import mmb.WORLD.block.BlockType;
import mmb.WORLD.rotate.ChirotatedImageGroup;
import mmb.WORLD.rotate.Side;

/**
 * @author oskar
 *
 */
public class PlayerPipe extends AbstractPlayerPipe {
	@Nonnull private final BlockType type;
	@Nonnull private final ChirotatedImageGroup img;
	@Nonnull private final Side from;
	@Nonnull private final Side to;
	@Nonnull protected final PipeTunnel tunnel; 
	@Override
	public BlockType type() {
		return type;
	}

	/**
	 * Creates a player pipe
	 * @param type block type
	 * @param img texture
	 * @param from first side
	 * @param to second side
	 * @param length pipe length in meters
	 */
	public PlayerPipe(BlockType type, ChirotatedImageGroup img, Side from, Side to, double length) {
		super();
		this.type = type;
		this.img = img;
		this.from = from;
		this.to = to;
		tunnel = new TunnelHelper(from, to);
		tunnel.path.length = length;
	}
	
	/*
	 * from = U
	 * to = D
	 * 
	 * Find from side
	 * 
	 */

	@Override
	protected void initConnections() {
		sides.reset();
		sides.set(from, tunnel.FWD);
		Side from1 = getChirotation().apply(from);
		double ox1 = from1.sideOffsetX;
		double oy1 = from1.sideOffsetY;
		tunnel.path.beginX = ox1 + posX();
		tunnel.path.beginY = oy1 + posY();
		sides.set(to, tunnel.BWD);
		Side to1 = getChirotation().apply(to);
		double ox2 = to1.sideOffsetX;
		double oy2 = to1.sideOffsetY;
		tunnel.path.endX = ox2 + posX();
		tunnel.path.endY = oy2 + posY();
	}

	@Override
	public ChirotatedImageGroup getImage() {
		return img;
	}

}
