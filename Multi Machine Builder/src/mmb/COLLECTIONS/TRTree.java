/**
 * 
 */
package mmb.COLLECTIONS;

/**
 * @author oskar
 * A type retaining version of {@link mmb.COLLECTIONS.Tree}
 */
public interface TRTree<T> extends Tree<T>{
	public Class<T> getType();
}
