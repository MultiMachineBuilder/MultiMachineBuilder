/**
 * 
 */
package monniasza.collects.graph;

import javax.annotation.Nonnull;

import monniasza.collects.Identifiable;

/**
 * @author oskar
 * @param <V> type of vertex data
 * @param <E> type of edge data
 *
 */
public interface Edge<V, E> extends Identifiable<E>{
	/**
	 * @return graph which owns this edge
	 */
	public Graph<V, E> graph();
	/**
	 * @return the starting node
	 */
	public Node<V, E> start();
	/**
	 * @return the ending node
	 */
	public Node<V, E> end();
	/**
	 * @return weight of this edge
	 */
	public double weight();
	/**
	 * @param weight new weight
	 */
	public void setWeight(double weight);
	/**
	 * @return value of this edge
	 */
	@Nonnull public E value();
	@Override
	default E id() {
		return value();
	}
}
