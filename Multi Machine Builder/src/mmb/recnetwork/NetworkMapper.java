/**
 * 
 */
package mmb.recnetwork;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;

/**
 * @author oskar
 * @param <T> type of network node
 * @param <U> type of network
 * A class to help map a network
 */
public abstract class NetworkMapper<T extends NetworkNode<T, U>, U extends Network<T>> {
	private Queue<T> queue = new LinkedList<>();
	public void add(T node) {
		queue.add(node);
	}
	/**
	 * Create a new network
	 * @return a new network
	 */
	public abstract U createNewNetwork();
	
	@SuppressWarnings("unchecked")
	public NetworkSystem<T> traceAllItems() {
		Set<T> processedNodes = new HashSet<>();
		outer:
		while(!queue.isEmpty()) {
			T item = queue.remove(); //dequeue
			if(processedNodes.contains(item)) continue outer;
			
			//trace all nodes
			U net = createNewNetwork();
			Queue<T> subqueue = new LinkedList<>();
			subqueue.add(item);
			inner:
			while(!subqueue.isEmpty()) {
				T subitem = subqueue.remove();
				if(processedNodes.contains(subitem)) continue inner;
				U subnet = subitem.getNetwork();
				if(net != null) { //the node already has a network
					net.replaceWith(subnet);
					net = subnet;
				}
				subitem.addNextNodes((Queue<NetworkNode<T, U>>) subqueue);
				processedNodes.add(subitem);
			}
		}
	}	
}
