/**
 * 
 */
package mmb.content.stn.planner;

import java.util.Objects;

import mmb.annotations.NN;
import mmb.annotations.Nil;
import mmb.engine.recipe.RecipeOutput;

/**
 * The recipe node.
 * Recipe nodes bind quantity and a node spec
 * @author oskar
 */
public final class RecipeNode {
	/** The node spec for this node*/
	@NN public final NodeSpec node;
	/** Quantity of items */
	public final int quantity;
	
	/**
	 * Creates a recipe node from scratch
	 * @param node node spec
	 * @param quantity quantity of processes
	 * @throws IllegalArgumentException when quantity is non-positive
	 * @throws NullPointerException when node spec is null
	 */
	public RecipeNode(NodeSpec node, int quantity) {
		Objects.requireNonNull(node, "node is null");
		if(quantity <= 0) throw new IllegalArgumentException("Non-positive quantity");
		this.node = node;
		this.quantity = quantity;
	}
	public RecipeNode(Builder builder) {
		this(builder.node, builder.quantity);
	}

	/**
	 * Recipe node builder
	 * @author oskar
	 *
	 */
	public static class Builder{
		/** The node spec for this node*/
		@NN public NodeSpec node;
		/** Quantity of items */
		public int quantity;
	}
	
	/**
	 * More information about a recipe node
	 * @author oskar
	 */
	public static interface NodeSpec{
		/**
		 * @return items consumed per operation
		 */
		public RecipeOutput inputs();
		/**
		 * @return items produced per operation
		 */
		public RecipeOutput outputs();
		/**
		 * Gets the remaining items to source
		 * @return remaining items, or -1 if unlimited
		 */
		public int remaining();
	}
}
