/**
 * 
 */
package mmb.WORLD.machine;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import com.fasterxml.jackson.databind.JsonNode;
import mmb.GameObject;
import mmb.BEANS.Loader;
import mmb.BEANS.Saver;
import mmb.WORLD.worlds.MapProxy;
import mmb.WORLD.worlds.map.BlockMap;

/**
 * @author oskar
 * A machine is an entity that is run in real time, in contrast to blocks that require update events and/or triggers.
 * 
 * The following beans are supported by default:
 * <ul>
 * 	<li>ActionListener - machine activation</li>
 * 	<li>InventoryContainer - inventory</li>
 * 	<li>BlockInterface - connect to other machines</li>
 * 	<li>SignalListener - detect control signals</li>
 * </ul>
 * 
 * 0.5 planned:
 * 
 * 
 * 0.6 planned:
 * <ul>
 * 	<li>Vehicle - able to move on roads and tracks</li>
 * 	<li>InventoryContainer - inventory</li>
 * 	<li>BlockInterface - connect to other machines</li>
 * </ul>
 */
public interface Machine extends GameObject, Loader<JsonNode>, Saver<JsonNode>{
	//[start] positioning
	public default void setPos(Point p) {
		setPos(p.x, p.y);
	}
	/**
	 * Set machine's position
	 * @param x X coordinate
	 * @param y Y coordinate
	 */
	public void setPos(int x, int y);
	/**
	 * Set machine's X position
	 * @param x X coordinate
	 */
	public void setX(int x);
	/**
	 * Set machine's Y position
	 * @param y Y coordinate
	 */
	public void setY(int y);
	/**
	 * @return X coordinate
	 */
	public int posX();
	/**
	 * @return Y coordinate
	 */
	public int posY();
	/**
	 * Returns the location of this {@code Machine} in a new {@link Point}
	 * @return location of this {@code Machine}
	 */
	public default Point pos() {
		return new Point(posX(), posY());
	}
	public int sizeX();
	public int sizeY();
	/**
	 * Returns the size of this {@code Machine} in a new {@link Dimension}
	 * @return size of this {@code Machine}
	 */
	public default Dimension size() {
		return new Dimension(sizeX(), sizeY());
	}
	/**
	 * Returns the size of this {@code Machine} in a new {@link Rectangle}
	 * @return size of this {@code Machine}
	 */
	public default Rectangle bounds() {
		return new Rectangle(posX(), posY(), sizeX(), sizeY());
	}
	//[end]
	//[start] world
	public BlockMap getMap();
	public void setMap(BlockMap map);
	//[end]
	//[start] runtime
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
	public void onStartup();
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
	public void onDataLoaded();
	/**    
	 * Run an update on this machine for every tick
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
	public void onUpdate(MapProxy proxy);
	/**
	 * Handles the machine being placed.
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
	 * @param obj object which requested placement:
	 * 		<br>class name if GameObject item not provided
	 * 		<br>player instance if placed by player
	 * 		<br>WorldBehavior instance if placed by a WorldBehavior
	 * 		<br>MapBehavior instance if placed by a MapBehavior
	 */
	public void onPlace(GameObject obj);
	/**
	 * Handles the machine being mined.
	 * <ul>
	 * 	<li>Event: block mining</li>
	 * 	<li>Purpose: correctly mine blocks</li>
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
	 * 		<br>null if mined by player in LSP
	 */
	public void onRemove(GameObject obj);
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
	public void onShutdown();
	//[end]

	/**
	 * Render a machine
	 * 
	 * The graphics context is isolated from main graphics context
	 * @param g graphics context
	 */
	public void render(Graphics g);	
	/**
	 * @return machine's type ID
	 */
	public String name();	
}
