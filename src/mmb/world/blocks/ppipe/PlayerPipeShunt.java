/**
 * 
 */
package mmb.world.blocks.ppipe;

import javax.annotation.Nonnull;

import mmb.world.block.BlockEntry;
import mmb.world.block.BlockType;
import mmb.world.rotate.ChirotatedImageGroup;
import mmb.world.rotate.Side;
import mmb.world.worlds.MapProxy;
import mmb.world.worlds.world.WorldUtils;

/**
 * @author oskar
 *
 */
public class PlayerPipeShunt extends AbstractPlayerPipe {
	private boolean toggle;
	/**
	 * @return the toggle
	 */
	public boolean isToggle() {
		return toggle;
	}
	/**
	 * @param toggle the toggle to set
	 */
	public void setToggle(boolean toggle) {
		if(this.toggle == toggle) return;
		this.toggle = toggle;
		initConnections(posX(), posY());
	}
	
	private boolean state;

	/**
	 * Creates a player pipe which combines two pipes into one. It has connections (R → D of length 0.8) and ({@code mainDir} → D of length {@code mainLen})
	 * @param mainLen length of main branch
	 * @param mainDir direction of main branch
	 * @param typ block type
	 */
	public PlayerPipeShunt(double mainLen, Side mainDir, BlockType typ) {
		type = typ;
		main = mainDir;
		tunnelM = new TunnelHelper(mainDir, Side.D);
		tunnelM.path.length = mainLen;
		tunnelS = new TunnelHelper(Side.R, Side.D);
		tunnelS.path.length = 0.8;
	}
	@Nonnull private final BlockType type;
	@Nonnull private static final ChirotatedImageGroup img0 = ChirotatedImageGroup.create("machine/switch straight.png");
	@Nonnull private static final ChirotatedImageGroup img1 = ChirotatedImageGroup.create("machine/switch turn.png");
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
		boolean dir = toggle ^ state;
		sides.set(Side.D, dir?tunnelM.BWD:tunnelS.BWD);
		
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
		boolean dir = toggle ^ state;
		return dir?img0:img1;
	}

	@Override
	public BlockEntry blockCopy() {
		double mainLen = tunnelM.path.length;
		PlayerPipeShunt copy = new PlayerPipeShunt(mainLen, main, type);
		return copy;
	}
	
	@Override
	public void onTick(MapProxy map) {
		boolean oldState = state;
		state = WorldUtils.hasIncomingSignal(this);
		if(state != oldState) initConnections(posX(), posY());
	}

}
