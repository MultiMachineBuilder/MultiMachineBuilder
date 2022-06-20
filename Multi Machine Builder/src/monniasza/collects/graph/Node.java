/**
 * 
 */
package monniasza.collects.graph;

import java.util.Set;

import javax.annotation.Nonnull;

import monniasza.collects.Identifiable;

/**
 * @author oskar
 * @param <V> type of vertex data
 * @param <E> type of edge data
 *
 */
public interface Node<V, E> extends Identifiable<V>{
	/**
	 * @return graph which owns this node
	 */
	public Graph<V, E> graph();
	/**
	 * @return all incoming edges
	 */
	public Set<Edge<V, E>> incoming();
	/**
	 * @return all outgoing edges
	 */
	public Set<Edge<V, E>> outgoing();
	/**
	 * @return value of this node
	 */
	@Nonnull public V value();
	@Override
	default V id() {
		return value();
	}
}
