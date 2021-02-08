/**
 * 
 */
package mmb.COLLECTIONS;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author oskar
 *
 */
public class TRArrayTree<T> implements TRTree<T> {
	private final Class<T> cls;
	public TRArrayTree(Class<T> cls) {
		super();
		this.cls = cls;
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

	@Override
	public Iterator<T> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<T> getType() {
		return cls;
	}

}
