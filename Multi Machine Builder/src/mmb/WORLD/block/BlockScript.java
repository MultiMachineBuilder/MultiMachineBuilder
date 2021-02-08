/**
 * 
 */
package mmb.WORLD.block;

import java.util.function.Consumer;

import mmb.GameObject;
import mmb.WORLD.worlds.MapProxy;

/**
 * @author oskar
 *
 */
public interface BlockScript {
	
	/**
	 * Handles start-up.
	 * <br>Does not run when block is placed.
	 * <br>To handle placement, set onPlace variable
	 * <br>
	 * <br>Event: world loading
	 * <br>Purpose: initialize block
	 * <br>Exception handling: If exception occurs, a block is not properly initialized and error is logged into:
	 * <ul>
	 * 	<li>log file, which remains on a server</li>
	 * 	<li>error statement, sent to registered server users and newsletter subscribers</li>
	 * </ul>
	 * @param ent newly created block entry
	 */
	public void onStartup(BlockEntry ent);
	/**
	 * This function is run when the data is provided to the block.
	 * <br>To handle placement, set onPlace variable
	 * <br>
	 * <br>Event: world loading
	 * <br>Purpose: initialize block data
	 * <br>Exception handling: If exception occurs, a block is not properly initialized and error is logged into:
	 * <ul>
	 * 	<li>log file, which remains on a server</li>
	 * 	<li>error statement, sent to registered server users and newsletter subscribers</li>
	 * </ul>
	 * @param ent block entry, to which data is loaded
	 */
	public void onDataLoaded(BlockEntry ent);
	/**    
	 * Run an update on this block for every tick
	 * <br>NOTE: design this function carefully to prevent logspam
	 * <br>
	 * <br>Event: tick update (50 times per second)
	 * <br>Purpose: run normal updates
	 * <br>Exception handling: If exception occurs, block is not properly updated and error is logged and chatted
	 * 	<ul>
	 * 		<li>to a player, if action was requested by a player or theirs block,</li>
	 * 		<li>or public, if action was requested by unclaimed or server owned block</li>
	 * 	</ul>
	 * @param ent block entry to be updated
	 * @param proxy the map's proxy
	 */
	public void onUpdate(BlockEntry ent, MapProxy proxy);
	/**
	 * Handles the block being placed.
	 * <ul>
	 * 	<li>Event: block placement</li>
	 * 	<li>Purpose: correctly place blocks</li>
	 * 	<li>
	 * 		Exception handling: If exception occurs, block is not placed and error is logged and chatted
	 * 		<ul>
	 * 			<li>to a player, if action was requested by a player or theirs block,</li>
	 * 			<li>or public, if action was requested by unclaimed or server owned block</li>
	 * 		</ul>
	 * 	</li>
	 * </ul>
	 * @param ent newly created block entry, about to be placed
	 * @param obj object which requested placement:
	 * 		<br>class name if GameObject item not provided
	 * 		<br>player instance if placed by player
	 * 		<br>WorldBehavior instance if placed by a WorldBehavior
	 * 		<br>MapBehavior instance if placed by a MapBehavior
	 */
	public void onPlace(BlockEntry ent, GameObject obj);
	/**
	 * Handles the block being mined.
	 * <ul>
	 * 	<li>Event: block mining</li>
	 * 	<li>Purpose: correctly mine blocks</li>
	 * 	<li>Input 1: </li>
	 * 	<li>
	 * 		Exception handling: If exception occurs, block is not removed and error is logged and chatted
	 * 		<ul>
	 * 			<li>to a player, if action was requested by a player or theirs block,</li>
	 * 			<li>or public, if action was requested by unclaimed or server owned block</li>
	 * 		</ul>
	 * 	</li>
	 * </ul>
	 * @param ent block entry, about to be removed
	 * @param obj object which requested placement:
	 * 		<br>class name if GameObject item not provided
	 * 		<br>player instance if mined by player
	 * 		<br>WorldBehavior instance if mined by a WorldBehavior
	 * 		<br>MapBehavior instance if mined by a MapBehavior
	 */
	public void onRemove(BlockEntry ent, GameObject obj);
	/**    
	 * Prepares a block for server shutdown.
	 * <br>Does not run when block is broken.
	 * <br>To handle block removal,set onRemove variable
	 * <br>
	 * <br>Event: server shutdown
	 * <br>Purpose: finalize block before disposal
	 * <br>Exception handling: If exception occurs, block is not properly finalized and error is logged into:
	 * <ul>
	 * 	<li>log file, which remains on a server</li>
	 * 	<li>error statement, sent to registered server users and newsletter subscribers</li>
	 * </ul>
	 * @param ent block entry to be saved
	 */
	public void onShutdown(BlockEntry ent);
	
	/**
	 * Returns a composed script that performs given operations combined.
	 * @param that second script
	 * @return combined script
	 */
	default public BlockScript andThen(BlockScript that) {
		BlockScript that2 = this;
		return new BlockScript() {

			@Override
			public void onStartup(BlockEntry ent) {
				that.onStartup(ent);
				that2.onStartup(ent);
			}

			@Override
			public void onDataLoaded(BlockEntry ent) {
				that.onDataLoaded(ent);
				that2.onDataLoaded(ent);
			}

			@Override
			public void onUpdate(BlockEntry ent, MapProxy proxy) {
				that.onUpdate(ent, proxy);
				that2.onUpdate(ent, proxy);
			}

			@Override
			public void onPlace(BlockEntry ent, GameObject obj) {
				that.onPlace(ent, obj);
				that2.onPlace(ent, obj);
			}

			@Override
			public void onRemove(BlockEntry ent, GameObject obj) {
				that.onRemove(ent, obj);
				that2.onRemove(ent, obj);
			}

			@Override
			public void onShutdown(BlockEntry ent) {
				that.onShutdown(ent);
				that2.onShutdown(ent);
			}
			
		};
	}
}
