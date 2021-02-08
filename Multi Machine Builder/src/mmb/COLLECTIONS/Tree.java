/**
 * 
 */
package mmb.COLLECTIONS;

import java.util.Collection;

import javax.annotation.Nullable;

/**
 * @author oskar
 *
 */
public interface Tree<T> extends Iterable<T> {
	public Collection<Tree<T>> getChildren();
	/**
	 * Get the parent node of a tree
	 * @return parent node of the tree
	 */
	@Nullable public Tree<T> getParent();
}
