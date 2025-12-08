/**
 * 
 */
package mmb.content.ppipe;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.annotations.NN;
import mmb.engine.block.BlockEntry;
import mmb.engine.block.BlockType;
import mmb.engine.rotate.ChirotatedImageGroup;
import mmb.engine.rotate.Side;
import mmb.engine.worlds.MapProxy;
import mmb.engine.worlds.world.WorldUtils;

/**
 * Diverts the player to a player pipe
 * @author oskar
 *
 */
public class PlayerPipeShunt extends AbstractPlayerPipe {
	//Constructors
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
	
	//Contents
	private boolean toggle;
	/** @return the toggle */
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
	
	@NN private final Side main;
	@NN protected final PipeTunnel tunnelM;
	@NN protected final PipeTunnel tunnelS; 
	
	//Block methods
	@NN private final BlockType type;
	@Override
	public BlockType type() {
		return type;
	}
	@NN private static final ChirotatedImageGroup img0 = ChirotatedImageGroup.create("machine/switch straight.png");
	@NN private static final ChirotatedImageGroup img1 = ChirotatedImageGroup.create("machine/switch turn.png");
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
	
	//Serialization
	@Override
	protected void save1(ObjectNode node) {
		node.put("toggle", toggle);
	}
	@Override
	protected void load1(ObjectNode node) {
		JsonNode value = node.get("toggle");
		if(value != null) toggle = value.asBoolean();
	}
}
