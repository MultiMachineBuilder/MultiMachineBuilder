package mmb.engine.item;

import com.fasterxml.jackson.databind.JsonNode;

import mmb.annotations.Nil;

@FunctionalInterface public interface ItemFactory{
	/**
	 * Creates a new item entry or loads the item from JSON
	 * @param nodeOrNull the JSON additional data node or null
	 * @param itemType 
	 * @return a new item entry
	 */
	public ItemEntry create(@Nil JsonNode nodeOrNull, ItemType itemType);
}