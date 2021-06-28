/**
 * 
 */
package mmb.WORLD.blocks.pipe;

import mmb.WORLD.RotatedImageGroup;
import mmb.WORLD.Side;
import mmb.WORLD.block.BlockEntityType;

/**
 * @author oskar
 *
 */
public class Pipes {
	/**
	 * Represets a straight pipe
	 */
	public static final BlockEntityType STRAIGHT;
	/**
	 * Represents an elbow pipe
	 */
	public static final BlockEntityType ELBOW;
	/**
	 * An Intersecting Pipe Extractor has a perpendicular straight pipe
	 */
	public static final BlockEntityType IPE;
	
	public static final BlockEntityType TOLEFT;
	
	public static final BlockEntityType TORIGHT;
	
	public static final BlockEntityType CROSS;
	
	public static final BlockEntityType DUALTURN;
	
	static {
		STRAIGHT = new BlockEntityType();
		STRAIGHT.setTexture("machine/pipe straight.png");
		STRAIGHT.setDescription("A pipe which is straight.");
		STRAIGHT.setTitle("Straight pipe");
		RotatedImageGroup textureStraight = RotatedImageGroup.create("machine/pipe straight.png");
		STRAIGHT.setFactory((x, y, m) -> new Pipe(x, y, m, Side.R, Side.L, Pipes.STRAIGHT, textureStraight));
		STRAIGHT.register("pipe.I");
		
		ELBOW = new BlockEntityType();
		ELBOW.setTexture("machine/pipe elbow.png");
		ELBOW.setDescription("A pipe which is bent.");
		ELBOW.setTitle("Elbow pipe");
		RotatedImageGroup textureElbow = RotatedImageGroup.create("machine/pipe elbow.png");
		ELBOW.setFactory((x, y, m) -> new Pipe(x, y, m, Side.R, Side.D, ELBOW, textureElbow));
		ELBOW.register("pipe.L");
		
		IPE = new BlockEntityType();
		IPE.setTexture("machine/imover intersected.png");
		IPE.setDescription("A hybrid of Item Mover and straight pipe");
		IPE.setTitle("Intersecting Pipe Mover");
		RotatedImageGroup textureIPE = RotatedImageGroup.create("machine/imover intersected.png");
		IPE.setFactory((x, y, m) -> new IntersectingPipeExtractor(x, y, m, Side.R, Side.L, IPE, textureIPE));
		IPE.register("pipe.IPE");
		
		TOLEFT = new BlockEntityType();
		TOLEFT.setTexture("machine/pipe merge left.png");
		TOLEFT.setDescription("A pipe which directs its side input to the left.");
		TOLEFT.setTitle("Left Binding Pipe");
		RotatedImageGroup textureToLeft = RotatedImageGroup.create("machine/pipe merge left.png");
		TOLEFT.setFactory((x, y, m) -> new PipeBinder(x, y, m, TOLEFT, Side.L, textureToLeft));
		TOLEFT.register("pipe.toleft");
		
		TORIGHT = new BlockEntityType();
		TORIGHT.setTexture("machine/pipe merge right.png");
		TORIGHT.setDescription("A pipe which directs its side input to the right.");
		TORIGHT.setTitle("Right Binding Pipe");
		RotatedImageGroup textureToRight = RotatedImageGroup.create("machine/pipe merge right.png");
		TORIGHT.setFactory((x, y, m) -> new PipeBinder(x, y, m, TORIGHT, Side.R, textureToRight));
		TORIGHT.register("pipe.toright");
		
		CROSS = new BlockEntityType();
		CROSS.setTexture("machine/pipe bridged.png");
		CROSS.setDescription("A pair of disconnected perpendicular pipes.");
		CROSS.setTitle("Bridging pipe");
		RotatedImageGroup textureCross = RotatedImageGroup.create("machine/pipe bridged.png");
		CROSS.setFactory((x, y, m) -> new DualPipe(x, y, m, Side.D, Side.R, CROSS, textureCross));
		CROSS.register("pipe.X");
		
		DUALTURN = new BlockEntityType();
		DUALTURN.setTexture("machine/pipe biturn.png");
		DUALTURN.setDescription("A pair of curved pipes.");
		DUALTURN.setTitle("Dual pipe");
		RotatedImageGroup textureDualTurn = RotatedImageGroup.create("machine/pipe biturn.png");
		DUALTURN.setFactory((x, y, m) -> new DualPipe(x, y, m, Side.R, Side.D, DUALTURN, textureDualTurn));
		DUALTURN.register("pipe.D");
	}
	/**
	 * Initializes pipes
	 */
	public static void init() {
		//JUST FOR INITIALIZATION
	}

	private Pipes() {}
}
