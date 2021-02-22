/**
 * 
 */
package mmb.recnetwork;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

/**
 * @author oskar
 * This class represents a network system, which contains all networks
 * @param <T>
 * @param <U> type of network
 */
public class NetworkSystem<T extends NetworkNode<?>> {
	private Queue<NetworkNode<T>> toNetworkizeQueue = new LinkedList<>();
	private Set<Network<T>> networks = new HashSet<>();
	public Network<T> networkFor(NetworkNode<T> node){
		Network<T> result = node.getNetwork();
		if(result != null) return result; //the node already has a network
		//Traverse the network
		Set<NetworkNode<T>> checkedNodes = new HashSet<>(); 
		Network<T> network = new Network<>(); //new network for the nodes
		Queue<NetworkNode<T>> queue = new LinkedList<>();
		queue.add(node);
		//Iterate over each node
		NetworkNode<T> next;
		while((next = queue.poll()) != null) {
			if(checkedNodes.contains(next)) continue; //node was already inspected
			Network<T> net = next.getNetwork();
			if(net != null) { //the node already has a network
				network.replaceWith(net);
				network = net;
			}
			next.addNextNodes(queue);
			checkedNodes.add(next);
		}
		networks.add(network);
		return network;
	}
}
