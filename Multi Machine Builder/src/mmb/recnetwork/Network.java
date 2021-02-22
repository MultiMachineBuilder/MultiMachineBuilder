/**
 * 
 */
package mmb.recnetwork;

import java.util.HashSet;
import java.util.Set;

/**
 * @author oskar
 * @param <T> type of connections
 *
 */
public class Network<T> {
	Set<NetworkNode<T>> contents = new HashSet<>();
	public void replaceWith(Network<T> other) {
		for(NetworkNode<T> node: contents) {
			node.network = other;
		}
		contents.clear();
	}
}
