/**
 * 
 */
package mmb.world.block;

import java.awt.Graphics;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.joml.Vector2d;

import mmb.beans.Saver;
import mmb.graphics.texture.BlockDrawer;
import mmb.world.blocks.ppipe.PipeTunnelEntry;
import mmb.world.electric.Electricity;
import mmb.world.inventory.Inventory;
import mmb.world.inventory.NoSuchInventory;
import mmb.world.inventory.io.InventoryReader;
import mmb.world.inventory.io.InventoryWriter;
import mmb.world.rotate.Chiral;
import mmb.world.rotate.ChiralRotation;
import mmb.world.rotate.Chirality;
import mmb.world.rotate.Rotable;
import mmb.world.rotate.Rotation;
import mmb.world.rotate.Side;
import mmb.world.worlds.world.Player;
import mmb.world.worlds.world.World;

/**
 * @author oskar
 * The following methods can be overridden:
 * provideSignal - 
 */
public interface BlockEntry extends Saver, Rotable, Chiral {
	//Block entity checks
	public boolean isBlockEntity();
	@Nonnull public BlockEntity asBlockEntity();
	public BlockEntity nasBlockEntity();
	
	/** @return the block type */
	@Nonnull public BlockType type();
	/**
	 * @param type block type to check
	 * @return does given type match actual type?
	 */
	public default boolean typeof(BlockType type) {
		return type == type();
	}
	/**
	 * @param s side, from which to get signal
	 * @return the WireWorld signal
	 */
	public default boolean provideSignal(Side s) {
		return false;
	}
	
	//Inventories
	/**
	 * @param s side, from which to get inventory
	 * @return inventory at given side
	 */
	@Nonnull public default Inventory getInventory(Side s) {
		return NoSuchInventory.INSTANCE;
	}
	/**
	 * @param s side, from which to get output
	 * @return inventory reader at given side
	 */
	@Nonnull public default InventoryReader getOutput(Side s) {
		return getInventory(s).createReader();
	}
	/**
	 * @param s side, from which to get input
	 * @return inventory writer at given side
	 */
	@Nonnull public default InventoryWriter getInput(Side s) {
		return getInventory(s).createWriter();
	}
	
	/**
	 * Renders a block
	 * @param x left X coordinate
	 * @param y upper Y coordinate
	 * @param g graphics context
	 * @param side side size
	 */
	public default void render(int x, int y, Graphics g, int side) {
		BlockDrawer drawer = type().getTexture();
		drawer.draw(this, x, y, g, side);
	}
	
	/**
	 * Called when world is initialized
	 * <br>Exception handling: If exception is thrown by this method, the block is not properly initialized
	 * @param map world, which is initialized
	 * @param x X coordinate of the block
	 * @param y	Y coordinate of the block
	 */
	public default void onStartup(World map, int x, int y) {
		//optional
	}
	/**
	 * Called just after world is initialized
	 * <br>Exception handling: If exception is thrown by this method, the block is not properly initialized
	 * @param map world, which is initialized
	 * @param x X coordinate of the block
	 * @param y	Y coordinate of the block
	 */
	public default void postLoad(World map, int x, int y) {
		//optional
	}
	/**
	 * Called when block is placed
	 * <br>Exception handling: If exception is thrown by this method, the block is not placed
	 * @param map world, in which the block is placed
	 * @param obj player, which placed the block
	 * @param x X coordinate of the block
	 * @param y	Y cordinate of the block
	 */
	public default void onPlace(World map, int x, int y) {
		//optional
	}
	/**
	 * Called when block is broken
	 * <br>Exception handling: If exception is thrown by this method, the block is not broken
	 * @param map world, in which block is broken
	 * @param obj player, which broke the block
	 * @param x X coordinate of the block
	 * @param y	Y cordinate of the block
	 */
	public default void onBreak(World map, int x, int y) {
		//optional
	}
	/**
	 * Called when world is closed
	 * <br>Exception handling: If exception is thrown by this method, the block is not properly closed
	 * @param map world, which is shutting down
	 */
	public default void onShutdown(World map) {
		//optional
	}
	
