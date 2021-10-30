/**
 * 
 */
package monniasza.collects.graph;

import java.util.Collection;
import java.util.Set;

import javax.annotation.Nullable;

import monniasza.collects.selfset.SelfSet;

/**
 * @author oskar
 * A superinterface for all graphs
 * @param <V> type of vertex data
 * @param <E> type of edge data
 */
public interface Graph<V, E> {
	/**
	 * @return set of all edges
	 */
	public SelfSet<E, ? extends Edge<V, E>> edgesSet();
	/**
	 * @return set of all vertices
	 */
	public SelfSet<V, ? extends Node<V, E>> vertsSet();
	/**
	 * Gets a node
	 * @param value value of this node
	 * @return the obtained node, or null if it does not exist
	 */
	public Node<V, E> getNode(@Nullable V value);
	/**
	 * Gets an edge
	 * @param value value of the edge
	 * @return the created edge, or null if it does not exist
	 */
	public Edge<V, E> getEdge(@Nullable E value);
	/**
	 * Creates a new node
	 * @param value value of this node
	 * @return the created node, or existing node
	 */
	public Node<V, E> createOrGetNode(V value);
	/**
	 * Creates a new edge
	 * @param start starting node ID
	 * @param end ending node ID
	 * @param value value of the edge
	 * @return the created edge, or the existing edge
	 */
	public Edge<V, E> createOrGetEdge(V start, V end, E value);
	/**
	 * Creates a new node
	 * @param value value of this node
	 * @return the created node, or null if node with a value already exists
	 */
	public Node<V, E> createNode(V value);
	/**
	 * Creates a new edge
	 * @param start starting node ID
	 * @param end ending node ID
	 * @param value value of the edge
	 * @return the created edge, or null if it already exists in the same direction
	 */
	public Edge<V, E> createEdge(V start, V end, E value);
	/**
	 * Removes a node with given ID
	 * Removal of the node removes associated edges
	 * @param value node ID
	 * @return was the node removed?
	 */
	public boolean removeNode(V value);
	/**
	 * Removes a node with given ID
	 * @param value edge ID
	 * @return was the edge removed?
	 */
	public boolean removeEdge(E value);
	
	
	
}
