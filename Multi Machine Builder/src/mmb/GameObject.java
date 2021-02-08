/**
 * 
 */
package mmb;

import java.util.Set;

import javax.annotation.Nullable;

import mmb.ERRORS.UnsupportedObjectException;
import mmb.RUNTIME.RuntimeManager;

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
public interface GameObject extends Identifiable<String>, Iterable<GameObject>{
	/**
	 * @return uniquely formatted, shareable and retrievable ID
	 */
	public String getUTID();
	/**
	 * Get a runtime manager which runs given {@code GameObject}.
	 * Return null to indicate that game object does not have runtime
	 * @return a runtime manager, or null
	 */
	@Nullable
	public RuntimeManager getRuntimeManager();
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
	//[start] containment
	/**
	 * 
	 * 
	 * Each of these objects is contained by:
	 * <ul>
	 * 	<li>players: nothing</li>
	 * 	<li>machines: map</li>
	 * 	<li>world behaviors: world</li>
	 *  <li>map behaviors: map</li>
	 * 	<li>creatures: map or container</li>
	 *  <li>packets: machine, task or network</li>
	 *  <li>maps: world</li>
	 *  <li>worlds: server</li>
	 *  <li>tasks: machine or network</li>
	 *  <li>null objects: nothing</li>
	 *  <li>servers: World Wide Web (virtual)</li>
	 * </ul>
	 * @return this GameObject's container
	 */
	@Nullable
	public GameObject getContainer();
	/**
	 * @param container
	 * @throws UnsupportedObjectException if container is not supported
	 */
	public void setContainer(GameObject container);
	/**
	 * @param obj {@code GameObject} to be added
	 * @return was given object added?
	 * <br>returns false when given {@code GameObject} is not supported or addition fails
	 */
	public boolean add(GameObject obj);
	/**
	 * Remove given {@code GameObject} from contents
	 * @param obj 
	 * @return was given object removed?
	 */
	public boolean remove(GameObject obj);
	/**
	 * @param obj {@code GameObject} to check
	 * @return does this {@code GameObject} contain provided {@code GameObject}
	 */
	public boolean contains(GameObject obj);
	/**
	 * @return a set with all {@code GameObject}s contained within this {@code GameObject}
	 */
	public Set<GameObject> contents();
	//[end]
	//[start] saves
	
	//[end]
}
