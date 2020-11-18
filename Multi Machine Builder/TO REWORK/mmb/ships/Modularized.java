/**
 * 
 */
package mmb.ships;

/**
 * 
 * @author oskar
 *
 * @param <T> this object
 * @param <M> type of module 
 */
public interface Modularized<T extends Modularized<T, M>, M extends Module> {
	public void add(M module);
}
