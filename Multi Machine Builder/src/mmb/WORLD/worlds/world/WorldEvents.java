/**
 * 
 */
package mmb.WORLD.worlds.world;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.pploder.events.Event;
import com.pploder.events.SimpleEvent;

import io.vavr.Tuple2;
import mmb.CatchingEvent;
import mmb.GameObject;
import mmb.WORLD.block.BlockEntry;
import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class WorldEvents {
	private static final Debugger debug = new Debugger("WORLD EVENTS");
	private WorldEvents() {}

	/**
	 * Invoked when the world is saved
	 * Input 1: the world
	 * Input 2: the top level node, to which the world is saved
	 */
	public static final Event<Tuple2<World, ObjectNode>> save = new SimpleEvent<>();
	/**
	 * Invoked when the world is loaded
	 * Input 1: the world
	 * Input 2: the top level node, from which the world is loaded
	 */
	public static final Event<Tuple2<World, ObjectNode>> load = new SimpleEvent<>();
	/**
	 * Invoked after the world is loaded
	 * Any exceptions are caught and logged
	 * Input: the loaded world
	 */
	public static final Event<World> afterLoad = new CatchingEvent<>(debug, "Failed to process an event post-save");
	
	public static enum BlockAction{
		PLACE, DESTROY;
	}
	/**
	 * @author oskar
	 * Provided to block place/delete events
	 */
	public static class BlockModifyEvent{
		/** The world, on which the block is placed */
		public final World world;
		
		/** The block, which is about to be placed or removed*/
		public final BlockEntry block;
		
		/** The object which tries to perform the action*/
		public final GameObject owner;
		
		/** X coordinate */
		public final int x;
		/** Y coordinate */
		public final int y;

		private boolean doNotContinue;
		/** Call this to prevent the action */
		public void doNotContinue() {
			doNotContinue = true;
		}
		/** Should the process be continued? */
		@SuppressWarnings("javadoc")
		public boolean shouldContinue() {
			return !doNotContinue;
		}
		/** The action about to be performed*/
		public final BlockAction task;
		
		/**
		 * Create an event object
		 * @param world the world
		 * @param block the block entry
		 * @param owner performer
		 * @param task thing to do
		 * @param x X coordinate of the block entry
		 * @param y Y coordinate of the block entry
		 */
		public BlockModifyEvent(World world, BlockEntry block, GameObject owner, BlockAction task, int x, int y) {
			this.world = world;
			this.block = block;
			this.owner = owner;
			this.x = x;
			this.y = y;
			this.task = task;
		}
	}
	
	/**
	 * Invoked when any block is placed
	 * Input: the block event
	 */
	public static final Event<BlockModifyEvent> blockPlaced = 
			new CatchingEvent<>(debug, "Failed to handle block being placed");
	/**
	 * Invoked when any block is removed
	 * Input: the block event
	 */
	public static final Event<BlockModifyEvent> blockRemoved = 
			new CatchingEvent<>(debug, "Failed to handle block being removed");
}
