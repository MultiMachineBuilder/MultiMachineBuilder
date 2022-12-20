/**
 * 
 */
package mmb.engine.item;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.SetMultimap;

import mmb.NN;
import mmb.Nil;
import mmb.engine.debug.Debugger;
import mmbbase.GameLoader;
import monniasza.collects.Collects;
import monniasza.collects.selfset.HashSelfSet;
import monniasza.collects.selfset.SelfSet;

/**
 * @author oskar
 *
 */
public class Items {
	private Items() {}
	private static Debugger debug = new Debugger("ITEMS");
	
	//Registration
	@NN private static final SelfSet<String, ItemType>     _items =      HashSelfSet.createNonnull(ItemType.class);
	/** String to item lookup */
	@NN public  static final SelfSet<String, ItemType>      items =      Collects.unmodifiableSelfSet(_items);	
	/**
	 * Registers a new item type
	 * @param type item type to register
	 * @throws NullPointerException when the item or it's ID is null
	 */
	public static void register(ItemType type) {
		Objects.requireNonNull(type, "The item must not be null");
		Objects.requireNonNull(type.id(), "The item's ID must not be null");
		debug.printl("Adding "+type.id()+" with title "+type.title()+" and description:\n "+type.description());
		_items.add(type);
		if(type instanceof ItemEntityType) 
			GameLoader.onIntegRun(() -> ItemRaw.make((ItemEntityType) type)); //ignore the result, all the magic is in the ItemRaw
		type.onregister();
	}
	
	//Deprecation
	@NN private static final Map<String, ItemType>    _deprecator = new HashMap<>();
	/** A map from deprecated IDs to items. Used to keep compatibility with older versions after renaming */
	@NN public  static final Map<String, ItemType>     deprecator = Collections.unmodifiableMap(_deprecator);
	/**
	 * @param deprecated the deprecated ID of an item
	 * @param type item type to be deprecated
	 * @throws IllegalStateException if item is not already registered before deprecation
	 */
	public static void deprecate(String deprecated, ItemType type) {
		if(!items.contains(type)) throw new IllegalStateException("Item type "+type+" is not registered before deprecation");
		debug.printl("Deprecating "+type.id()+" as "+deprecated);
		_deprecator.put(deprecated, type);
	}
	
	//Retrieval
	/**
	 * Get an item with following ID, or null if block with given ID is not found
	 * @param name ID of the block
	 * @return a block with given name, or null if not found
	 */
	public static ItemType get(@Nil String name) {
		ItemType get = items.get(name);
		if(get == null) get = deprecator.get(name);
		return get;
	}
	public static <T extends ItemType> T getExpectType(@Nil String name, Class<T> cls) {
		ItemType item = get(name);
		if(cls.isInstance(item)) return cls.cast(item);
		return null;
	}

	//Tags
	@NN private static final HashMultimap<String, ItemType> _tags = HashMultimap.create();
	/** Tag to items lookup */
	@NN public  static final SetMultimap<String, ItemType>  tags = Multimaps.unmodifiableSetMultimap(_tags);
	@NN private static final HashMultimap<ItemType, String> _btags = HashMultimap.create();
	/** Item to tags lookup */
	@NN public  static final SetMultimap<ItemType, String>  btags = Multimaps.unmodifiableSetMultimap(_btags);
	public static void tagItem(String tag, ItemType item) {
		debug.printl("Tagging "+item+" with "+tag);
		_tags.put(tag, item);
		_btags.put(item, tag);
	}
	public static void tagsItem(ItemType item, String... tags) {
		tagsItem(item, Arrays.asList(tags));
	}
	public static void tagsItem(ItemType item, Iterable<@NN String> tags) {
		for(String tag: tags) {
			tagItem(tag, item);
		}
	}
	public static void tagItems(String tag, ItemType... items) {
		tagItems(tag, Arrays.asList(items));
	}
	public static void tagsItems(String[] tag, ItemType... items) {
		tagsItems(tag, Arrays.asList(items));
	}
	public static void tagItems(String tag, Iterable<@NN ? extends ItemType> items) {
		for(ItemType item: items) {
			tagItem(tag, item);
		}
	}
	public static void tagsItems(String[] tag, Iterable<@NN ? extends ItemType> items) {
		for(ItemType item: items) {
			tagsItem(item, tag);
		}
	}

}
