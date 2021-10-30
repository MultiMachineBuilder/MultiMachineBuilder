/**
 * 
 */
package mmb.WORLD.blocks.ipipe;

import javax.annotation.Nonnull;

import mmb.WORLD.block.BlockEntityType;
import mmb.WORLD.rotate.ChirotatedImageGroup;
import mmb.WORLD.rotate.Side;

/**
 * @author oskar
 *
 */
public class Pipes {
	/**
	 * Represets a straight pipe
	 */
	@Nonnull public static final BlockEntityType STRAIGHT;
	/**
	 * Represents an elbow pipe
	 */
	@Nonnull public static final BlockEntityType ELBOW;
	/**
	 * An Intersecting Pipe Extractor has a perpendicular straight pipe
	 */
	@Nonnull public static final BlockEntityType IPE;
	
	@Nonnull public static final BlockEntityType TOLEFT;
	
	@Nonnull public static final BlockEntityType TORIGHT;
	
	@Nonnull public static final BlockEntityType CROSS;
	
	@Nonnull public static final BlockEntityType DUALTURN;
	
	static {
		STRAIGHT = new BlockEntityType();
		STRAIGHT.setTexture("machine/pipe straight.png");
		STRAIGHT.setDescription("A pipe which is straight.");
		STRAIGHT.setTitle("Straight pipe");
		ChirotatedImageGroup textureStraight = ChirotatedImageGroup.create("machine/pipe straight.png");
		STRAIGHT.setFactory(() -> new Pipe(Side.R, Side.L, Pipes.STRAIGHT, textureStraight));
		STRAIGHT.register("pipe.I");
		
		ELBOW = new BlockEntityType();
		ELBOW.setTexture("machine/pipe elbow.png");
		ELBOW.setDescription("A pipe which is bent.");
		ELBOW.setTitle("Elbow pipe");
		ChirotatedImageGroup textureElbow = ChirotatedImageGroup.create("machine/pipe elbow.png");
		ELBOW.setFactory(() -> new Pipe(Side.R, Side.D, ELBOW, textureElbow));
		ELBOW.register("pipe.L");
		
		IPE = new BlockEntityType();
		IPE.setTexture("machine/imover intersected.png");
		IPE.setDescription("A hybrid of Item Mover and straight pipe");
		IPE.setTitle("Intersecting Pipe Mover");
		ChirotatedImageGroup textureIPE = ChirotatedImageGroup.create("machine/imover intersected.png");
		IPE.setFactory(() -> new IntersectingPipeExtractor(Side.R, Side.L, IPE, textureIPE));
		IPE.register("pipe.IPE");
		
		TOLEFT = new BlockEntityType();
		TOLEFT.setTexture("machine/pipe merge left.png");
		TOLEFT.setDescription("A pipe which directs its side input to the left.");
		TOLEFT.setTitle("Left Binding Pipe");
		ChirotatedImageGroup textureToLeft = ChirotatedImageGroup.create("machine/pipe merge left.png");
		TOLEFT.setFactory(() -> new PipeBinder(TOLEFT, Side.L, textureToLeft));
		TOLEFT.register("pipe.toleft");
		
		TORIGHT = new BlockEntityType();
		TORIGHT.setTexture("machine/pipe merge right.png");
		TORIGHT.setDescription("A pipe which directs its side input to the right.");
		TORIGHT.setTitle("Right Binding Pipe");
		ChirotatedImageGroup textureToRight = ChirotatedImageGroup.create("machine/pipe merge right.png");
		TORIGHT.setFactory(() -> new PipeBinder(TORIGHT, Side.R, textureToRight));
		TORIGHT.register("pipe.toright");
		
		CROSS = new BlockEntityType();
		CROSS.setTexture("machine/pipe bridged.png");
		CROSS.setDescription("A pair of disconnected perpendicular pipes.");
		CROSS.setTitle("Bridging pipe");
		ChirotatedImageGroup textureCross = ChirotatedImageGroup.create("machine/pipe bridged.png");
		CROSS.setFactory(() -> new DualPipe(Side.D, Side.R, CROSS, textureCross));
		CROSS.register("pipe.X");
		
		DUALTURN = new BlockEntityType();
		DUALTURN.setTexture("machine/pipe biturn.png");
		DUALTURN.setDescription("A pair of curved pipes.");
		DUALTURN.setTitle("Dual pipe");
		ChirotatedImageGroup textureDualTurn = ChirotatedImageGroup.create("machine/pipe biturn.png");
		DUALTURN.setFactory(() -> new DualPipe(Side.R, Side.D, DUALTURN, textureDualTurn));
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
