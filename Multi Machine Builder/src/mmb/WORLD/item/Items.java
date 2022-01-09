/**
 * 
 */
package mmb.WORLD.item;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import mmb.WORLD.block.BlockType;
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
	@Nonnull                           private static final SelfSet<String, ItemType> _items =      new HashSelfSet<>();
	@Nonnull                           public  static final SelfSet<String, ItemType>  items =      Collects.unmodifiableSelfSet(_items);
	
	@Nonnull                           private static final Map<String, ItemType>     _deprecator = new HashMap<>();
	@SuppressWarnings("null") @Nonnull public  static final Map<String, ItemType>      deprecator = Collections.unmodifiableMap(_deprecator);
	
	@Nonnull                           private static final Set<String>               _keys =       new HashSet<>();
	@SuppressWarnings("null") @Nonnull public  static final Set<String>                keys =       Collections.unmodifiableSet(_keys);
	
	public static void register(ItemType type) {
		Objects.requireNonNull(type, "The block must not be null");
		Objects.requireNonNull(type.id(), "The block's ID must not be null");
		debug.printl("Adding "+type.id()+" with title "+type.title()+" and description:\n "+type.description());
		_items.add(type);
		_keys.add(type.id());
	}
	
	/**
	 * @param deprecated the deprecated ID of an item
	 * @param type item type to be deprecated
	 * @throws IllegalStateException if item is not already registered before deprecation
	 */
	public static void deprecate(String deprecated, BlockType type) {
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

}
