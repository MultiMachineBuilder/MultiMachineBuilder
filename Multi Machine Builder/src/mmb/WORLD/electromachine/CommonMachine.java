/**
 * 
 */
package mmb.WORLD.electromachine;

import javax.annotation.Nonnull;

import mmb.BEANS.BlockActivateListener;
import mmb.WORLD.block.BlockEntry;
import mmb.WORLD.block.SkeletalBlockEntityRotary;
import mmb.WORLD.contentgen.ElectricMachineGroup.ElectroMachineType;
import mmb.WORLD.electric.Battery;
import mmb.WORLD.electric.VoltageTier;
import mmb.WORLD.inventory.Inventory;
import mmb.WORLD.inventory.storage.SimpleInventory;

/**
 * @author oskar
 *
 */
public abstract class CommonMachine extends SkeletalBlockEntityRotary implements ElectroMachine{

	@Nonnull
	public final SimpleInventory in = new SimpleInventory();
	@Nonnull
	protected final SimpleInventory out0 = new SimpleInventory();
	@Nonnull
	public final Inventory out = out0.lockInsertions();
	@Nonnull
	protected final Battery elec;
	@Nonnull
	protected final ElectroMachineType type;
	protected boolean pass;
	protected boolean autoExtract;

	/**
	 * 
	 */
	public CommonMachine(ElectroMachineType type) {
		super();
		elec = new Battery(20_000, 40_000, this, type.volt);
		this.type = type;
	}

	/**
	 * @return the pass
	 */
	@Override
	public boolean isPass() {
		return pass;
	}

	/**
	 * @param pass the pass to set
	 */
	@Override
	public void setPass(boolean pass) {
		this.pass = pass;
	}

	/**
	 * @return the autoExtract
	 */
	@Override
	public boolean isAutoExtract() {
		return autoExtract;
	}

	/**
	 * @param autoExtract the autoExtract to set
	 */
	@Override
	public void setAutoExtract(boolean autoExtract) {
		this.autoExtract = autoExtract;
	}

	/**
	 * Performs partial copy of this machine. The copies items may include recipe helper and catalyst.
	 * For implementers: Do not copy input, output, pass on, auto-extract and battery., as copying these is redundant
	 * @return
	 */
	@Nonnull abstract protected CommonMachine copy0();

	@Override
	public final BlockEntry blockCopy() {
		CommonMachine copy = copy0();
		copy.autoExtract = autoExtract;
		copy.pass = pass;
		copy.in.set(in);
		copy.out0.set(out0);
		copy.elec.set(elec);
		return copy;
	}

	@Override
	public ElectroMachineType type() {
		return type;
	}

	@Override
	public VoltageTier voltage() {
		return elec.voltage;
	}

	@Override
	public Inventory input() {
		return in;
	}

	@Override
	public Inventory output() {
		return out;
	}

	@Override
	public Battery energy() {
		return elec;
	}

	
	
}