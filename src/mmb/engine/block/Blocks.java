/**
 * 
 */
package mmb.engine.block;

import java.awt.Color;

import mmb.Main;
import mmb.PropertyExtension;
import mmb.annotations.NN;
import mmb.engine.blockdrawer.ColorDrawer;
import mmb.engine.blockdrawer.TextureDrawer;
import mmb.engine.debug.Debugger;
import mmb.engine.texture.Textures;

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
	@NN public static final Block air = Blocks.createAir();
	@NN
	private static Block createAir() {
		debug.printl("Creating blocks");
		Block result = new Block("mmb.air",
			PropertyExtension.setTexture(new ColorDrawer(Color.CYAN)),
			PropertyExtension.translateTitle("#air"),
			PropertyExtension.setSurface(true)
		);
		result.setLeaveBehind(result);
		return result;
	}
	/** The basic building block of any world */
	@NN public static final Block grass = Blocks.createGrass(); //REQUIRES SPECIAL INIT
	@NN
	private static Block createGrass() {	
		if(!Main.isRunning()) return air;
		
		Block result = new Block("mmb.grass",
			PropertyExtension.setTexture(new TextureDrawer(Textures.get("grass.png"))),
			PropertyExtension.translateTitle("#grass"),
			PropertyExtension.setTitle("A default block in the world"),
			PropertyExtension.setSurface(true),
			PropertyExtension.setLeaveBehind(air)
		);
		return result;
	}
	/** A placeholder block used when the world access is out of bounds*/
	@NN public static final Block blockVoid = 
		new Block("mmb.void", 
			PropertyExtension.setTexture(new ColorDrawer(Color.DARK_GRAY)),
			PropertyExtension.translateTitle("#void")
		);
	
	/** Initializes blocks */
	public static void init() {
		//empty
	}
	
}
