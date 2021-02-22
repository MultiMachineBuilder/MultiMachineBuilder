/**
 * 
 */
package mmb.recnetwork;

import java.util.Queue;

/**
 * @author oskar
 * @param <T> type of node
 * @param <U> type of network
 *
 */
public abstract class NetworkNode<T, U extends Network<T>> {
	U network;

	/**
	 * @return the network
	 */
	public U getNetwork() {
		return network;
	}

	/**
	 * @param network the network to set
	 */
	public void setNetwork(U network) {
		if(this.network == network) return;
		this.network.contents.remove(this);
		this.network = network;
		network.contents.add(this);
	}

	/**
	 * @param queue target queue
	 */
	protected abstract void addNextNodes(Queue<NetworkNode<T, U>> queue);
}
