/**
 * 
 */
package mmb.ships;

/**
 * 
 * @author oskar
 *
 * @param <T> module's type
 * @param <U> target type
 * @param <V> databank type
 */
@SuppressWarnings("rawtypes")
public interface Module<T extends Module, U, V> {
	public void createModule(U object);
	public void startApplying();
	public void stopApplying();
	
	/**
	 * Get data object for this module
	 */
	public V getData();
}
