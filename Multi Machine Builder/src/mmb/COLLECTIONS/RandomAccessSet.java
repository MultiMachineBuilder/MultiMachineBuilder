/**
 * 
 */
package mmb.COLLECTIONS;

import java.util.Set;

/**
 * @author oskar
 *
 */
public interface RandomAccessSet<T> extends Set<T> {
	public T remove(int index);
	public T get(int index);
}
