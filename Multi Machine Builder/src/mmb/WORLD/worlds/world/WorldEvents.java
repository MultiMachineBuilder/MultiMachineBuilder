/**
 * 
 */
package mmb.WORLD.worlds.world;

import mmb.GameObject;
import mmb.WORLD.block.BlockEntry;

/**
 * @author oskar
 *
 */
public class WorldEvents {
	private WorldEvents() {}
	
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
	
	
}