	/**
	 * Gets the electrical connection on this block
	 * @param s
	 * @return the electrical connection on given side.
	 */
	@Nullable public default Electricity getElectricalConnection(Side s) {
		return null;
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
	
	//Rotations - rotary
	/**
	 * Checks if given block supports rotations
	 * @return is given block rotable?
	 */
	public default boolean isRotary() {
		return false;
	}
	@Override
	default void setRotation(Rotation rotation) {
		//does nothing
	}
	@Override
	@Nonnull default Rotation getRotation() {
		return Rotation.N;
	}
	/** Rotates the block clockwise */
	public default void wrenchCW() {
		setRotation(getRotation().cw());
	}
	/** Rotates the block counter-clockwise */
	public default void wrenchCCW() {
		setRotation(getRotation().cw());
	}
	/**
	 * Wrenches the block in chirality's "right" direction
	 * @param chiral chirality to use
	 */
	public default void wrenchRight(Chirality chiral) {
		setRotation(chiral.right(getRotation()));
	}
	/**
	 * Wrenches the block in chirality's "left" direction
	 * @param chiral chirality to use
	 */
	public default void wrenchLeft(Chirality chiral) {
		setRotation(chiral.left(getRotation()));
	}
	
	//Rotations - chiral
	/**
	 * Checks if given block is chiral
	 * @return is given block chiral?
	 */
	public default boolean isChiral() {
		return false;
	}
	@Override
	@Nonnull default Chirality getChirality() {
		return Chirality.R;
	}
	@Override
	default void setChirality(Chirality chirality) {
		//does nothing
	}
	/**
	 * Reverses the chirality of this block
	 */
	default void flip() {
		setChirality(getChirality().reverse());
	}

	//Rotation - chirotation
	default void setChirotation(ChiralRotation rotation) {
		setRotation(rotation.rotation);
		setChirality(rotation.chirality);
	}
	/** @return chirality and rotation of this block together */
	@Nonnull default ChiralRotation getChirotation() {
		return ChiralRotation.of(getRotation(), getChirality());
	}
	/** Flips on | plane */
	public default void flipH() {
		setChirotation(getChirotation().flipH());
	}
	/** Flips on ― plane */
	public default void flipV() {
		setChirotation(getChirotation().flipV());
	}
	/** Flips on / plane */
	public default void flipNE() {
		setChirotation(getChirotation().flipNE());
	}
	/** Flips on \ plane*/
	public default void flipNW() {
		setChirotation(getChirotation().flipNW());
	}

	//Player collision
	int FLAGS_COLLIDE_LEFT = 1;
	int FLAGS_COLLIDE_RIGHT = 2;
	int FLAGS_COLLIDE_UP = 4;
	int FLAGS_COLLIDE_DOWN = 8;
	int NO_COLLISION = 0;
	int COLLIDE_ALL = 15;
	
	/**
	 * Runs on player collision
	 * @param blockX the block's X position
	 * @param blockY the block's Y position
	 * @param world the world, in which the player resides
	 * @param player the player which collides with the block
	 */
	public default void onPlayerCollide(int blockX, int blockY, World world, Player player) {
		if(!isSurface())
			displaceFrom(blockX, blockY, blockX+1.0, blockY+1.0, player, true);
	}
	/**
	 * @return is this block entry surface?
	 */
	public default boolean isSurface() {
		return type().isSurface();
	}
	/**
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param player player to displace
	 * @param move TODO should player be moved
	 * @return rectangle collision: 0 if none, 1 if horizontal, 2 if vertical
	 */
	static int displaceFrom(double x1, double y1, double x2, double y2, Player player, boolean move) {
		//Displace on X axis
		double px1 = player.pos.x-0.3;
		double py1 = player.pos.y-0.3;
		double px2 = player.pos.x+0.3;
		double py2 = player.pos.y+0.3;
		Vector2d offset = new Vector2d();
		int coll = displace(x1, y1, x2, y2, px1, py1, px2, py2, offset);
		if(move) {
			player.pos.add(offset);
			if(coll == 1) {
				player.speed.x = 0;
			}
			if(coll == 2) {
				player.speed.y = 0;
			}
		}
		return coll;
	}
	/**
	 * @param cx1 first X coordinate of the stationary rectangle
	 * @param cy1 first Y coordinate of the stationary rectangle
	 * @param cx2 second X coordinate of the stationary rectangle
	 * @param cy2 second Y coordinate of the stationary rectangle
	 * @param px1 first X coordinate of the movable rectangle
	 * @param py1 first Y coordinate of the movable rectangle
	 * @param px2 second X coordinate of the movable rectangle
	 * @param py2 second Y coordinate of the movable rectangle
	 * @param out the vector, which stores offset
	 * @return rectangle collision: 0 if none, 1 if horizontal, 2 if vertical
	 */
	static int displace(double cx1, double cy1, double cx2, double cy2, double px1, double py1, double px2, double py2, Vector2d out) {
		double radCX =    (cx2-cx1)/2;
		double centerCX = (cx2+cx1)/2;
		double radPX =    (px2-px1)/2;
		double centerPX = (px2+px1)/2;
		double distX = Math.abs(centerPX-centerCX);
		double infringeX = (radPX+radCX) - distX;
		boolean colx = infringeX > 0;
		
		double radCY =    (cy2-cy1)/2;
		double centerCY = (cy2+cy1)/2;
		double radPY =    (py2-py1)/2;
		double centerPY = (py2+py1)/2;
		double distY = Math.abs(centerPY-centerCY);
		double infringeY = (radPY+radCY) - distY;
		boolean coly = infringeY > 0;
		
		boolean collide = colx && coly;
		if(!collide)
			return 0;
		
		if(infringeX < infringeY) {
			//displace horizontally
			if(centerPX > centerCX) {
				//displace right
				out.x = infringeX;
			}else{
				//displace left
				out.x = -infringeX;
			}
			return 1; //not working as expected
		}
			//displace vertically
			if(centerPY > centerCY) {
				//displace down
				out.y = infringeY;
			}else{
				//displace up
				out.y = -infringeY;
			}
			return 2;
	}
	
	//Pipe tunnels
	/**
	 * Gets the pipe tunnel at given side
	 * @param s the side from which pipe enters
	 * @return the pipe tunnel entry for this pipe, or null if none
	 */
	public default PipeTunnelEntry getPipeTunnel(Side s) {
		return null;
	}
}
