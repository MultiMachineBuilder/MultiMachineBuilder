/**
 * 
 */
package mmb.WORLD.block;

import java.awt.Graphics;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;

import mmb.GameObject;
import mmb.BEANS.Saver;
import mmb.WORLD.Side;
import mmb.WORLD.electric.Electricity;
import mmb.WORLD.inventory.Inventory;
import mmb.WORLD.inventory.NoSuchInventory;
import mmb.WORLD.inventory.io.InventoryReader;
import mmb.WORLD.inventory.io.InventoryWriter;
import mmb.WORLD.worlds.world.BlockMap;

/**
 * @author oskar
 * The following methods can be overridden:
 * provideSignal - 
 */
public interface BlockEntry extends Saver<JsonNode> {
	public boolean isBlockEntity();
	@Nonnull public BlockEntity asBlockEntity();
	public BlockEntity nasBlockEntity();
	@Nonnull public BlockType type();
	/**
	 * @param type block type to check
	 * @return does given type match actual type?
	 */
	public default boolean typeof(BlockType type) {
		return type == type();
	}
	public default boolean provideSignal(Side s) {
		return false;
	}
	public default Inventory getInventory(Side s) {
		return NoSuchInventory.INSTANCE;
	}
	public default InventoryReader getOutput(Side s) {
		return getInventory(s).createReader();
	}
	
	public default void wrenchCW() {
		//to be implemented by sublasses
	}
	public default void wrenchCCW() {
		//to be implemented by sublasses
	}
	
	/**
	 * @param x X coordinate of UL corner
	 * @param y Y coordinate of UL corner
	 * @param g graphics context
	 */
	public default void render(int x, int y, Graphics g) {
		type().getTexture().draw(this, x, y, g);
	}
	/**
	 * @param nother
	 * @return
	 */
	public default InventoryWriter getInput(Side s) {
		return getInventory(s).createWriter();
	}

	public default void onStartup(BlockMap map) {}
	public default void onPlace(BlockMap map, @Nullable GameObject obj) {}
	public default void onBreak(BlockMap blockMap, @Nullable GameObject obj) {}
	public default void onShutdown(BlockMap map) {}
	
	
	@Nullable public default Electricity getElectricalConnection(Side s) {
		return null;
	}
	/**
	 * Returns conduit capacity of this wire. If value passes {@link BlockEntry#isValidConduitCapacity(double) isValidConduit()}, it represents a valid conduit.
	 * @return the capacity of conduit, or NaN if not.
	 */
	public default double condCapacity() {
		return Double.NaN;
	}
	/**
	 * Checks if given block entry is a valid conduit.
	 * @param ent block to check
	 * @return is given block conduit?
	 */
	public static boolean isValidConduit(@Nullable BlockEntry ent) {
		if(ent == null) return false;
		return isValidConduitCapacity(ent.condCapacity());
	}
	/**
	 * Checks if given {@code double} value represents a valid conduit capacity
	 * @param cap capacity to check
	 * @return is given capacity valid?
	 */
	public static boolean isValidConduitCapacity(double cap) {
		return Double.isFinite(cap) && cap >= 0;
	}
	/**
	 * Prints debug information for use in debug menu
	 * @param sb string builder
	 */
	public default void debug(StringBuilder sb) {
		//unused
	}

}
