/**
 * 
 */
package mmb.NETWORK;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.collections4.iterators.EmptyIterator;

import mmb.GameObject;
import mmb.RUNTIME.RuntimeManager;

/**
 * @author oskar
 *
 */
public class EmptyGameObject implements GameObject {
	@Override
	public Iterator<GameObject> iterator() {
		return EmptyIterator.emptyIterator();
	}
	@Override
	public String getUTID() {
		return "World Wide Web";
	}
	@Override
	public RuntimeManager getRuntimeManager() {
		return null;
	}
	@Override
	public void destroy() {
		//do nothing
	}
	@Override
	public GameObject getOwner() {
		return null;
	}
	@Override
	public GameObject getContainer() {
		return null;
	}
	@Override
	public void setContainer(GameObject container) {}
	@Override
	public boolean add(GameObject obj) {
		return false;
	}
	@Override
	public boolean remove(GameObject obj) {
		return false;
	}
	@Override
	public boolean contains(GameObject obj) {
		return false;
	}
	@Override
	public Set<GameObject> contents() {
		return Collections.emptySet();
	}
	@Override
	public String identifier() {
		// TODO Auto-generated method stub
		return null;
	}

}
