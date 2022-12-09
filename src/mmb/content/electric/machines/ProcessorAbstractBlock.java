/**
 * 
 */
package mmb.content.electric.machines;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import mmb.content.electric.Battery;
import mmb.content.electric.Electricity;
import mmb.content.electric.GUIMachine;
import mmb.content.electric.VoltageTier;
import mmb.content.electric.ElectricMachineGroup.ElectroMachineType;
import mmb.engine.block.BlockEntityRotary;
import mmb.engine.block.BlockEntry;
import mmb.engine.inv.Inventories;
import mmb.engine.inv.Inventory;
import mmb.engine.inv.io.InventoryWriter;
import mmb.engine.inv.storage.SimpleInventory;
import mmb.engine.inv.storage.SingleItemInventory;
import mmb.engine.worlds.MapProxy;

/**
 * @author oskar
 * A class with many common utilities for machines
 */
public abstract class ProcessorAbstractBlock extends BlockEntityRotary implements ElectroMachine{

	/** The input inventory */
	@Nonnull public final SimpleInventory in = new SimpleInventory();
	/** Internal output inventory for recipe processors */
	@Nonnull protected final SimpleInventory out0 = new SimpleInventory();
	/** The output inventory */
	@Nonnull public final Inventory out = out0.lockInsertions();
	/** The electrical buffer */
	@Nonnull public final Battery elec;
	/** The block type */
	@Nonnull protected final ElectroMachineType type;
			 protected boolean pass;
			 protected boolean autoExtract;
			 protected GUIMachine tab;
	
	@SuppressWarnings("javadoc") //internal use only
	public void close(GUIMachine tbb) {
		if(tab == tbb) tab = null;
	}

	/**
	 * Constructs base components of a machine
	 */
	protected ProcessorAbstractBlock(ElectroMachineType type) {
		super();
		elec = new Battery(200, 400, this, type.volt);
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
	 * @apiNote This method may be overridden if catalysts are supported
	 * @return the item catalyst inventory, or {@code null} if catalysts are unsupported
	 */
	@Nullable public SingleItemInventory catalyst() {
		return null;
	}

	/**
	 * Performs partial copy of this machine. The copies items may include recipe helper and catalyst.
	 * For implementers: Do not copy input, output, pass on, auto-extract and battery., as copying these is redundant
	 * @return the partial copy
	 */
	@Nonnull protected abstract ProcessorAbstractBlock copy0();

	@Override
	public final BlockEntry blockCopy() {
		ProcessorAbstractBlock copy = copy0();
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

	@Override
	public final void onTick(MapProxy proxy) {
		elec.capacity = 400;
		elec.maxPower = 200;
		if(autoExtract) {
			InventoryWriter writer = getAtSide(getRotation().R()).getInput(getRotation().L());
			Inventories.transfer(out, writer);
		}
		//Pass on items
		if(pass && Math.random() < 0.1) { //10% chance to pass on unsupported items
			//Pass on items
			Inventories.transfer(in, out0, r -> !recipes().supportedItems().contains(r.item()));
			if(tab != null) {
				tab.refreshInputs();
				tab.refreshOutputs();
			}
		}
		Electricity.equatePPs(this, proxy, elec, 0.9);
		onTick0(proxy);
	}
		
	/**
	 * This method may be overridden to run something on a tick
	 * @param proxy map proxy
	 */
	protected void onTick0(MapProxy proxy) {
		//overridable
	}
}