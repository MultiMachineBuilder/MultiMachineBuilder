/**
 * 
 */
package mmb.WORLD.electromachine;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.BEANS.Aimable;
import mmb.BEANS.BlockActivateListener;
import mmb.BEANS.Electric;
import mmb.WORLD.block.BlockEntry;
import mmb.WORLD.block.BlockType;
import mmb.WORLD.block.SkeletalBlockEntityRotary;
import mmb.WORLD.contentgen.ElectricMachineGroup.ElectroMachineType;
import mmb.WORLD.electric.Electricity;
import mmb.WORLD.gui.window.WorldWindow;
import mmb.WORLD.rotate.RotatedImageGroup;
import mmb.WORLD.rotate.Side;
import mmb.WORLD.worlds.MapProxy;
import mmb.WORLD.worlds.world.World;

/**
 * A block for wireless transmission of power
 * @author oskar
 */
public class BlockPowerTower extends SkeletalBlockEntityRotary implements Electric, BlockActivateListener, Aimable{
	/** The block type*/
	@Nonnull public final ElectroMachineType type;
	/** The electrical connection*/
	@Nonnull public final Electricity elec;
	
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
	public void click(int blockX, int blockY, World map, @Nullable WorldWindow window, double partX, double partY) {
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
