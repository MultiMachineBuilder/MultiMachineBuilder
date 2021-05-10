/**
 * 
 */
package mmb;

import javax.annotation.Nullable;

import monniasza.collects.Identifiable;

/**
 * @author oskar
 * This interface is used for all game objects which can be:
 * <ul>
 * 	<li>called by their ID</li>
 * 	<li>identified online</li>
 * 	<li>assigned to a owner</li>
 * 	<li></li>
 * </ul>
 * Following classes are game objects:
 * (variables are placed in square brackets)
 * <ul>
 * 	<li>players - [nick]</li>
 * 	<li>machines: [English title]:[map]@[world]:[x],[y]</li>
 * 	<li>world behaviors: [title]:[world]</li>
 *  <li>map behaviors: [title]:[map]@[world]</li>
 * 	<li>creatures: [title] #[UUID]</li>
 *  <li>packets: packet #[UUID]</li>
 *  <li>maps: map [map]@[world]</li>
 *  <li>worlds: world [world]</li>
 *  <li>tasks: task [taskID]</li>
 *  <li>null objects: null</li>
 *  <li>servers: [URL or IP address]</li>
 * </ul>
 */
public interface GameObject extends Identifiable<String>{
	/**
	 * @return uniquely formatted, shareable and retrievable ID
	 */
	public String getUTID();
	
	/**
	 * Remove the {@code GameObject} from existence
	 */
	public void destroy();
	/**
	 * Get the GameObject which owns this GameObject.
	 * If this is a top level GameObject, return null.
	 * The ownership is different from containment, which can be retrieved by {@link getContainer()}
	 * 
	 * <br>If object is owned by a player, it returns a player.
	 * If object is public, it returns a server, or null if in local mode
	 * 
	 * @return this GameObject's owner
	 */
	@Nullable
	public GameObject getOwner();
	/**
	 * @return is given GameObject a singleton (one copy for each type)
	 */
	public default boolean isSingleton() {return false;}
}
