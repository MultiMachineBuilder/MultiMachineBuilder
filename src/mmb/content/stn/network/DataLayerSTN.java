/**
 * 
 */
package mmb.content.stn.network;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.NN;
import mmb.Nil;
import mmb.content.stn.block.STNBaseMachine;
import mmb.content.stn.network.STNNetworkProcessing.STNRGroupTag;
import mmb.engine.UnitFormatter;
import mmb.engine.debug.Debugger;
import mmb.engine.inv.Inventory;
import mmb.engine.json.JsonTool;
import mmb.engine.worlds.world.DataLayer;
import mmb.engine.worlds.world.World;

/**
 * The brains of Simple Transportation Network
 * @author oskar
 */
public class DataLayerSTN extends DataLayer<@NN World> {
	@NN private final Debugger debug;

	/**
	 * Creates a Simple Transportation Network
	 * @param world
	 */
	public DataLayerSTN(World world) {
		super(world);
		debug = new Debugger("STN data layer @"+world.getName());
	}

	@Override
	public @NN JsonNode save() {
		ObjectNode node = JsonTool.newObjectNode();
		
		//Save the flush queue (if it failed to flush)
		node.set("queue", inv.storageQueue.save());
		
		//Save the processing capabilities
		node.set("processing", processor.save());
		
		return node;
	}

	@Override
	public void load(@Nil JsonNode data) {
		if(data == null) return;
		//Load the flush queue
		JsonNode queueNode = data.get("queue");
		inv.storageQueue.load(queueNode);
		inv.storageQueue.setCapacity(128);
		
		//Load the processing capabilities
		JsonNode procNode = data.get("processing");
		processor.load(procNode);
	}

	//EMBARK/DISEMBARK
	/**
	 * Embarks the machine (when machine is loaded or placed)
	 * @param m machine to embark
	 * @param x X coordinate of a machine
	 * @param y Y coordinate of a machine
	 */
	public void embark(STNBaseMachine m, int x, int y) {
		debug.printl("Embarkment: "+UnitFormatter.formatPoint(x, y));
		revalidate(m);
	}
	/**
	 * Disembarks the machine (when machine is demolished)
	 * @param m machine to embark
	 * @param x X coordinate of a machine
	 * @param y Y coordinate of a machine
	 */
	public void disembark(STNBaseMachine m, int x, int y) {
		debug.printl("Disembarkment: "+UnitFormatter.formatPoint(x, y));
		inv.removeInv(m.storage());
		inv.removeInv(m.oldstorage());
	}
	/**
	 * Re-validates the machine (when a machine changes)
	 * @param m machine to validate
	 */
	public void revalidate(STNBaseMachine m) {
		STNRGroupTag oldTag = m.oldrecipes();
		STNRGroupTag newTag = m.recipes();
		
		if(oldTag != newTag) {
			processor.theProcessingMachineIsGone(m);
		}
		
		//Re-validate the storage
		Inventory oldInv = m.oldstorage();
		Inventory newInv = m.storage();
		if(oldInv != newInv) {
			inv.removeInv(oldInv);
			inv.addInv(newInv);
		}
	}
	
	//STORAGE
	/** The storage capabilities of this network, expressed as an inventory */
	@NN public final STNNetworkInventory inv = new STNNetworkInventory(this);
	
	//TODO CRAFTING
	/** The processing capabilities of this network */
	@NN public final STNNetworkProcessing processor = new STNNetworkProcessing(this);

	@Override
	public void shutdown() {
		//Clean up
		inv.flushQueue();
	}
	
	private int cycler = 0;
	private static final int SECOND = 50;
	private static final int MINUTE = 3000;

	@Override
	public void cycle() {
		cycler++;
		
		if(cycler % SECOND == 0) {
			//Every second
			inv.flushQueue();
		}
		if(cycler % MINUTE == 0) {
			//Every minute
			debug.printl("Rebuild");
			inv.rebuild();
		}
	}
}
