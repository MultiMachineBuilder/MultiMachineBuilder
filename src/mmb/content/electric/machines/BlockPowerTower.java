/**
 * 
 */
package mmb.content.electric.machines;

import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.NN;
import mmb.Nil;
import mmb.cgui.BlockActivateListener;
import mmb.content.aim.Aimable;
import mmb.content.electric.Electric;
import mmb.content.electric.Electricity;
import mmb.content.electric.ElectricMachineGroup.ElectroMachineType;
import mmb.engine.block.BlockEntityRotary;
import mmb.engine.block.BlockEntry;
import mmb.engine.block.BlockType;
import mmb.engine.rotate.RotatedImageGroup;
import mmb.engine.rotate.Side;
import mmb.engine.worlds.MapProxy;
import mmb.engine.worlds.world.World;
import mmb.menu.world.window.WorldWindow;

/**
 * A block for wireless transmission of power
 * @author oskar
 */
public class BlockPowerTower extends BlockEntityRotary implements Electric, BlockActivateListener, Aimable{
	/** The block type*/
	@NN public final ElectroMachineType type;
	/** The electrical connection*/
	@NN public final Electricity elec;
	
	/**
	 * Creates a power tower
	 * @param type block type
	 */
	public BlockPowerTower(ElectroMachineType type) {
		this.type = type;
		elec = Electricity.limitCurrent(Electricity.circuitBreaker(Electricity.dynamicElectricity(this::obtain), type.volt, this), 100 * type.powermul);
	}
	@Override
	public BlockType type() {
		return type;
	}

	@Override
	public BlockEntry blockCopy() {
		return new BlockPowerTower(type);
	}
	public static final RotatedImageGroup rig = RotatedImageGroup.create("machine/ptower.png");
	@Override
	public RotatedImageGroup getImage() {
		return rig;
	}
	@Override
	public Electricity getElectricalConnection(Side s) {
		if(s == getRotation().D()) return elec;
		return null;
	}
	@Override
	public void click(int blockX, int blockY, World map, @Nil WorldWindow window, double partX, double partY) {
		if(window == null) return;
		
	}
	
	//electricity
	@Override
	public Electricity getElectricity() {
		return elec;
	}
	
	private Electricity obtainedLastTime;
	//obtains raw electric connection
	private Electricity obtain() {
		if(obtainedLastTime != null) return obtainedLastTime; //check the cache
		BlockEntry block = owner().get(tgtX, tgtY);
		Electricity elec0 = null;
		if(block instanceof Electric) elec0 = ((Electric) block).getElectricity();
		obtainedLastTime = elec0;
		if(obtainedLastTime == null) obtainedLastTime = Electricity.NONE;
		return obtainedLastTime;
	}
	@Override
	public void onTick(MapProxy map) {
		obtainedLastTime = null;
	}
	
	//Aiming
	/** X coordinate of target block */
	public int tgtX = -999999;
	/** Y coordinate of target block */
	public int tgtY = -999999;
	@Override
	public int aimX() {
		return tgtX;
	}
	@Override
	public int aimY() {
		return tgtY;
	}
	@Override
	public void aimX(int x) {
		tgtX = x;
	}
	@Override
	public void aimY(int y) {
		tgtY = y;
	}
	
	//Save/load
	@Override
	protected void save1(ObjectNode node) {
		node.put("tgtX", tgtX);
		node.put("tgtY", tgtY);
	}
	@Override
	protected void load1(ObjectNode node) {
		tgtX = node.get("tgtX").asInt(-999999);
		tgtY = node.get("tgtY").asInt(-999999);
	}

}
