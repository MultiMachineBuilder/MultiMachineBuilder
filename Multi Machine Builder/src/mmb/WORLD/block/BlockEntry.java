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
import mmb.WORLD.texture.BlockDrawer;
import mmb.WORLD.worlds.world.World;

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
	
	//Inventories
	public default Inventory getInventory(Side s) {
		return NoSuchInventory.INSTANCE;
	}
	public default InventoryReader getOutput(Side s) {
		return getInventory(s).createReader();
	}
	/**
	 * @param nother
	 * @return
	 */
	public default InventoryWriter getInput(Side s) {
		return getInventory(s).createWriter();
	}
	
	public default void wrenchCW() {
		//to be implemented by sublasses
	}
	public default void wrenchCCW() {
		//to be implemented by sublasses
	}
	
	/**
	 * @param x left X coordinate
	 * @param y upper Y coordinate
	 * @param g graphics context
	 * @param side side size
	 */
	public default void render(int x, int y, Graphics g, int side) {
		BlockDrawer drawer = type().getTexture();
		drawer.draw(this, x, y, g, side);
	}
	

	public default void onStartup(World map) {}
	public default void onPlace(World map, @Nullable GameObject obj) {}
	public default void onBreak(World blockMap, @Nullable GameObject obj) {}
	public default void onShutdown(World map) {}
	
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
	
	/**
	 * Creates a block-wise copy of this block entry.
	 * The returned block entry may have position data attached,
	 * or it may be identical.
	 * @return a copy of this block
	 * @implSpec This method must not throw any exceptions
	 * @apiNote Used to copy blocks by world editing tools
	 */
	@Nonnull public BlockEntry blockCopy();

	/**
	 * Moves this block to a new map
	 * @param map new map (can be the same, or null if block goes off map)
	 * @param x X coordinate on a new map
	 * @param y Y coordinate on a new map
	 * @throws IllegalStateException if given map/position combination is not surface
	 */
	public void resetMap(@Nullable World map, int x, int y); //should only be called by World
}
