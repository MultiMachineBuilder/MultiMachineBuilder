/**
 * 
 */
package mmb.WORLD.item;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.SetMultimap;

import mmb.MODS.loader.ModLoader;
import mmb.debug.Debugger;
import monniasza.collects.Collects;
import monniasza.collects.selfset.HashSelfSet;
import monniasza.collects.selfset.SelfSet;

/**
 * @author oskar
 *
 */
public class Items {
	private static Debugger debug = new Debugger("ITEMS");
	@Nonnull                           private static final SelfSet<String, ItemType>     _items =      new HashSelfSet<>();
	@Nonnull                           public  static final SelfSet<String, ItemType>      items =      Collects.unmodifiableSelfSet(_items);
	
	@Nonnull                           private static final Map<String, ItemType>    _deprecator = new HashMap<>();
	@SuppressWarnings("null") @Nonnull public  static final Map<String, ItemType>     deprecator = Collections.unmodifiableMap(_deprecator);
	
	@Nonnull                           private static final Set<String>                    _keys =       new HashSet<>();
	@SuppressWarnings("null") @Nonnull public  static final Set<String>                     keys =       Collections.unmodifiableSet(_keys);
	
	@SuppressWarnings("null") @Nonnull private static final HashMultimap<String, ItemType> _tags = HashMultimap.create();
	/** Tag to items lookup */
	@SuppressWarnings("null") @Nonnull public  static final SetMultimap<String, ItemType>  tags = Multimaps.unmodifiableSetMultimap(_tags);
	
	@SuppressWarnings("null") @Nonnull private static final HashMultimap<ItemType, String> _btags = HashMultimap.create();
	/** Item to tags lookup */
	@SuppressWarnings("null") @Nonnull public  static final SetMultimap<ItemType, String>  btags = Multimaps.unmodifiableSetMultimap(_btags);
	
	public static void register(ItemType type) {
		Objects.requireNonNull(type, "The block must not be null");
		Objects.requireNonNull(type.id(), "The block's ID must not be null");
		debug.printl("Adding "+type.id()+" with title "+type.title()+" and description:\n "+type.description());
		_items.add(type);
		_keys.add(type.id());
		if(type instanceof ItemEntityType) 
			ModLoader.onIntegRun(() -> ItemRaw.make((ItemEntityType) type)); //ignore the result, all the magic is in the ItemRaw
		type.onregister();
	}
	
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
	
	/**
	 * Get a block with following ID, or null if block with given ID is not found
	 * @param name ID of the block
	 * @return a block with given name, or null if not found
	 */
	public static ItemType get(@Nullable String name) {
		ItemType get = items.get(name);
		if(get == null) get = deprecator.get(name);
		return get;
	}

	public static void tagItem(String tag, ItemType item) {
		debug.printl("Tagging "+item+" with "+tag);
		_tags.put(tag, item);
		_btags.put(item, tag);
	}
	public static void tagsItem(ItemType item, String... tags) {
		tagsItem(item, Arrays.asList(tags));
	}
	public static void tagsItem(ItemType item, Iterable<@Nonnull String> tags) {
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
	public static void tagItems(String tag, Iterable<@Nonnull ? extends ItemType> items) {
		for(ItemType item: items) {
			tagItem(tag, item);
		}
	}
	public static void tagsItems(String[] tag, Iterable<@Nonnull ? extends ItemType> items) {
		for(ItemType item: items) {
			tagsItem(item, tag);
		}
	}

}
