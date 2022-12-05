/**
 * 
 */
package mmbgame.ppipe;

import javax.annotation.Nonnull;

import mmbeng.block.BlockEntry;
import mmbeng.block.BlockType;
import mmbeng.rotate.ChirotatedImageGroup;
import mmbeng.rotate.Side;

/**
 * @author oskar
 *
 */
public class TwinPlayerPipe extends AbstractPlayerPipe {
	/**
	 * Creates a twin player pipe
	 * @param type block type
	 * @param img texture
	 * @param from1 first side of first tunnel
	 * @param to1 second side of first tunnel
	 * @param from2 first side of second tunnel
	 * @param to2 second side of second tunnel
	 * @param length pipe length in meters
	 */
	public TwinPlayerPipe(BlockType type, ChirotatedImageGroup img, Side from1, Side to1, Side from2, Side to2, double length) {
		super();
		this.type = type;
		this.img = img;
			this.from1 = from1;
			this.to1 = to1;
			this.tunnel1 = new TunnelHelper(from1, to1);
			tunnel1.path.length = length;
		this.from2 = from2;
		this.to2 = to2;
		this.tunnel2 = new TunnelHelper(from2, to2);
		tunnel2.path.length = length;
	}

	@Nonnull private final BlockType type;
	@Nonnull private final ChirotatedImageGroup img;
	@Nonnull private final Side from1;
	@Nonnull private final Side to1;
	@Nonnull protected final PipeTunnel tunnel1;
	@Nonnull private final Side from2;
	@Nonnull private final Side to2;
	@Nonnull protected final PipeTunnel tunnel2; 
	@Override
	public BlockType type() {
		return type;
	}

	@Override
	protected void initConnections(int x, int y) {
		sides.reset();
		
		sides.set(from1, tunnel1.FWD);
		Side from3 = getChirotation().apply(from1);
		double ox1 = from3.sideOffsetX;
		double oy1 = from3.sideOffsetY;
		tunnel1.path.beginX = ox1 + x;
		tunnel1.path.beginY = oy1 + y;
			sides.set(to1, tunnel1.BWD);
			Side to3 = getChirotation().apply(to1);
			double ox2 = to3.sideOffsetX;
			double oy2 = to3.sideOffsetY;
			tunnel1.path.endX = ox2 + x;
			tunnel1.path.endY = oy2 + y;
		
		sides.set(from2, tunnel2.FWD);
		Side from4 = getChirotation().apply(from2);
		double ox3 = from4.sideOffsetX;
		double oy3 = from4.sideOffsetY;
		tunnel2.path.beginX = ox3 + x;
		tunnel2.path.beginY = oy3 + y;
			sides.set(to2, tunnel2.BWD);
			Side to4 = getChirotation().apply(to2);
			double ox4 = to4.sideOffsetX;
			double oy4 = to4.sideOffsetY;
			tunnel2.path.endX = ox4 + x;
			tunnel2.path.endY = oy4 + y;
	}

	@Override
	public ChirotatedImageGroup getImage() {
		return img;
	}

	@Override
	public BlockEntry blockCopy() {
		TwinPlayerPipe copy = new TwinPlayerPipe(type, img, from1, to1, from2, to2, tunnel1.path.length);
		return copy;
	}

}
