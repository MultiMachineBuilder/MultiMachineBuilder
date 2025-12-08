/**
 * 
 */
package mmb.content.machinemics.line;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.annotations.NN;
import mmb.engine.block.BlockEntityRotary;
import mmb.engine.block.BlockEntry;
import mmb.engine.inv.Inventories;
import mmb.engine.inv.Inventory;
import mmb.engine.inv.NoSuchInventory;
import mmb.engine.inv.io.InventoryReader;
import mmb.engine.inv.io.InventoryWriter;
import mmb.engine.inv.storage.SimpleInventory;
import mmb.engine.json.JsonTool;
import mmb.engine.rotate.Side;
import mmb.engine.worlds.MapProxy;

/**
 * @author oskar
 * A machine with linear processing
 */
public abstract class SkeletalBlockLinear extends BlockEntityRotary {
	
	@NN public final SimpleInventory incoming = new SimpleInventory();
	@NN protected final SimpleInventory outgoing = new SimpleInventory();
	@NN public final Inventory output = outgoing.lockInsertions();
	
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
