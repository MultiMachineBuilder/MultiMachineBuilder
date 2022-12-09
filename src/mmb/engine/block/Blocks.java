/**
 * 
 */
package mmbeng.block;

import java.awt.Color;

import javax.annotation.Nonnull;

import mmbeng.debug.Debugger;

/**
 * A minimum set of blocks required for the game to work.
 * <ul>
 * 	<li>Air - required for grass to be removable</i>
 * 	<li>Grass - required to fill the world</i>
 * 	<li>Void - default return value for blocks outside the bounds of the world</i>
 * </ul>
 * @author oskar
 */
public class Blocks {
	private Blocks() {}
	private static final Debugger debug = new Debugger("BLOCK ENGINE");

	//Primitive blocks
	/** The alternative surface block */
	@Nonnull public static final Block air = Blocks.createAir();
	@Nonnull
	private static Block createAir() {
		debug.printl("Creating blocks");
		Block result = new Block();
		result.texture(Color.CYAN)
		.leaveBehind(result)
		.title("#air")
		.finish("mmb.air");
		result.setSurface(true);
		return result;
	}
	/** The basic building block of any world */
	@Nonnull public static final Block grass = Blocks.createGrass(); //REQUIRES SPECIAL INIT
	@Nonnull
	private static Block createGrass() {
		Block result = new Block();
		result.texture("grass.png")
		.leaveBehind(air)
		.title("#grass")
		.describe("A default block in the world")
		.finish("mmb.grass");
		result.setSurface(true);
		return result;
	}
	/** A placeholder block used when the world access is out of bounds*/
	@Nonnull public static final Block blockVoid = 
		new Block()
		.texture(Color.DARK_GRAY)
		.title("#void")
		.finish("mmb.void");
	
	/** Initializes blocks */
	public static void init() {
		//empty
	}
	
}
