/**
 * 
 */
package mmb.content.modular.part;

import java.util.Objects;

import com.fasterxml.jackson.databind.JsonNode;

import mmb.PropertyExtension;
import mmb.annotations.NN;
import mmb.annotations.Nil;
import mmb.beans.IDrop;
import mmb.engine.chance.Chance;
import mmb.engine.item.Item;
import mmb.engine.item.ItemEntry;
import mmb.engine.item.ItemType;
import mmb.engine.recipe.ItemList;

/**
 *
 * @author oskar
 *
 */
public class PartType extends Item implements IDrop {
	@FunctionalInterface public static interface PartFactory{
		public PartEntry createPart(@Nil JsonNode json, PartType type);
	}
	
	/** Loads and creates parts */
	private PartFactory partFactory;
	/** 
	 * Sets a part factory. It must not be null. 
	 * @param factory new part factory. Must not be null
	 * @throws NullPointerException when factory == null
	 */
	public void setPartFactory(PartFactory factory) {
		Objects.requireNonNull(factory, "factory is null");
		partFactory = factory;
	}
	/** 
	 * Creates or loads parts. The part factory must be set first. 
	 * @param data JSON data to load. If null, the part is freshly created.
	 * @return a newly created part
	 * @throws IllegalStateException when the part factory has not been set
	 * @throws NullPointerException when the part factory returns null
	 */
	public PartEntry createPart(@Nil JsonNode data) {
		if(partFactory == null) throw new IllegalStateException("Part factory not initialized");
		return Objects.requireNonNull(partFactory.createPart(data, this), "The part factory returned null");
	}
	
	
	public ItemList returnToPlayer;
	
	/**
	 * Creates a part type
	 * @param id
	 * @param properties
	 */
	public PartType(String id, PropertyExtension[] properties) {
		super(id, properties);
		if(this instanceof Part p) this.partFactory = (x, type) -> p;
		else this.partFactory = Objects.requireNonNull(partFactory, "partFactory is null");
	}
	/**
	 * Loads a part entry using JSON payload, restricting the output type
	 * @param <T> expected type
	 * @param node data to load from
	 * @param cls expected type
	 * @return a new part entry with data, or null if failed
	 */
	@Nil public <T extends PartEntry> PartEntry loadPartExpectType(@Nil JsonNode node, @Nil Class<T> cls) {
		PartEntry item = createPart(node);
		if(cls != null && !cls.isInstance(item)) return null;
		return item;
	}
	
	public Chance dropItems;
	@Override
	public @NN Chance drop() {
		return dropItems;
	}
	@Override
	public void setDrop(Chance drop) {
		Objects.requireNonNull(drop, "drop is null");
	}
	
	
}
