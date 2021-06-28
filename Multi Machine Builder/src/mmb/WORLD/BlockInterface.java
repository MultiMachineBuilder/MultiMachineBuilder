/**
 * 
 */
package mmb.WORLD;

import mmb.WORLD.inventory.io.InventoryWriter;

/**
 * @author oskar
 * A class which offers bindings to a block state.
 * This class should be overridden to implement custom behavior
 */
public class BlockInterface {
	/**
	 * Represents a block interface without any I/O capabilities
	 */
	public static final BlockInterface NONE = new BlockInterface();
	protected BlockInterface() {}
	/**
	 * Provides the boolean signal
	 * @return
	 */
	@SuppressWarnings("static-method")
	public boolean boolSignal() {
		return false;
	}
	
	/**
	 * @return
	 */
	@SuppressWarnings("static-method")
	public InventoryWriter getItemIn() {
		return InventoryWriter.NONE;
	}
}
