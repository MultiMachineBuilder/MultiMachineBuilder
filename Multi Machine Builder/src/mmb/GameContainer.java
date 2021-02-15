/**
 * 
 */
package mmb;

import java.util.Set;

import javax.annotation.Nullable;

import mmb.ERRORS.UnsupportedObjectException;

/**
 * @author oskar
 *
 */
public interface GameContainer extends GameObject, Iterable<GameObject>{
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
}
