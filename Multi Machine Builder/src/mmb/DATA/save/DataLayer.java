/**
 * 
 */
package mmb.DATA.save;

import com.google.gson.JsonObject;

import mmb.WORLD.player.DataLayerPlayer;
import mmb.WORLD.tileworld.world.BlockProxy;
import mmb.debug.Debugger;

import java.util.Hashtable;
import java.util.Map;
import java.util.function.*;

/**
 * @author oskar
 *
 */
public interface DataLayer {
	/**
	 * Stores data layers.
	 */
    Map<String, DataLayerEntry> layers = new Hashtable<String, DataLayerEntry>();
    
    static Debugger debug = new Debugger("Data Layers");
	
	/**
	 * A class with fields used to create data layers in {@code createDL(String, JsonObject)} and {@code createDL(String)}
	 * @author oskar
	 */
    class DataLayerEntry {
		public Function<JsonObject, DataLayer> load;
		public Supplier<DataLayer> create;
		protected DataLayerEntry() {}
	}
	/**
	 * Register data layer within loading system.
	 * @param id data layer name
	 * @param loader function to load data
	 * @param createNew function to create new data layers
	 */
	static void register(String id, Function<JsonObject, DataLayer> loader, Supplier<DataLayer> createNew) {
		if(loader == null) debug.printl("Loader for "+id+" is null");
		if(createNew == null) debug.printl("Creator for "+id+" is null");
		DataLayerEntry dle = new DataLayerEntry();
		dle.create = createNew;
		dle.load = loader;
		layers.put(id, dle);
	}
	/**
	 * Loads a data layer from JSON object
	 * @param id layer name
	 * @param array data to load
	 * @return newly created data layer
	 */
	static DataLayer createDL(String id, JsonObject array) {
		return layers.get(id).load.apply(array);
	}
	
	/**
	 * Creates an empty data layer
	 * @param id layer name
	 * @return newly created data layer
	 */
	static DataLayer createDL(String id) {
		return layers.get(id).create.get();
	}
	
	public static void load() {
		register("PlayerInv", DataLayerPlayer::load, DataLayerPlayer::empty);
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * Gets a data layer with given type and name
	 * @throws ClassCastException if retrieved data layer is of wrong type
	 * @param <T> type of data layer
	 * @param name name of data layer
	 * @return retrieved data layer casted to correct type
	 */
	public static <T extends DataLayer> T getDL(String name){
		return (T) layers.get(name);
	}
	//Instance methods
	/**
	 * Convert given data layer into saveable form
	 * @return data array
	 */
    public JsonObject save();
	public String name();
	public void setProxy(BlockProxy p);
}
