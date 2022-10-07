/**
 * 
 */
package mmb.world.blocks.machine;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.data.json.JsonTool;
import mmb.world.block.BlockEntry;
import mmb.world.block.SkeletalBlockEntityRotary;
import mmb.world.inventory.Inventories;
import mmb.world.inventory.Inventory;
import mmb.world.inventory.NoSuchInventory;
import mmb.world.inventory.io.InventoryReader;
import mmb.world.inventory.io.InventoryWriter;
import mmb.world.inventory.storage.SimpleInventory;
import mmb.world.rotate.Side;
import mmb.world.worlds.MapProxy;

/**
 * @author oskar
 * A machine with linear processing
 */
public abstract class SkeletalBlockLinear extends SkeletalBlockEntityRotary {
	
	@Nonnull public final SimpleInventory incoming = new SimpleInventory();
	@Nonnull protected final SimpleInventory outgoing = new SimpleInventory();
	@Nonnull public final Inventory output = outgoing.lockInsertions();
	
	@Override
	protected final void save1(ObjectNode node) {
		node.set("in", incoming.save());
		node.set("out", outgoing.save());
		save2(node);
	}

	@Override
	protected final void load1(ObjectNode node) {
		ArrayNode invin = JsonTool.requestArray("in", node);
		incoming.load(invin);
		ArrayNode invout = JsonTool.requestArray("out", node);
		outgoing.load(invout);
		load2(node);
	}
	
	/**
	 * Additional function used to save additional data
	 * @param node node, to which data can be saved
	 */
	protected void save2(ObjectNode node) {
		//optional
	}
	/**
	 * Additional function used to save additional data
	 * @param node node, to which data can be loaded
	 */
	protected void load2(ObjectNode node) {
		//optional
	}

	@Override
	public InventoryReader getOutput(Side s) {
		if(s == getRotation().U()) return output.createReader();
		return InventoryReader.NONE;
	}

	@Override
	public InventoryWriter getInput(Side s) {
		if(s == getRotation().D()) return incoming.createWriter();
		return InventoryWriter.NONE;
	}
	
	@Override
	public Inventory getInventory(Side s) {
		if(s == getRotation().U()) {
			return output;
		}
		if(s == getRotation().D()) {
			return incoming;
		}
		return NoSuchInventory.INSTANCE;
	}

	@Override
	public final void onTick(MapProxy map) {
		//Extract any output items
		BlockEntry top = map.getAtSide(posX(), posY(), getRotation().U());
		InventoryWriter writer = top.getInput(getRotation().D());
		Inventories.transferFirst(outgoing, writer);
		//Run the cycle
		cycle();
	}
	/** Runs the processing cycle */
	protected abstract void cycle();
	
}
