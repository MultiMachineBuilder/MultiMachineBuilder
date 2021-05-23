/**
 * 
 */
package mmb.WORLD.item;

import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;

import mmb.WORLD.inventory.ItemEntry;

/**
 * @author oskar
 *
 */
public class ItemEntityType extends ItemBase{
	
	private ItemEntityFactory factory;
	/**
	 * @author oskar
	 * This interface is used to define lambda expressions that produce item entitiess
	 */
	@FunctionalInterface public interface ItemEntityFactory{
		/**
		 * Creates an ItemEntity
		 * @return a new {@link ItemEntity}
		 */
		public ItemEntity create();
	}
	@Override
	public ItemEntry create() {
		return factory.create();
	}

	@Override
	public ItemEntry load(@Nullable JsonNode node) {
		ItemEntity result = factory.create();
		if(node != null) result.load(node);
		return result;
	}

	/**
	 * @return current item entity factory
	 */
	public ItemEntityFactory getFactory() {
		return factory;
	}

	/**
	 * @param factory new item entity factory
	 */
	public void setFactory(ItemEntityFactory factory) {
		this.factory = factory;
	}

}
