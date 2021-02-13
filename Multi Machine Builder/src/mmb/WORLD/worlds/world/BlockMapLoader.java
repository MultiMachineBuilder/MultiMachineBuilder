/**
 * 
 */
package mmb.WORLD.worlds.world;

import java.util.Iterator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.BEANS.LazyLoader;
import mmb.DATA.json.JsonTool;
import mmb.WORLD.block.BlockLoader;
import mmb.WORLD.machine.Machine;
import mmb.WORLD.machine.MachineModel;
import mmb.WORLD.worlds.DataLayers;
import mmb.WORLD.worlds.map.BlockMap;
import mmb.WORLD.worlds.map.MapDataLayer;
import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class BlockMapLoader implements LazyLoader<ObjectNode, BlockMap> {
	private String name;
	private BlockMap loaded;
	/**
	 * Loads world data from given ObjectNode.
	 * The map's name must be set beforehand with setName();
	 */
	private final Debugger debug = new Debugger("BLOCK MAP LOADER");
	@Override
	public void load(ObjectNode data) {
		//Dimensions
		int sizeX = data.get("sizeX").asInt();
		int sizeY = data.get("sizeY").asInt();
		int startX = data.get("startX").asInt();
		int startY = data.get("startY").asInt();
		int endX = sizeX + startX;
		int endY = sizeY + startY;
		
		//Prepare the map
		loaded = new BlockMap(sizeX, sizeY, startX, startY);
		loaded.setName(name);
		
		//Blocks
		ArrayNode worldArray = (ArrayNode) data.get("world");
		BlockLoader bloader = new BlockLoader();
		bloader.map = loaded;
		// deepcode ignore UsingEmptyCollection: must be correct size		Iterator<JsonNode> iter = worldArray.elements();
		for(int y = startY; y < endY; y++) {
			for(int x = startX; x < endX; x++) {
				JsonNode node = iter.next();
				bloader.x = x;
				bloader.y = y;
				bloader.load(node);
				loaded.set(x, y, bloader.getLoaded());
			}
		}
		
		//Machines
		ArrayNode machines = (ArrayNode) data.get("machines");
		for(JsonNode machineNode: machines) {
			ArrayNode aMachine = (ArrayNode) machineNode;
			String type = aMachine.get(0).asText();
			int x = aMachine.get(1).asInt();
			int y = aMachine.get(2).asInt();
			JsonNode machineData  = aMachine.get(3);
			try {
				Machine machine = MachineModel.forID(type).initialize(endX, endY, machineData);
				loaded.placeMachine(machine);
			} catch(Exception e) {
				debug.pstm(e, "Failed to place a machine of type "+type+" at ["+x+","+y+"]");
			}
		}
		
		//Data layers
		ObjectNode dataNode = (ObjectNode) data.get("data");
		loaded.data.addAll(DataLayers.createAllMapDataLayers());
		for(MapDataLayer dl: loaded.data) {
			JsonNode datalayerData = dataNode.get(dl.identifier());
			try {
				if(!datalayerData.isMissingNode()) dl.load(datalayerData);
				dl.afterMapLoaded(loaded);
			} catch (Exception e) {
				debug.pstm(e, "Failed to load data layer "+dl.title());
			}
		}
	}
	/**
	 * Set given {@link BlockMap}'s name
	 * @param name name
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the loaded {@link BlockMap}
	 */
	@Override
	public BlockMap getLoaded() {
		return loaded;
	}

}
