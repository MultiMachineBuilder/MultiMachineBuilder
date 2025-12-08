/**
 * 
 */
package mmb.content.ppipe;

import mmb.annotations.NN;
import mmb.engine.block.BlockEntry;
import mmb.engine.block.BlockType;
import mmb.engine.rotate.ChirotatedImageGroup;
import mmb.engine.rotate.Side;

/**
 * Carries players to some location
 * @author oskar
 */
public class PlayerPipe extends AbstractPlayerPipe {
	//Constructors
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
	protected void initConnections(int x, int y) {
		sides.reset();
		sides.set(from, tunnel.FWD);
		Side from1 = getChirotation().apply(from);
		double ox1 = from1.sideOffsetX;
		double oy1 = from1.sideOffsetY;
		tunnel.path.beginX = ox1 + x;
		tunnel.path.beginY = oy1 + y;
		sides.set(to, tunnel.BWD);
		Side to1 = getChirotation().apply(to);
		double ox2 = to1.sideOffsetX;
		double oy2 = to1.sideOffsetY;
		tunnel.path.endX = ox2 + x;
		tunnel.path.endY = oy2 + y;
	}
	
	//Contents
	@NN private final Side from;
	@NN private final Side to;
	@NN protected final PipeTunnel tunnel; 
	
	//Block methods	
	@NN private final BlockType type;
	@Override
	public BlockType type() {
		return type;
	}
	@NN private final ChirotatedImageGroup img;
	@Override
	public ChirotatedImageGroup getImage() {
		return img;
	}
	@Override
	public BlockEntry blockCopy() {
		PlayerPipe copy = new PlayerPipe(type, img, from, to, tunnel.path.length);
		return copy;
	}

}
