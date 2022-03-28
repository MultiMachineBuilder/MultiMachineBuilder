/**
 * 
 */
package mmb.WORLD.blocks.ppipe;

import javax.annotation.Nonnull;

import mmb.WORLD.block.BlockEntry;
import mmb.WORLD.block.BlockType;
import mmb.WORLD.rotate.ChirotatedImageGroup;
import mmb.WORLD.rotate.Side;

/**
 * @author oskar
 *
 */
public class JoiningPlayerPipe extends AbstractPlayerPipe {

	/**
	 * Creates a player pipe which combines two pipes into one. It has connections (R → D of length 0.8) and ({@code mainDir} → D of length {@code mainLen})
	 * @param mainLen length of main branch
	 * @param mainDir direction of main branch
	 * @param img texture
	 * @param typ block type
	 */
	public JoiningPlayerPipe(double mainLen, Side mainDir, ChirotatedImageGroup img, BlockType typ) {
		type = typ;
		main = mainDir;
		this.img = img;
		tunnelM = new TunnelHelper(mainDir, Side.D);
		tunnelM.path.length = mainLen;
		tunnelS = new TunnelHelper(Side.R, Side.D);
		tunnelS.path.length = 0.8;
	}
	@Nonnull private final BlockType type;
	@Nonnull private final ChirotatedImageGroup img;
	@Nonnull private final Side main;
	@Nonnull protected final PipeTunnel tunnelM;
	@Nonnull protected final PipeTunnel tunnelS; 
	@Override
	public BlockType type() {
		return type;
	}

	/*
	 * Chirotation: El
	 * Main side: U
	 * Down: FWD
	 */
	@Override
	protected void initConnections(int x, int y) {
		//Common
		Side dw = getChirotation().D();
		double oxc = dw.sideOffsetX;
		double oyc = dw.sideOffsetY;
		
		//Main
		sides.set(main, tunnelM.FWD);
		Side art = getChirotation().apply(main);
		double oxm = art.sideOffsetX;
		double oym = art.sideOffsetY;
		tunnelM.path.beginX = oxm + x;
		tunnelM.path.beginY = oym + y;
		tunnelM.path.endX = oxc + x;
		tunnelM.path.endY = oyc + y;
		
		//Side
		sides.set(Side.R, tunnelS.FWD);
		Side rw = getChirotation().R();
		double oxs = rw.sideOffsetX;
		double oys = rw.sideOffsetY;
		tunnelS.path.beginX = oxs + x;
		tunnelS.path.beginY = oys + y;
		tunnelS.path.endX = oxc + x;
		tunnelS.path.endY = oyc + y;
	}

	@Override
	public ChirotatedImageGroup getImage() {
		return img;
	}

	@Override
	public BlockEntry blockCopy() {
		double mainLen = tunnelM.path.length;
		JoiningPlayerPipe copy = new JoiningPlayerPipe(mainLen, main, img, type);
		return copy;
	}

}
