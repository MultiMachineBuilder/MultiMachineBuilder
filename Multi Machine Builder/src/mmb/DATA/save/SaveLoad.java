/**
 * 
 */
package mmb.DATA.save;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import mmb.DATA.json.JsonTool;
import mmb.WORLD.tileworld.block.Block;
import mmb.WORLD.tileworld.block.BlockEntity;
import mmb.WORLD.tileworld.block.BlockEntityData;
import mmb.WORLD.tileworld.block.Blocks;
import mmb.WORLD.tileworld.block.MapEntry;
import mmb.WORLD.tileworld.map.TileMap;
import mmb.WORLD.tileworld.map.World;
import mmb.debug.Debugger;

/**
 * @author oskar
 * This class contains methods to save and load worlds.
 * 
 */
public class SaveLoad {
	private static final Debugger debug = new Debugger("JSON-WORLD LOADER");

	public static World load(String data) {
		//debug.printl(data);
		JsonParser parser = new JsonParser();
		JsonObject tree = parser.parse(data).getAsJsonObject();
		
		//Map related information
		int startX = tree.get("startX").getAsInt();
		int startY = tree.get("startY").getAsInt();
		int sizeX = tree.get("sizeX").getAsInt();
		int sizeY = tree.get("sizeY").getAsInt();
		JsonArray blockarr = tree.get("world").getAsJsonArray();
		
		//Data layers
		
		debug.printl("Loading data layers");
		JsonElement data0 = tree.get("data");
		List<DataLayer> dl;
		if(data0 == null) {
			dl = loadDLs(null);
		}else {
			JsonObject information = tree.get("data").getAsJsonObject();
			dl = loadDLs(information);
		}
		
		debug.printl("Successfully loaded data layers");
		
		//World information
		debug.printl("Loading world");
		TileMap blocks = loadMap(startX, startY, sizeX, sizeY, blockarr);
		debug.printl("Successfully loaded world");
		
		// v0.3 block data handler is in loadMap()
		
		return new World(blocks, dl);
	}
	public static TileMap loadMap(int startX, int startY, int sizeX, int sizeY, JsonArray arr) {
		int size = sizeX * sizeY;
		//debug.print(Blocks.blocks.get(arr.get(0).getAsString()).toString());
		TileMap data = new TileMap(startX, startY, sizeX, sizeY);
		for(int i = 0; i < size; i++) {
			JsonElement element = arr.get(i);
			MapEntry me;
			if(element.isJsonObject()) {
				//block with data
				JsonObject jo = element.getAsJsonObject();
				String name = jo.get("blocktype").getAsString();
				Block value = Blocks.blocks.get(name);
				me = value.load(jo);
			}else {
				//block without data
				String name = element.getAsString();
				Block value = Blocks.blocks.get(name);
				if(value == null) {
					value = Blocks.air;
					Point p =data.indexToWrldCoords(i);
					debug.printl(" [WRN] The block "+name+" at "+p.x+","+p.y+" was not recognized and was replaced by air");
				}
				me = MapEntry.createNew(value);
			}
			
			data.blocks[i] = me;
		}
		return data;
	}
	public static List<DataLayer> loadDLs(JsonObject o) {
		List<String> missingEntries = new ArrayList<String>();
		List<DataLayer> result = new ArrayList<DataLayer>();
		//Pre-initialize the missing entry list
		DataLayer.layers.forEach((name, dle) -> {
			debug.printl("Adding missing data layer: "+name);
			missingEntries.add(name);
		});
		
		if(o != null) {
			//Create the pre-defined entries
			Set<Entry<String, JsonElement>> contents = o.entrySet();
			contents.forEach((entry) -> {
				debug.print("Loading data layer: ");
				debug.printl(entry.getKey());
				DataLayer loaded = DataLayer.createDL(entry.getKey(), entry.getValue().getAsJsonObject());
				debug.print("Successfully loaded data layer: ");
				debug.printl(entry.getKey());
				result.add(loaded);
				//Remove given entry from the missing entry list
				missingEntries.remove(entry.getKey());
			});
		}
		
		//Add missing entries
		missingEntries.forEach((ent) -> {
			debug.print("Adding missing entry: ");
			debug.printl(ent);
			DataLayer loaded = DataLayer.createDL(ent);
			debug.print("Successfully addied missing entry: ");
			debug.printl(ent);
			result.add(loaded);
		});
		return result;
	}
	
	
	//Saving methods
	public static String save(World data) {
		//Prepare
		JsonObject tree = new JsonObject();
		
		//Save dimensions
		debug.printl("Saving dimensions");
		tree.add("sizeX", new JsonPrimitive(data.blocks.sizeX));
		tree.add("sizeY", new JsonPrimitive(data.blocks.sizeY));
		tree.add("startX", new JsonPrimitive(data.blocks.startX));
		tree.add("startY", new JsonPrimitive(data.blocks.startY));
		
		//Save blocks
		debug.printl("Saving blocks");
		MapEntry[] dat = data.blocks.blocks;
		JsonArray target = new JsonArray(dat.length);
		for(int i = 0; i <dat.length; i++) {
			MapEntry ent = dat[i];
			if(ent instanceof BlockEntity) {
				//save as block entity
				BlockEntity ent2 = (BlockEntity) ent;
				BlockEntityData bed = ent2.bed;
				Block b =  ent2.type;
				JsonObject jo = b.bel.saver.apply(bed);
				jo.add("blocktype", new JsonPrimitive(saveBlock(ent.getType())));
				target.add(jo);
			}else {
				//save as block
				target.add(new JsonPrimitive(saveBlock(ent.getType())));
			}
			
		}
		tree.add("world", target);
		
		//Save data layers
		debug.printl("Saving data layers");
		JsonObject datalayers = new JsonObject();
		data.data.forEach((datalayer) -> {
			debug.print("Saving data layer: ");
			debug.printl(datalayer.name());
			datalayers.add(datalayer.name(), datalayer.save());
			debug.print("Saved data layer: ");
			debug.printl(datalayer.name());
		});
		tree.add("data", datalayers);
		
		//Return
		return JsonTool.gson.toJson(tree);
	}
	
	public static String saveBlock(Block b) {
		if(b == null) {
			debug.printl("block is null");
			return "null";
		}
		if(b.getID() == null) {
			debug.printl("block name is null");
			return "null";
		}
		return b.getBlock().getID();
	}

}
