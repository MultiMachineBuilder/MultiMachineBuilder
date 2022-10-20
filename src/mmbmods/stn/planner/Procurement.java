/**
 * 
 */
package mmbmods.stn.planner;

import java.util.Objects;

import javax.annotation.Nonnull;

import mmb.world.crafting.RecipeOutput;
import mmbmods.stn.planner.RecipeNode.NodeSpec;

/**
 * @author oskar
 *
 */
public class Procurement {
	/** The source for this node*/
	@Nonnull public final Source node;
	/** Quantity of items */
	public final int quantity;
	
	/**
	 * Creates a recipe node from scratch
	 * @param node source
	 * @param quantity quantity of processes
	 * @throws IllegalArgumentException when quantity is non-positive
	 * @throws NullPointerException when source is null
	 */
	public Procurement(Source node, int quantity) {
		Objects.requireNonNull(node, "node is null");
		if(quantity <= 0) throw new IllegalArgumentException("Non-positive quantity");
		this.node = node;
		this.quantity = quantity;
	}
	
	/**
	 * Procurement node builder
	 * @author oskar
	 *
	 */
	public static class Builder{
		/** The node spec for this node*/
		@Nonnull public Source node;
		/** Quantity of items */
		public int quantity;
	}

	public static interface Source{
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
