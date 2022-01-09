/**
 * 
 */
package mmb.WORLD.block;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.annotation.Nullable;

import mmb.WORLD.blocks.ContentsBlocks;
import mmb.WORLD.item.Items;
import mmb.debug.Debugger;
import monniasza.collects.Collects;
import monniasza.collects.selfset.HashSelfSet;
import monniasza.collects.selfset.SelfSet;

/**
 * @author oskar
 *
 */
public class Blocks {
	private static Debugger debug = new Debugger("BLOCKS");
	private static final SelfSet<String, BlockType> _blocks = new HashSelfSet<>();
	/**
	 * An unmodifiable {@link SelfSet} of all {@link BlockType}s
	 */
	@SuppressWarnings("null")
	public static final SelfSet<String, BlockType> blocks = Collects.unmodifiableSelfSet(_blocks);
	
	public static final Map<String, BlockType> _deprecator = new HashMap<>();
	public static final Map<String, BlockType> deprecator = Collections.unmodifiableMap(_deprecator);
	
	private static final Set<String> _keys = new HashSet<>();
	public static final Set<String> keys = Collections.unmodifiableSet(_keys);
	
	public static void register(BlockType type) {
		Objects.requireNonNull(type, "The block must not be null");
		Objects.requireNonNull(type.id(), "The block's ID must not be null");
		debug.printl("Adding "+type.id()+" with title "+type.title()+" and description:\n "+type.description());
		Items.register(type);
		_blocks.add(type);
		_keys.add(type.id());
	}
	
	/**
	 * @param deprecated the deprecated ID of a block
	 * @param type block type to be deprecated
	 * @throws IllegalStateException if block is not already registered before deprecation
	 */
	public static void deprecate(String deprecated, BlockType type) {
		if(!blocks.contains(type)) throw new IllegalStateException("Block type "+type+" is not registered before deprecation");
		debug.printl("Deprecating "+type.id()+" as "+deprecated);
		_deprecator.put(deprecated, type);
	}
	/**
	 * Get a block with following ID, or null if block with given ID is not found
	 * @param name ID of the block
	 * @return a block with given name, or null if not found
	 */
	public static BlockType get(@Nullable String name) {
		BlockType get = blocks.get(name);
		if(get == null) get = deprecator.get(name);
		return get;
	}

	public static boolean isGround(BlockEntry ent) {
		return ent == ContentsBlocks.air || ent == ContentsBlocks.grass;
	}
	public static boolean isGround(BlockType ent) {
		return ent == ContentsBlocks.air || ent == ContentsBlocks.grass;
	}
}
