/**
 * 
 */
package mmb.RUNTIME.actions;

import mmb.DATA.file.AdvancedFile;
import mmb.WORLD_new.worlds.world.World;

/**
 * @author oskar
 *	This class describes a server-side world behavior run for every tick.
 *	World behavior methods:
 *	open(World w) - called after creation
 *	run() - run on every tick
 *	close() - before world is removed
 *
 *	void loadData(AdavncedFile f, String id) - called to load mod data
 *  void saveData(AdvancedFile f, String id) - called to save mod data
 *  void createNewData(String id) - create new data if file couldn't be loaded
 *  String[] requestedData() - a list of requested file paths
 */
public interface WorldBehavior {
	/**
	 * This method is called before
	 * @param w
	 */
	public void open(World w);
	/**
	 * Run the world behavior
	 * @param w targeted world
	 */
	default public void run(World w) {}
	/**
	 * Close and clean up after world is closed.
	 * @param w
	 */
	public void close(World w);
	
	/**
	 * Return an indentifier used as object name in the serializer.
	 * @return
	 */
	public String id();
	
	/**
	 * Implement this method to receive any saved data from the world.
	 * This method is used for deserialization of data submitted by remote clients.
	 * @param f the file containing loaded data
	 * @param id world file path
	 */
	default void loadData(AdvancedFile f, String id) {}
	
	/**
	 * Implement this method to serialize data for further use.
	 * This method is used for serialization of data to be sent to clients.
	 * @param f the file to have data saved
	 * @param id world file path
	 */
	default void saveData(AdvancedFile f, String id) {}
	/**
	 * Implement this method to create new world data if previous load failed or data wasn't created previously.
	 * This method is used in world creation
	 * @param id world file path
	 */
	default void createNewData(String id) {}
	
	/**
	 * Return a string array containing requested resources.
	 * @return string array with requested resources
	 */
	default String[] requestedData() {return new String[0];}
}
