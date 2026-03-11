/**
 * 
 */
package mmb.engine.item;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.SetMultimap;

import mmb.GameLoader;
import mmb.annotations.NN;
import mmb.annotations.Nil;
import mmb.engine.debug.Debugger;
import mmb.engine.recipe3.Group;
import monniasza.collects.Collects;
import monniasza.collects.selfset.HashSelfSet;
import monniasza.collects.selfset.SelfSet;

/**
 * A set of item utilities
 * @author oskar
 */
public class Items {
	private Items() {}
	private static Debugger debug = new Debugger("ITEMS");
	
	//Registration
	@NN private static final SelfSet<String, ItemType> internalItems = HashSelfSet.createNonnull(ItemType.class);
	/** String to item lookup. Deprecation is not supported. To find deprecated items, use {@link #get(String)} */
	@NN public static final SelfSet<String, ItemType> items = Collects.unmodifiableSelfSet(internalItems);	
	
	@NN private static final SelfSet<String, Group> internalSingletonGroups = HashSelfSet.createNonnull(Group.class);
	/** All singleton item groups. Accessible by group ID. */
	@NN public static final SelfSet<String, Group> singletonGroups = Collects.unmodifiableSelfSet(internalSingletonGroups);
	
	/**
	 * Registers a new item type. Useful only in {@link ItemType}, since it is called there
	 * @param type item type to register
	 * @throws NullPointerException when the item or it's ID is null
	 */
	public static void register(ItemType type) {
		Objects.requireNonNull(type, "The item must not be null");
		Objects.requireNonNull(type.id, "The item's ID must not be null");
		debug.printl("Adding "+type.id);
		internalItems.add(type);
		internalSingletonGroups.add(type.singletonGroup);
		if(!(type instanceof Item)) 
			GameLoader.onIntegRun(() -> ItemRaw.make(type)); //ignore the result, all the magic is in the ItemRaw
	}
	
	//Deprecation
	@NN private static final Map<String, ItemType> _deprecator = new HashMap<>();
	/** A map from deprecated IDs to items. Used to keep compatibility with older versions after renaming */
	@NN public static final Map<String, ItemType> deprecator = Collections.unmodifiableMap(_deprecator);
	/**
	 * Maps a different ID to resolve to the specified item using the {@link #get(String)} method
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
	 * @param name item ID
	 * @return an item with given name, or null if not found
	 */
	@Nil public static ItemType get(@Nil String name) {
		if(name == null) return null;		
		ItemType get = items.get(name);
		if(get == null) get = deprecator.get(name);
		return get;
	}
	/**
	 * Get an item with following ID with type restrictions, or null if block with given ID is not found
	 * @param <T> generic restriction type
	 * @param name item ID
	 * @param cls item type restriction
	 * @return an item with given name, or null if not found
	 */
	@Nil public static <T extends ItemType> T getExpectType(@Nil String name, Class<T> cls) {
		ItemType item = get(name);
		if(cls.isInstance(item)) return cls.cast(item);
		return null;
	}

	//Tags
	@NN private static final HashMultimap<String, ItemType> _tags = HashMultimap.create();
	/** Tag to items lookup */
	@NN public static final SetMultimap<String, ItemType>  tags = Multimaps.unmodifiableSetMultimap(_tags);
	@NN private static final HashMultimap<ItemType, String> _btags = HashMultimap.create();
	/** Item to tags lookup */
	@NN public static final SetMultimap<ItemType, String>  btags = Multimaps.unmodifiableSetMultimap(_btags);
	/**
	 * Tags a single item
	 * @param tag item tag
	 * @param item item to be tagged
	 */
	public static void tagItem(String tag, ItemType item) {
		debug.printl("Tagging "+item+" with "+tag);
		_tags.put(tag, item);
		_btags.put(item, tag);
	}
	/**
	 * Tags a single item with mutiple tags
	 * @param item item to be tagged
	 * @param tags1 item tags
	 */
	public static void tagsItem(ItemType item, String... tags1) {
		tagsItem(item, Arrays.asList(tags1));
	}
	/**
	 * Tags a single item with mutiple tags
	 * @param item item to be tagged
	 * @param tags1 item tags
	 */
	public static void tagsItem(ItemType item, Iterable<@NN String> tags1) {
		for(String tag: tags1) {
			tagItem(tag, item);
		}
	}
	/**
	 * Tags mutiple items with one tag
	 * @param tag item tags
	 * @param items1 items to be tagged
	 */
	public static void tagItems(String tag, ItemType... items1) {
		tagItems(tag, Arrays.asList(items1));
	}
	/**
	 * Tags each item with all given tags
	 * @param tag item tags
	 * @param items1 items to be tagged
	 */
	public static void tagsItems(String[] tag, ItemType... items1) {
		tagsItems(tag, Arrays.asList(items1));
	}
	/**
	 * Tags mutiple items with one tag
	 * @param tag item tags
	 * @param items1 items to be tagged
	 */
	public static void tagItems(String tag, Iterable<@NN ? extends ItemType> items1) {
		for(ItemType item: items1) {
			tagItem(tag, item);
		}
	}
	/**
	 * Tags each item with all given tags
	 * @param tag item tags
	 * @param items1 items to be tagged
	 */
	public static void tagsItems(String[] tag, Iterable<@NN ? extends ItemType> items1) {
		for(ItemType item: items1) {
			tagsItem(item, tag);
		}
	}
	
	/**
	 * Gets groups for an item entry or null and verifies them
	 * @param item item to get groups of
	 * @return set of all item groups for given item entry, or {@link #Group.NONE} when item == null
	 */
	public static Set<Group> getGroups(@Nil ItemEntry item) {
		if(item == null) return Set.of(Group.NONE);
		ItemGroups result = item.getGroups();
		result.enforce(item.itemType());
		return result.groups;
	}
	/**
	 * Gets groups for an item type or null and verifies them
	 * @param item item to get groups of
	 * @return set of all item groups for given item entry, or {@link #Group.NONE} when item == null
	 */
	public static Set<Group> getGroups(@Nil ItemType item) {
		if(item == null) return Set.of(Group.NONE);
		return item.groups;
	}

}
