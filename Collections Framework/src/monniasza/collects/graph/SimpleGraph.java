/**
 * 
 */
package monniasza.collects.graph;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nullable;

import monniasza.collects.Collects;
import monniasza.collects.selfset.HashSelfSet;
import monniasza.collects.selfset.SelfSet;

/**
 * @author oskar
 * @param <V> type of vertex data
 * @param <E> type of edge data
 *
 */
public class SimpleGraph<V, E> implements Graph<V, E> {
	private class EdgeHelper implements Edge<V, E>{
		private final E value;
		final NodeHelper start;
		final NodeHelper end;
		@Override
		public Graph<V, E> graph() {
			return SimpleGraph.this;
		}

		public EdgeHelper(E value, NodeHelper start, NodeHelper end) {
			super();
			this.value = value;
			this.start = start;
			this.end = end;
		}

		@Override
		public Node<V, E> start() {
			return start;
		}

		@Override
		public Node<V, E> end() {
			return end;
		}
		private double weight = 1;
		@Override
		public double weight() {
			return weight;
		}

		@Override
		public E value() {
			return value;
		}

		@Override
		public void setWeight(double weight) {
			this.weight = weight;
		}
	}
	private class NodeHelper implements Node<V, E>{
		Set<EdgeHelper> incoming = new HashSet<>();
		Set<EdgeHelper> outgoing = new HashSet<>();
		private final V value;
		public NodeHelper(V value) {
			super();
			this.value = value;
		}

		@Override
		public Graph<V, E> graph() {
			return SimpleGraph.this;
		}

		@Override
		public Set<Edge<V, E>> incoming() {
			return Collections.unmodifiableSet(incoming);
		}

		@Override
		public Set<Edge<V, E>> outgoing() {
			return Collections.unmodifiableSet(outgoing);
		}

		@Override
		public V value() {
			return value;
		}
		
	}

	//Edges
	private final SelfSet<E, EdgeHelper> edges = new HashSelfSet<>();
	@Override
	public SelfSet<E, Edge<V, E>> edgesSet() {
		return Collects.unmodifiableSelfSet(edges);
	}
	@Override
	public Edge<V, E> getEdge(@Nullable E value) {
		if(value == null) return null;
		return edges.get(value);
	}
	@Override
	public Edge<V, E> createOrGetEdge(V start, V end, E value) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Edge<V, E> createEdge(V start, V end, E value) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean removeEdge(E value) {
		EdgeHelper edge = edges.get(value);
		if(edge == null) return false;
		boolean result = edges.removeKey(value);
		if(result) {
			NodeHelper s = edge.start;
			NodeHelper e = edge.end;
			s.outgoing.remove(edge);
			e.incoming.remove(edge);
		}
		return result;
	}

	//Nodes
	private final SelfSet<V, NodeHelper> nodes = new HashSelfSet<>();
	@Override
	public SelfSet<V, Node<V, E>> vertsSet() {
		return Collects.unmodifiableSelfSet(nodes);
	}
	@Override
	public Node<V, E> getNode(@Nullable V value) {
		if(value == null) return null;
		return nodes.get(value);
	}
	@Override
	public Node<V, E> createOrGetNode(V value) {
		Node<V, E> node = getNode(value);
		if(node == null) return createNode(value);
		return node;
	}
	@Override
	public Node<V, E> createNode(V value) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean removeNode(V value) {
		Node<V, E> node = getNode(value);
		if(node == null) return false;
		//Remove the edges
		for(Edge<V, E> edge: node.incoming()) {
			removeEdge(edge.value());
		}
		for(Edge<V, E> edge: node.outgoing()) {
			removeEdge(edge.value());
		}
		return nodes.removeKey(value);
	}
}
