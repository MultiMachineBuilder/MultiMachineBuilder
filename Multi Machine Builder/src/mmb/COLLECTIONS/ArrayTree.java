/**
 * 
 */
package mmb.COLLECTIONS;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author oskar
 * @param <T> item type
 *
 */
public class ArrayTree<T> implements Tree<T> {
	Tree<T> parent;
	Set<Tree<T>> children = new HashSet<>();
	@Override
	public Iterator<T> iterator() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Collection<Tree<T>> getChildren() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Tree<T> getParent() {
		// TODO Auto-generated method stub
		return null;
	}
}
