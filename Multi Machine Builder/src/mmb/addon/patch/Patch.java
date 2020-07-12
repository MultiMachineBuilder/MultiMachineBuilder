/**
 * 
 */
package mmb.addon.patch;

/**
 * @author oskar
 *
 */
public interface Patch<T> {
	public void apply(T value);
	
}
